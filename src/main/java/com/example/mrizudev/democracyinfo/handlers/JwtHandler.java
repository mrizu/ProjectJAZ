package com.example.mrizudev.democracyinfo.handlers;

import io.jsonwebtoken.*;
import org.w3c.dom.ls.LSOutput;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Date;
import java.util.*;


public class JwtHandler {
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
        String secret = System.getenv("JAZProjectSecret");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }

    public static String generateToken(String subject, String auth) {
        String secret = System.getenv("JAZProjectSecret");
        System.out.println(secret);
        Date exp = new Date(System.currentTimeMillis() + (1000 * 900));
        return Jwts.builder()
                .setSubject(subject)
                .claim("auth", auth)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static boolean verifyToken(HttpServletRequest request) {
        try {
            String token = JwtHandler.getJwtToken(request);
        if (Objects.equals(token, "None")) return false;
            String secret = System.getenv("JAZProjectSecret");
            Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
            return !body.getExpiration().before(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsername(String token) {
        try {
            if (Objects.equals(token, "None")) return null;
            String secret = System.getenv("JAZProjectSecret");
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

    public static String getPartyColorByUsername(EntityManager entityManager, String nick) {
        String queryBody = "SELECT party.id as 'party_id2', name, color, tag FROM party INNER JOIN user ON user.party_id = party.id WHERE user.username = :username";
        Query query = entityManager.createNativeQuery(queryBody);
        query.setParameter("username", nick);
        List<Object[]> res = query.getResultList();
        if (res.size() == 1) {
            return res.get(0)[2].toString();
        }
        return "#000000";
    }
}
