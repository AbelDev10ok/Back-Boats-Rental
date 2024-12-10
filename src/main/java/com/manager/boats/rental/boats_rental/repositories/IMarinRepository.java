package com.manager.boats.rental.boats_rental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manager.boats.rental.boats_rental.persistence.models.Marin;

@Repository
public interface IMarinRepository extends JpaRepository<Marin,Long>{
    boolean existsByDni(String dni);
    Marin findByDni(String dni);
}
