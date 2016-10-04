package com.questbase.app.net.objects;

public class PhoneAddingResponse {
    public String success;
    public String error;

    @Override
    public String toString() {
        String str = "";
        if (success != null) {
            str = success;
        } else if (error != null) {
            str = error;
        }
        return str;
    }
}
