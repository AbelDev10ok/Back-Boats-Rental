-- Active: 1728437573594@@localhost@5432@boats_rental@public
INSERT INTO boat (tuition, type, ability, name, model, enabled, price_for_day) VALUES
(1234567, 'YATE', 10, 'Yate de lujo 1', 'Princess V50', TRUE, 1500),
(8901234, 'LANCHA', 6, 'Lancha deportiva 1', 'Sea Ray SLX 280', TRUE, 800),
(5678901, 'VELERO', 4, 'Velero clásico 1', 'Beneteau Oceanis 40.1', TRUE, 600),
(2345678, 'YATE', 8, 'Yate familiar 1', 'Sunseeker Manhattan 52', TRUE, 1200),
(9012345, 'LANCHA', 4, 'Lancha pesquera 1', 'Boston Whaler 240 Vantage', TRUE, 500),
(6789012, 'VELERO', 2, 'Velero pequeño 1', 'Hunter 260', TRUE, 300),
(3456789, 'YATE', 12, 'Superyate 1', 'Azimut Grande 35 Metri', TRUE, 2500),
(123456, 'LANCHA', 8, 'Lancha rápida 1', 'Cigarette Racing 50 Marauder', TRUE, 1000),
(7890123, 'VELERO', 6, 'Velero de carreras 1', 'J/111', TRUE, 700),
(4567890, 'YATE', 4, 'Yate de pesca 1', 'Viking Yachts 92 Convertible', TRUE, 2000),
(1011121, 'LANCHA', 2, 'Lancha de paseo 1', 'Bayliner VR5 Bowrider', TRUE, 400),
(1314151, 'VELERO', 8, 'Velero de crucero 1', 'Hanse 548', TRUE, 900),
(1617181, 'YATE', 6, 'Yate deportivo 1', 'Fairline Targa 65 GTO', TRUE, 1800),
(1920212, 'LANCHA', 10, 'Lancha de lujo 1', 'Riva Aquariva Super', TRUE, 1300),
(2223242, 'VELERO', 4, 'Velero de recreo 1', 'Catalina 30', TRUE, 550),
(7418529, 'YATE', 8, 'Yate moderno 1', 'Sanlorenzo SX88', TRUE, 2200),
(6309518, 'LANCHA', 6, 'Lancha elegante 1', 'Chris-Craft Launch 30', TRUE, 950),
(4785213, 'VELERO', 2, 'Velero práctico 1', 'MacGregor 26M', TRUE, 350),
(9632587, 'YATE', 10, 'Yate espacioso 1', 'Ocean Alexander 27 Explorer', TRUE, 2800),
(4159753, 'LANCHA', 4, 'Lancha versátil 1', 'Grady-White Fisherman 257', TRUE, 650);



INSERT INTO marin (name, lastname, dni) VALUES
('Juan', 'Pérez', '12345678'),
('María', 'García', '87654321'),
('Pedro', 'Rodríguez', '11223344'),
('Ana', 'Martínez', '44332211'),
('Luis', 'González', '55667788'),
('Isabel', 'Sánchez', '88776655'),
('Carlos', 'López', '99001122'),
('Sofía', 'Hernández', '22110099'),
('Miguel', 'Díaz', '33445566'),
('Lucía', 'Ramírez', '66554433');

INSERT INTO role (id, name) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');


INSERT INTO users (email, password, enabled) VALUES('cielo@gmail.com','12345',true);
INSERT INTO users (email, password, enabled) VALUES('ariel@gmail.com','12345',true);
INSERT INTO users (email, password, enabled) VALUES('martinez@gmail.com','12345',true);

INSERT INTO users_roles (user_id, role_id) VALUES(5,1);
INSERT INTO users_roles (user_id, role_id) VALUES(6,1);
INSERT INTO users_roles (user_id, role_id) VALUES(7,1);


-- Renta 1: Usuario con email 'cielo@gmail.com' alquila el barco con tuition 1234567
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-05-03', '2024-05-06', 8901234, id, 4500, TRUE, 'CONFIRMADO', 'token123'
FROM users WHERE email = 'cielo@gmail.com';

-- Renta 2: Usuario con email 'ariel@gmail.com' alquila el barco con tuition 8901234
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-05-10', '2024-05-15', 8901234, id, 4000, FALSE, 'PENDIENTE', 'token456'
FROM users WHERE email = 'ariel@gmail.com';

-- Renta 3: Usuario con email 'martinez@gmail.com' alquila el barco con tuition 5678901
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-05-18', '2024-05-22', 5678901, id, 2400, TRUE, 'FINALIZADA', 'token789'
FROM users WHERE email = 'martinez@gmail.com';


-- Renta 4: cielo@gmail.com alquila el barco con tuition 2345678
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-06-01', '2024-06-05', 2345678, id, 6000, TRUE, 'CONFIRMADO', 'token101'
FROM users WHERE email = 'cielo@gmail.com';

-- Renta 5: ariel@gmail.com alquila el barco con tuition 9012345
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-06-08', '2024-06-12', 9012345, id, 2500, FALSE, 'PENDIENTE', 'token102'
FROM users WHERE email = 'ariel@gmail.com';

-- Renta 6: martinez@gmail.com alquila el barco con tuition 6789012
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-06-15', '2024-06-18', 6789012, id, 1200, TRUE, 'FINALIZADA', 'token103'
FROM users WHERE email = 'martinez@gmail.com';

-- Renta 7: cielo@gmail.com alquila el barco con tuition 3456789
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-07-02', '2024-07-07', 3456789, id, 17500, TRUE, 'CONFIRMADO', 'token104'
FROM users WHERE email = 'cielo@gmail.com';

-- Renta 8: ariel@gmail.com alquila el barco con tuition 123456
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-07-09', '2024-07-14', 123456, id, 7000, FALSE, 'PENDIENTE', 'token105'
FROM users WHERE email = 'ariel@gmail.com';


-- Renta 9: martinez@gmail.com alquila el barco con tuition 7890123
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-07-16', '2024-07-20', 7890123, id, 4200, TRUE, 'CANCELADO', 'token106'
FROM users WHERE email = 'martinez@gmail.com';

-- Renta 10: cielo@gmail.com alquila el barco con tuition 4567890
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-08-01', '2024-08-06', 4567890, id, 12000, FALSE, 'PENDIENTE', 'token107'
FROM users WHERE email = 'cielo@gmail.com';

-- Renta 11: ariel@gmail.com alquila el barco con tuition 1011121
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-08-08', '2024-08-11', 1011121, id, 1600, TRUE, 'CONFIRMADO', 'token108'
FROM users WHERE email = 'ariel@gmail.com';

-- Renta 12: martinez@gmail.com alquila el barco con tuition 1314151
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-08-15', '2024-08-20', 1314151, id, 4500, TRUE, 'FINALIZADA', 'token109'
FROM users WHERE email = 'martinez@gmail.com';

-- Renta 13: cielo@gmail.com alquila el barco con tuition 1617181
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-09-03', '2024-09-08', 1617181, id, 10800, TRUE, 'CONFIRMADO', 'token110'
FROM users WHERE email = 'cielo@gmail.com';

-- Renta 14: cielo@gmail.com alquila el barco con tuition 8901234
INSERT INTO rental (date_init, date_end, boat_id, user_id, total, confirmed, state, confirmation_token)
SELECT '2024-05-03', '2024-05-06', 8901234, id, 4500, TRUE, 'CONFIRMADO', 'token123'
FROM users WHERE email = 'cielo@gmail.com';



