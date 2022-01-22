package com.example.mrizudev.democracyinfo.services;

import com.example.mrizudev.democracyinfo.model.User;
import com.example.mrizudev.democracyinfo.repositories.UserRepository;
import com.example.mrizudev.democracyinfo.handlers.JwtHandler;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Formatter;
import java.util.List;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class UserService {
    public static final int USER_COLUMN_ID = 0;
    public static final int USER_COLUMN_JWT_TOKEN = 3;
    public static final int USER_COLUMN_PARTY_ID = 5;
    public static final int PARTY_COLUMN_NAME = 1;


    @PersistenceContext
    EntityManager entityManager;
    UserRepository userRepository;
//    PartyRepository partyRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    public User getUser(int id){
        User user = userRepository.findById(id);
        return user;
    }

    public boolean login(String login, String password) {
        String queryBody = "SELECT * FROM user WHERE username = :username AND HMAC512_HASH = :hash";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", login);
        try {
            query.setParameter("hash", JwtHandler.calculateHMAC(password));
            return query.getResultList().size() == 1;
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public void register(String login, String password, String email, String party_name) {
        int party_id = getPartyIdByName(party_name);
        assert (party_id == -1);
        String queryBody = "INSERT INTO user (username, HMAC512_HASH, email, party_id) VALUES (:username, :hash, :email, :party_id)";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", login);
        query.setParameter("email", email);
        query.setParameter("party_id", party_id);
        try {
            query.setParameter("hash", JwtHandler.calculateHMAC(password));
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        query.executeUpdate();
    }

    private int getIdByName(String login) {
        String queryBody = "SELECT * FROM user WHERE username = :username";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", login);
        List<Object[]> res = query.getResultList();
        if (res.size() == 1) {
            return (int) res.get(0)[UserService.USER_COLUMN_ID];
        } else {
            return -1;
        }
    }
    private int getPartyIdByName(String party_name) {
        String queryBody = "SELECT * FROM party WHERE party.name = :party_name";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("party_name", party_name);
        List<Object[]> res = query.getResultList();
        System.out.println(res.size());
        if (res.size() == 1) {
            System.out.println(res.get(0)[0].toString());
            return (int) res.get(0)[0];
        } else {
            return -1;
        }
    }

    private String getPartyByName(String login) {
       String queryBody = "SELECT party.id as 'party_id2', name, color, tag FROM party INNER JOIN user ON user.party_id = party.id WHERE user.username = :username";
       Query query = entityManager.createNativeQuery(queryBody);
       query.setParameter("username", login);
       List<Object[]> res = query.getResultList();
       if (res.size() == 1) {
           // int partyId = (int) res.get(0)[UserService.USER_COLUMN_PARTY_ID];
           return res.get(0)[1].toString();
       }
       return "No party";
    }


    private String getPartyShortcutByUsername(String login) {
        String queryBody = "SELECT party.id as 'party_id2', name, color, tag FROM party INNER JOIN user ON user.party_id = party.id WHERE user.username = :username";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", login);
        List<Object[]> res = query.getResultList();
        if (res.size() == 1) {
            // int partyId = (int) res.get(0)[UserService.USER_COLUMN_PARTY_ID];
            return res.get(0)[3].toString();
        }
        return "#000000";
    }



    public String authenticate(String login, String password) {
        boolean isLogged = this.login(login, password);
        if (isLogged) {
            String currentParty = this.getPartyByName(login);
            String bearer = JwtHandler.generateToken(currentParty, login); // zamiast aaa to tutaj "GetParty(login)" i zrobic funkcje getParty zeby zwracala "None" jesli typ nie ma Party
            return bearer;
        } else {
            return "error";
        }
    }

}
