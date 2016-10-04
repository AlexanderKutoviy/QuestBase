package com.questbase.app.presentation;

/**
 * Class implementing this interface is a presenter providing reactions to logout events
 */
public interface LogoutPresenter {

    /**
     * Method is called when view generates logout attempt
     */
    void onLogout();
}
