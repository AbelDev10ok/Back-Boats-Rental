-- FUNCTIONS OF BOATS

-- RECORDEMOS PARA LA LOGICA LOS BOTES Y MARINS DE UNA RENTA CANCELADA Y ELIMINADA DEBE PODER REASIGNARSE A OTRAS RENTAS
DROP FUNCTION IF EXISTS get_available_boats(start_date date, end_date date);


-- BUSCAMOS BOTES QUE ESTEN DISPONIBLES LAS FECHAS DADAS Y QUE NO ESTEN EN RENTAS CONFIRMADAS
CREATE OR REPLACE FUNCTION get_available_boats(start_date date, end_date date)
RETURNS SETOF boat AS $$ -- devolvemos conjunto de filas cada fila representa un barco
BEGIN
  RETURN QUERY SELECT b.* -- busca seleccionar todos los campos de la tabla boat
  FROM boat b
  WHERE b.enabled = true
  AND NOT EXISTS -- devuelve verdadero si la subconsulta que contiene no devuelve ninguna fila es decir barco disponible para devolver
  (
    SELECT 1 -- no importa lo que devuelva lo importante que que devuelva filas o no
    -- si devuelve al menos una fila significa que existe una renta y el barco no cumple las condiciones (no disponible)
    FROM rental r
    WHERE r.boat_id = b.tuition    -- Assuming tuition is the boat_id. Change if different.
    AND (
        (r.date_init BETWEEN start_date AND end_date) OR
        (r.date_end BETWEEN start_date AND end_date) OR
        (start_date BETWEEN r.date_init AND r.date_end) OR  -- Check if start_date falls within a rental
        (end_date BETWEEN r.date_init AND r.date_end)
        )      -- Check if end_date falls within a rental
    AND r.state NOT IN ('CANCELADA', 'FINALIZADA') -- ES DECIR ESTADO NO SEA CANCELADO NI FINALIZADO, QUE ESTE CONFIRMAO O PENDIENTE
  );
END;
$$ LANGUAGE plpgsql;



drop Function if exists botes_disponibles(date,date);

create or replace function botes_disponibles(p_date_init date,p_date_end date)
returns table(
    tuition bigint,
    model varchar
)
as $$
begin 
    RETURN query
    select b.tuition, b.model 
    from boat b where b.enabled = TRUE
    AND not EXISTS 
        (select 1 from rental r where r.boat_id = b.tuition 
            and ((r.date_init BETWEEN p_date_init and p_date_end)
            or (r.date_end BETWEEN p_date_init and p_date_end)
            or (p_date_init BETWEEN r.date_init and r.date_end))
    AND r.state NOT IN ('CANCELADA', 'FINALIZADA') -- Filter by rental state
        );
end;
$$ LANGUAGE plpgsql;
-------------------------------------------------------------

-- FUNCTIONS AND TRIGGERS OF RENTAL

DROP FUNCTION IF EXISTS asignar_marin_a_rental(rental_id BIGINT, p_date_init timestamp without time zone, p_date_end timestamp without time zone);
-- Function to assign an available sailor, taking rental dates as parameters

-- RECORDEMOS PARA LA LOGICA LOS BOTES Y MARINS DE UNA RENTA CANCELADA Y ELIMINADA DEBE PODER REASIGNARSE A OTRAS RENTAS


CREATE OR REPLACE FUNCTION asignar_marin_a_rental(rental_id BIGINT, p_date_init timestamp without time zone, p_date_end timestamp without time zone)
RETURNS VOID AS $$
DECLARE
    available_sailor_id BIGINT;
BEGIN
    -- Find an available sailor, considering all boat assignments and dates
    SELECT m.id INTO available_sailor_id
    FROM marin m
    WHERE NOT EXISTS (
        SELECT 1
        FROM boat b
        JOIN rental r ON b.tuition = r.boat_id
        WHERE m.id = b.marin_id
        AND (p_date_init <= r.date_end AND p_date_end >= r.date_init)  -- Use the parameters here
        AND r.state NOT IN ('CANCELADA', 'FINALIZADA')  -- Corrected condition

    );


    IF available_sailor_id IS NOT NULL THEN
        UPDATE boat
        SET marin_id = available_sailor_id
        WHERE tuition = (SELECT boat_id FROM rental WHERE id = rental_id);
    ELSE
        RAISE EXCEPTION 'No tenemos marins disponibles para esta reserva.';
    END IF;

    RETURN;
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS call_assign_sailor_to_rental();
CREATE OR REPLACE FUNCTION call_assign_sailor_to_rental()
RETURNS TRIGGER AS $$
BEGIN
  PERFORM asignar_marin_a_rental(NEW.id, NEW.date_init, NEW.date_end); 
  RETURN NULL;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS trigger_asignar_marin ON rental;
CREATE TRIGGER trigger_assign_marin
AFTER INSERT ON rental
FOR EACH ROW
EXECUTE PROCEDURE call_assign_sailor_to_rental();



DROP FUNCTION IF EXISTS verificar_rental_boat_available();

-- RECORDEMOS PARA LA LOGICA LOS BOTES Y MARINS DE UNA RENTA CANCELADA 
-- Y ELIMINADA DEBE PODER REASIGNARSE A OTRAS RENTAS
-- VERIFICAMOS QUE SE PUEDA RENTAR EN ESA FECHA CON ESE BOTE Y TAMBIEN POR ESE USUARIO
-- EXCLUIMOS EL ID DE NUESTRA RENTA PARA QUE NO LA TOME EN CUENTA.
CREATE OR REPLACE FUNCTION verificar_rental_boat_available()
RETURNS TRIGGER AS $$
DECLARE 
    rental_exists INTEGER;
BEGIN
    SELECT 1
    INTO rental_exists
    FROM rental r
    WHERE r.boat_id = NEW.boat_id
        AND NEW.date_init < r.date_end   -- Lógica de comparación corregida
        AND NEW.date_end > r.date_init   -- Lógica de comparación corregida
        AND r.state IN ('CONFIRMADO', 'PENDIENTE')  --  <- Aquí el cambio
        AND CASE WHEN TG_OP = 'UPDATE' THEN r.id <> NEW.id ELSE TRUE END; -- si es update excluyo mi renta

    IF rental_exists IS NOT NULL THEN
        RAISE EXCEPTION 'This boat is already rented for the specified dates.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS tg_verificar_rental_boat_available ON rental;

CREATE TRIGGER tg_verificar_rental_boat_available
BEFORE INSERT OR UPDATE ON rental
FOR EACH ROW
EXECUTE FUNCTION verificar_rental_boat_available();

