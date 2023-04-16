package com.example.demo;

import com.example.demo.app.domain.model.entity.UserEntity;
import com.example.demo.app.domain.model.util.HashUtil;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testPasswordHashing() {
        String password = "mypassword";
        UserEntity userinfo = new UserEntity();
        userinfo.setPassword(password);
        String hashedPassword = HashUtil.sha256(password);

        System.out.println("Original Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);
        System.out.println("Is hashed? " + hashedPassword.equals(userinfo.getPassword())); // should print true
    }
}
