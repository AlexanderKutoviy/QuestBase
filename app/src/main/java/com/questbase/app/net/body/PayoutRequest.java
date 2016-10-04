package com.questbase.app.net.body;

public class PayoutRequest {

    public String phone;
    public String target;
    public String payoutAmount;

    public PayoutRequest(String phone, String target, String payoutAmount) {
        this.phone = phone;
        this.target = target;
        this.payoutAmount = payoutAmount;
    }
}