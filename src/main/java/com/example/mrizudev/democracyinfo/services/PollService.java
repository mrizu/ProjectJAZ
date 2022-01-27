package com.example.mrizudev.democracyinfo.services;

import com.example.mrizudev.democracyinfo.repositories.OptionRepository;
import com.example.mrizudev.democracyinfo.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class PollService {
    @PersistenceContext
    EntityManager entityManager;
    PollRepository pollRepository;
    OptionRepository optionRepository;

    @Autowired
    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }
}
