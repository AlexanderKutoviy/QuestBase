package com.questbase.app.form;

import com.google.gson.JsonElement;

/**
 * Receives response events
 */
public interface ResponseListener {

    void onResponse(JsonElement response);
}
