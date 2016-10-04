package com.questbase.app.profile;

import android.graphics.Bitmap;

import java.util.List;

public class ProfileData {
    public Bitmap avatarBitmap;
    public String avatarUrl;
    public String phone;
    public String name;
    public double balance;
    public double reservedBalance;
    public int karma;
    public int coins;
    public int energy;
    public List<PayableTestSession> payableTestSessions;
}
