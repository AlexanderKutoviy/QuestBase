package com.questbase.app.net.body;

public class ResourceRequest {
    long formId;
    String path;
    String crc;

    public ResourceRequest(long formId, String path, String crc) {
        this.formId = formId;
        this.path = path;
        this.crc = crc;
    }
}