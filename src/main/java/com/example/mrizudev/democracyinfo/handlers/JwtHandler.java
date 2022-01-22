package com.example.mrizudev.democracyinfo.handlers;

import io.jsonwebtoken.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Date;
import java.util.*;

public class JwtHandler {

    @PersistenceContext
    EntityManager entityManager;

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String calculateHMAC(String data)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        String secret = "7333637233745f6b33795f6b3472306c";
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    public static String generateToken(String subject, String auth) {
        String secret = "7333637233745f6b33795f6b3472306c";
        Date exp = new Date(System.currentTimeMillis() + (1000 * 900));
        String token = Jwts.builder()
                .setSubject(subject)
                .claim("auth", auth)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    public static boolean verifyToken(HttpServletRequest request) {
        try {
            String token = JwtHandler.getJwtToken(request);
        if (Objects.equals(token, "None")) return false;
            String secret = "7333637233745f6b33795f6b3472306c";
            Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
            return !body.getExpiration().before(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getParty(String token) {
        try {
            if (Objects.equals(token, "None")) return null;
            String secret = "7333637233745f6b33795f6b3472306c";
            Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
            if (body.getExpiration().before(new Date(System.currentTimeMillis()))) return null;
            return body.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUsername(String token) {
        try {
            if (Objects.equals(token, "None")) return null;
            String secret = "7333637233745f6b33795f6b3472306c";
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            if (body.getExpiration().before(new Date(System.currentTimeMillis()))) return null;
            return body.get("auth").toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getJwtToken(HttpServletRequest request) {
        Cookie c = JwtHandler.getCookie(request, "Bearer");
        return c == null ? "None" : c.getValue();
    }

    public static Cookie getCookie(HttpServletRequest request, String cookie) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(cookie)) {
                return c;
            }
        }
        return null;
    }

    public String getPartyColorByUsername(String nick) {
        String queryBody = "SELECT party.id as 'party_id2', name, color, tag FROM party INNER JOIN user ON user.party_id = party.id WHERE user.username = :username";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", nick);
        List<Object[]> res = query.getResultList();
        if (res.size() == 1) {
            // int partyId = (int) res.get(0)[UserService.USER_COLUMN_PARTY_ID];
            return res.get(0)[2].toString();
        }
        return "#000000";
    }
}
