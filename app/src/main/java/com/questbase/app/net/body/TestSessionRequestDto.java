package com.questbase.app.net.body;

import java.util.List;

public class TestSessionRequestDto {
    List<TestSessionRequest> sessions;

    public TestSessionRequestDto(List<TestSessionRequest> sessions) {
        this.sessions = sessions;
    }

    static public class TestSessionRequest {
        long localId;
        long formId;
        long formVersion;
        String state;

        public TestSessionRequest(long localId, long formId, long formVersion, String state) {
            this.localId = localId;
            this.formId = formId;
            this.formVersion = formVersion;
            this.state = state;
        }
    }
}
