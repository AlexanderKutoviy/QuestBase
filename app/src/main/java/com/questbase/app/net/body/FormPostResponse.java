package com.questbase.app.net.body;

public class FormPostResponse {

    public String userId;
    public int formId;
    public int id;
    public String state;
    public int finished;

    public FormPostResponse(String userId,
                            int formId,
                            int testSessionId,
                            String sessionState,
                            int finished) {
        this.userId = userId;
        this.formId = formId;
        this.id = testSessionId;
        this.state = sessionState;
        this.finished = finished;
    }
}
