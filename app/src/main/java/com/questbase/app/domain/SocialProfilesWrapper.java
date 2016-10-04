package com.questbase.app.domain;

public class SocialProfilesWrapper {

    public long facebook;
    public long vk;
    //exception in convention
    public String google_oauth;
    @Override
    public String toString() {
        return "facebook=> " + facebook + "VK=> " + vk + "GOOGLE=> " + google_oauth;
    }
}