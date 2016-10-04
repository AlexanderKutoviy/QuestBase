package com.questbase.app.form.bulkradio;

import com.google.gson.JsonObject;

public class BulkRadioItem {
    private static final String TEXT = "text";
    private JsonObject answerJson;
    private Boolean checked = false;
    private BulkRadioListAdapter.Response choice;

    public BulkRadioItem(JsonObject answerJson) {
        this.answerJson = answerJson;
    }

    public JsonObject getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(JsonObject answerJson) {
        this.answerJson = answerJson;
    }

    public Boolean isCheck() {
        return checked;
    }

    public void setCheck(Boolean checked) {
        this.checked = checked;
    }

    public String getAnswerText() {
        return answerJson.get(TEXT).getAsString();
    }

    public BulkRadioListAdapter.Response getChoice() {
        return choice;
    }

    public void setChoice(BulkRadioListAdapter.Response choice) {
        this.choice = choice;
    }
}
