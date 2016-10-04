package com.questbase.app.form.checkbox;

import com.annimon.stream.Optional;
import com.google.gson.JsonObject;

class ItemCheck {
    private Optional<String> textAnswer;
    private JsonObject answerJson;
    private Boolean checked = false;

    ItemCheck(JsonObject answerJson, String answer) {
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

    public String getAnswer() {
        return textAnswer.get();
    }

    public Optional<String> getAnswer(String answer) {
        if (this.textAnswer.get().equals(answer)) {
            return this.textAnswer;
        }
        return Optional.empty();
    }

    public void setAnswer(String answer) {
        this.textAnswer = Optional.of(answer);
    }

    @Override
    public String toString() {
        return textAnswer.get() + answerJson;
    }
}
