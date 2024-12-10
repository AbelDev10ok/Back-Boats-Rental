package com.manager.boats.rental.boats_rental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Repository
public interface IBoatRepository extends JpaRepository<Boat,Long>{
    boolean existsById(@NonNull Long id);
    
    // @Query("SELECT b FROM Boat b WHERE b.marin IS  NOT NULL AND b.tuition NOT IN (SELECT r.boat.tuition FROM Rental r WHERE (r.dateInit BETWEEN :startDate AND :endDate) OR (r.dateEnd BETWEEN :startDate AND :endDate))")
    // List<Boat> getBoatsAvaliable(Date startDate, Date endDate);

    // OTRA FORMA SI YA TENGO LA FUNCION CREADA EN MI BASE DE DATOS
    @Query(value = "SELECT * FROM get_available_boats(:startDate, :endDate)", nativeQuery = true)
    List<Boat> getBoatsAvaliable(Date startDate, Date endDate);

    Optional<Boat> findByTuition(Long tuition);

    Optional<Boat> deleteByTuition(Long tuition);
}
