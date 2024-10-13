package com.manager.boats.rental.boats_rental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manager.boats.rental.boats_rental.persistence.models.Boat;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBoatRepository extends JpaRepository<Boat,Long>{
    List findAll();
}
