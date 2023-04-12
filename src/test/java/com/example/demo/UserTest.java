package com.example.demo;

import com.example.demo.app.domain.model.entity.User;
import com.example.demo.app.domain.model.util.HashUtil;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testPasswordHashing() {
        String password = "mypassword";
        User userinfo = new User();
        userinfo.setPassword(password);

        String hashedPassword = HashUtil.sha256(password);

        System.out.println("Original Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);
        System.out.println("Is hashed? " + hashedPassword.equals(userinfo.getPassword())); // should print true
    }
}
