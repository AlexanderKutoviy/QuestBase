package com.questbase.app.net.objects;

import java.util.List;

public class TestSessionResponseDto {
    List<TestSessionResponse> results;

    public List<TestSessionResponse> getResults() {
        return results;
    }

    public class TestSessionResponse {
        int localId;
        long id;
        String state;

        public int getLocalId() {
            return localId;
        }

        public long getId() {
            return id;
        }

        public String getState() {
            return state;
        }

        @Override
        public String toString() {
            return "TestSessionResponse{" +
                    "localId=" + localId +
                    ", id=" + id +
                    ", state=" + state +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TestSessionResponse{" +
                "results=" + results +
                '}';
    }
}

