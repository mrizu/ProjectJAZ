package com.example.mrizudev.democracyinfo.repositories;

import com.example.mrizudev.democracyinfo.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    Option findById(int id);
}
