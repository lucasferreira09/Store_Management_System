package com.example.StoreManagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PagSeguroWebhookVerifier {

    private String token;

    public PagSeguroWebhookVerifier(@Value("{pagbank.token") String token) {
        this.token = token;
    }

    public boolean isValid(String payload, String authenticityToken) {

        if (authenticityToken == null || authenticityToken.isBlank())
            return false;

        String signature = token+"-"+authenticityToken;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    signature.getBytes(StandardCharsets.UTF_8));

            String generatedSignature = bytesToHex(encodedhash);

            return generatedSignature.equals(authenticityToken);


        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

