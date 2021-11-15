package com.davor.security.DavorSecurity;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class PasswordEncoderTest {

    private static final String PASSWORD = "davor1989";

    @Test
    void testWithMD5Encryption() {
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(PASSWORD.getBytes(StandardCharsets.UTF_8));
        System.out.println(md5DigestAsHex);
    }

    @Test
    void testWithNoOpEncryption() {
        PasswordEncoder noOpEncoder = NoOpPasswordEncoder.getInstance();
        System.out.println(noOpEncoder.encode(PASSWORD));
    }

    @Test
    void testLdapEncryption() {
        PasswordEncoder ldapEncoder = new LdapShaPasswordEncoder();
        System.out.println(ldapEncoder.encode(PASSWORD));
    }

    @Test
    void testSHA256Encryption() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));
    }
}
