package com.example.almagestor.Validation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    public String EncryptString(String data,String password) throws Exception {
        SecretKeySpec secretKey = generateKey(password);
        Cipher cipher= Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] dataEncrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(dataEncrypted);
    }
    public String DesEncryptString(String data, String password) throws Exception{
        SecretKeySpec secretKey = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte [] dataDescodified=Base64.getDecoder().decode(data);
        byte [] dataDescrypted=cipher.doFinal(dataDescodified);
        return new String(dataDescrypted);
    }
    private SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        key=sha.digest(key);
        return new SecretKeySpec(key,"AES");
    }
}
