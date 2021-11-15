package com.davor.security.DavorSecurity;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
}