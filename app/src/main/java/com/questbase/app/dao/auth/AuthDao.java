package com.questbase.app.dao.auth;

import com.annimon.stream.Optional;
import com.questbase.app.utils.Auth;

public interface AuthDao {
    boolean isAuthorized();

    void logout();

    void setAuth(Auth auth);

    Optional<Auth> getAuth();
}
