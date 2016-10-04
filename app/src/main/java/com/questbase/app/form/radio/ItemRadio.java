package com.questbase.app.form.radio;

import com.annimon.stream.Optional;
import com.google.gson.JsonObject;

class ItemRadio {
    private JsonObject answerJson;
    private Optional<String> textAnswer;
    private Boolean checked = false;

    public ItemRadio(JsonObject answerJson, String answer) {
        this.answerJson = answerJson;
        this.textAnswer = Optional.of(answer);
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

    public Optional<String> getAnswer() {
        return this.textAnswer;
    }

    public void setAnswer(String answer) {
        this.textAnswer = Optional.of(answer);
    }
}
