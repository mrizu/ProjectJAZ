package com.example.mrizudev.democracyinfo.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vote {
    private int id;
    private int user_id;
    private int poll_id;
    private int option_id;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name ="user_id")
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Basic
    @Column(name="poll_id")
    public int getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(int poll_id) {
        this.poll_id = poll_id;
    }

    @Basic
    @Column(name="option_id")
    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }
}
