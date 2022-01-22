package com.example.mrizudev.democracyinfo.services;

import com.example.mrizudev.democracyinfo.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class VoteService {
    VoteRepository voteRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public VoteService(VoteRepository voteRepository){
        this.voteRepository = voteRepository;
    }

    public void addVote(int user_id, int poll_id, int option_id){
        String queryBody = "INSERT INTO vote (user_id, poll_id, option_id) VALUES (:user_id, :poll_id, :option_id)";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("user_id", user_id);
        query.setParameter("poll_id", poll_id);
        query.setParameter("option_id", option_id);
    }
}
