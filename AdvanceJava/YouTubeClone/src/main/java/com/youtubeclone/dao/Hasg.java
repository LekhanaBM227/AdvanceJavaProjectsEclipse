package com.youtubeclone.dao;
import org.apache.commons.codec.digest.DigestUtils;

public class Hasg {
    public static void main(String[] args) {
        String password = "mypassword";
        String hashed = DigestUtils.sha256Hex(password);
        System.out.println(hashed);
    }
}

