package com.manager.boats.rental.boats_rental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;
import java.util.List;
import java.util.Date;

@Repository
public interface IBoatRepository extends JpaRepository<Boat,Long>{
    // List<Boat> findAll();
    boolean existsById(Long id);
    @Query("SELECT b FROM Boat b WHERE b.tuition NOT IN (SELECT r.boat.tuition FROM Rental r WHERE (r.dateInit BETWEEN :startDate AND :endDate) OR (r.dateEnd BETWEEN :startDate AND :endDate))")
    List<Boat> getBoatsAvaliable(Date startDate, Date endDate);

}
