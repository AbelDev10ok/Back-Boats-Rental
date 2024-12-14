package com.manager.boats.rental.boats_rental.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import com.manager.boats.rental.boats_rental.persistence.models.Rental;

@Repository
public interface IRentalRepository extends JpaRepository<Rental,Long>{
    Optional<Rental> findByUserId(Long id);
    Optional<Rental> findByConfirmationToken(String token);  //  El método ya define la consulta
    List<Rental> findAllByUserId(Long id);

    @Query("SELECT r FROM Rental r WHERE r.boat = :boat AND " +
           "((:dateInit BETWEEN r.dateInit AND r.dateEnd) OR " + // Check if dateInit falls within existing rental
           "(:dateEnd BETWEEN r.dateInit AND r.dateEnd) OR " +   // Check if dateEnd falls within existing rental
           "(r.dateInit BETWEEN :dateInit AND :dateEnd) OR " +   // Check if existing rental starts within new range
           "(r.dateEnd BETWEEN :dateInit AND :dateEnd))")        // Check if existing rental ends within new range    
    List<Rental> findRentalsByBoatAndDateRange(Boat boat, Date dateInit, Date dateEnd);
    
    @Query("SELECT r FROM Rental r JOIN FETCH r.user u JOIN FETCH r.boat b") // <- Aquí el cambio
    List<Rental> findAllWithUserAndBoat();

    // YA NO UTILIZO ... PERO ES UNA FORMA DE LLAMAR A UNA FUNCION
    // @Query(value = "SELECT encontrar_marinero_disponible(:fechaInicio, :fechaFin)", nativeQuery = true)
    // Long encontrarMarineroDisponible(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);
}
