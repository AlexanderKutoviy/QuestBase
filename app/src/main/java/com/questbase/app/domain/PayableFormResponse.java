package com.questbase.app.domain;

public class PayableFormResponse {

    public int id;
    public int form;
    public int senderSessionId;
    public Approval approval;
    public String state;

    @Override
    public String toString(){
        return " id=> " + id +
                "\n dbForm=> " + form +
                "\n senderSessionId=> " + senderSessionId +
                "\n approval: " + approval;
    }
}
