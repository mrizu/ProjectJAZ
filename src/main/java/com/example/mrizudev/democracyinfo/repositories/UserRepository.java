package com.example.mrizudev.democracyinfo.repositories;

import com.example.mrizudev.democracyinfo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
}