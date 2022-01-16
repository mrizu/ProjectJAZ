package com.example.mrizudev.democracyinfo.model;


import javax.persistence.*;


@Entity
public class User {
    private int id;
    private String username;
    private String HMAC512_HASH;
    private String jwt_token;
    private String email;
    private int party_id;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "HMAC512_HASH")
    public String getHMAC512_HASH() {
        return HMAC512_HASH;
    }

    public void setHMAC512_HASH(String HMAC512_HASH) {
        this.HMAC512_HASH = HMAC512_HASH;
    }

    @Basic
    @Column(name = "jwt_token")
    public String getJwt_token() {
        return jwt_token;
    }

    public void setJwt_token(String jwt_token) {
        this.jwt_token = jwt_token;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "party_id")
    public int getParty_id() {
        return party_id;
    }

    public void setParty_id(int party_id) {
        this.party_id = party_id;
    }

}
