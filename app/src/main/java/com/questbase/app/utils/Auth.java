package com.questbase.app.utils;

public class Auth {

    public String userId;
    public String token;
    public String tokenId;

    public Auth(String userId, String token, String tokenId) {
        this.userId = userId;
        this.token = token;
        this.tokenId = tokenId;
    }
}
