package com.jalon.jackson5;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

public class User {
    public User(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public User() {
    }

    private String username;

    private String nickname;

}
