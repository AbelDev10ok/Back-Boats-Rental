package com.manager.boats.rental.boats_rental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manager.boats.rental.boats_rental.persistence.models.Users;

@Repository
public interface IUserRepository extends JpaRepository<Users,Long> {
    boolean existsByEmail(String email);
}
