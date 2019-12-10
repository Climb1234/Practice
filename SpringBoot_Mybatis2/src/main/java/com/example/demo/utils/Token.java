package com.example.demo.utils;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * @Description Toten令牌验证
 * @Author Climb
 * @Date 2019/11/18
 **/
public class Token {
    /**
     * 加密
     * @param username
     * @param password
     * @return
     */
    public static String createJWT(String username, String password) {
        String id = Constants.ID;
        long ttlMillis = Constants.TTL_MILLIS;

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("4234dfasdfqdd");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setSubject(password)
                .setIssuer(username)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * 解密
     * @param jwt
     */
    public static Claims parseJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("4234dfasdfqdd"))
                .parseClaimsJws(jwt).getBody();
        /*System.out.println("ID: " + claims.getId());
        System.out.println("Name: " + claims.getSubject());
        System.out.println("Password: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());*/
        return claims;
    }

    /*public static void main(String[] args) {

        Token t = new Token();
        //加密
        String jwt = t.createJWT("3133", "13dsdda", "afrefsa", 123456);
        System.out.println(jwt);

        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMTMzIiwiaWF0IjoxNTc0MTI3MDIwLCJzdWIiOiJhZnJlZnNhIiwiaXNzIjoiMTNkc2RkYSIsImV4cCI6MTU3NDEyNzE0M30.L85p6108hqxG9k0WobUf6Q6gZA-r7gaCzSNaoB6lHdw";
        //解密
        t.parseJWT(jwt);
    }*/
}
