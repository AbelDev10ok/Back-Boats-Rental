package com.manager.boats.rental.boats_rental.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manager.boats.rental.boats_rental.persistence.models.Rental;

@Repository
public interface IRentalRepository extends JpaRepository<Rental,Long>{
    Optional<Rental> findByUserId(Long id);
}
