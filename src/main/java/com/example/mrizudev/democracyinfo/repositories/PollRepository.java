package com.example.mrizudev.democracyinfo.repositories;

import com.example.mrizudev.democracyinfo.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends JpaRepository<Poll,Integer> {
    Poll findById(int id);
}
