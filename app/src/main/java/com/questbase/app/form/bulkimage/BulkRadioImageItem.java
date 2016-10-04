package com.questbase.app.form.bulkimage;

import com.google.gson.JsonObject;

class BulkRadioImageItem {
    private static final String TEXT = "text";
    private BulkRadioImageAdapter.Response choice;
    private String imageName;
    private JsonObject answerJson;

    public BulkRadioImageItem(JsonObject answerJson, String imageName) {
        this.answerJson = answerJson;
        this.imageName = imageName;
    }

    public String getAnswerText() {
        return answerJson.get(TEXT).getAsString();
    }

    public JsonObject getAnswerJson() {
        return answerJson;
    }

    BulkRadioImageAdapter.Response getChoice() {
        return choice;
    }

    void setChoice(BulkRadioImageAdapter.Response choice) {
        this.choice = choice;
    }

    String getImageName() {
        return imageName;
    }
}
