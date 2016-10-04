package com.questbase.app.weblogin;

import android.os.Parcel;
import android.os.Parcelable;

import com.questbase.app.flowui.screens.RespoScreen;

public class WebLoginScreen implements Parcelable, RespoScreen {
    private final String loginUrl;

    public String getLoginUrl() {
        return loginUrl;
    }

    public WebLoginScreen(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    protected WebLoginScreen(Parcel in) {
        loginUrl = in.readString();
    }

    public static final Creator<WebLoginScreen> CREATOR = new Creator<WebLoginScreen>() {
        @Override
        public WebLoginScreen createFromParcel(Parcel in) {
            return new WebLoginScreen(in.readString());
        }

        @Override
        public WebLoginScreen[] newArray(int size) {
            return new WebLoginScreen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(loginUrl);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        WebLoginScreen webLoginScreen = (WebLoginScreen) object;
        return loginUrl.equals(webLoginScreen.loginUrl);
    }

    @Override
    public int hashCode() {
        return loginUrl.hashCode();
    }
}
