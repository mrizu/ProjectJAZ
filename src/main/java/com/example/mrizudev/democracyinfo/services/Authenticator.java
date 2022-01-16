package com.example.mrizudev.democracyinfo.services;

import com.example.mrizudev.democracyinfo.model.User;
import com.example.mrizudev.democracyinfo.model.Party;
import com.example.mrizudev.democracyinfo.repositories.UserRepository;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Date;
import java.util.Formatter;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class Authenticator {
    public static final int USER_COLUMN_ID = 0;
    public static final int USER_COLUMN_JWT_TOKEN = 3;
    public static final int USER_COLUMN_PARTY_ID = 5;
    public static final int PARTY_COLUMN_NAME = 1;


    @PersistenceContext
    EntityManager entityManager;

    private final String secret = "7333637233745f6b33795f6b3472306c";
    UserRepository userRepository;
//    PartyRepository partyRepository;

    @Autowired
    public Authenticator(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    public User getUser(int id){
        User user = userRepository.findById(id);
        return user;
    }


    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String calculateHMAC(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }


    public boolean login(String login, String password) {
        String queryBody = "SELECT * FROM user WHERE username = :username AND HMAC512_HASH = :hash";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", login);
        try {
            query.setParameter("hash", calculateHMAC(password, this.secret));
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

    private void assignToken(String login) {
        JSONObject jwtBody = new JSONObject();
        jwtBody.append("username", login);
        jwtBody.append("id", this.getIdByName(login));


    }

    private String generateToken(String subject, String auth) {
            Date exp = new Date(System.currentTimeMillis() + (1000 * 120));

            String token = Jwts.builder()
                .setSubject(subject)
                .claim("auth", auth)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
            return token;
        }


    private int getIdByName(String login) {
        String queryBody = "SELECT * FROM user WHERE username = :username";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", login);
        List<Object[]> res = query.getResultList();
        if (res.size() == 1) {
            System.out.println(res.get(0)[0]);
            return (int) res.get(0)[Authenticator.USER_COLUMN_ID];
        } else {
            return -1;
        }
    }

//    private String getPartyByName(String login) {
//        String queryBody = "SELECT * FROM user WHERE username = :username";
//        Query query = entityManager.createNativeQuery(queryBody);
//        query.setParameter("username", login);
//        List<Object[]> res = query.getResultList();
//        if (res.size() == 1) {
//            int partyId = (int) res.get(0)[Authenticator.USER_COLUMN_PARTY_ID];
//            //Party party = this.partyRepository.getById(partyId);
//            //return party.getName();
//            queryBody = "SELECT * FROM party WHERE id = :id";
//            query = entityManager.createNativeQuery(queryBody);
//            query.setParameter("id", partyId);
//            res = query.getResultList();
//            if (res.size() == 1) {
//                return (String) res.get(0)[Authenticator.PARTY_COLUMN_NAME];
//            } else {
//                return "No party";
//            }
//        } else {
//            return "";
//        }
//    }

    public String authenticate(String login, String password) {
        boolean isLogged = this.login(login, password);
        if (this.login(login, password)) {
            return "Logged in!";
        } else {
            return "Wrong username or password";
        }
    }

}
