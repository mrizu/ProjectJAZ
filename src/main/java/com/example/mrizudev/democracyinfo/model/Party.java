package com.example.mrizudev.democracyinfo.model;

import org.springframework.context.annotation.Configuration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;

public class Party {
    private int id;
    private String name;
    
    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Basic
    @Column(name = "id")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
