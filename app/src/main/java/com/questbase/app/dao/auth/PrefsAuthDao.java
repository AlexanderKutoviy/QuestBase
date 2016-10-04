package com.questbase.app.dao.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Optional;
import com.questbase.app.utils.Auth;
import com.questbase.app.utils.Preconditions;

/**
 * This helper class contains methods that persist id/auth data.
 */
public class PrefsAuthDao implements AuthDao {

    private static final String AUTH_PREFERENCES_NAME = "AuthPrefs";
    private static final String TOKEN_NAME = "userToken";
    private static final String TOKEN_ID = "tokenId";
    private static final String USER_ID_NAME = "userId";

    private final SharedPreferences prefs;

    public PrefsAuthDao(Context ctx) {
        prefs = ctx.getSharedPreferences(AUTH_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isAuthorized() {
        return getAuth().isPresent();
    }

    @Override
    public synchronized void logout() {
        setValue(TOKEN_NAME, null);
        setValue(TOKEN_ID, null);
        setValue(USER_ID_NAME, null);
    }

    @Override
    public synchronized void setAuth(Auth auth) {
        setValue(TOKEN_NAME, auth.token);
        setValue(TOKEN_ID, auth.tokenId);
        setValue(USER_ID_NAME, auth.userId);
    }

    public synchronized Optional<Auth> getAuth() {
        String token = getValue(TOKEN_NAME);
        String tokenId = getValue(TOKEN_ID);
        String userId = getValue(USER_ID_NAME);
        if (token == null) {
            Preconditions.check(tokenId == null && userId == null);
            return Optional.empty();
        }
        return Optional.of(new Auth(userId, token, tokenId));
    }

    private String getValue(String key) {
        return prefs.getString(key, null);
    }

    private void setValue(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }
}
