package com.example.mrizudev.democracyinfo.repositories;

import com.example.mrizudev.democracyinfo.model.User;
import com.example.mrizudev.democracyinfo.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Integer> {
    Vote findById(int id);
}
