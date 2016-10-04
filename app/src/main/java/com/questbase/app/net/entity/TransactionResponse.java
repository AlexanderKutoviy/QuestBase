package com.questbase.app.net.entity;

public class TransactionResponse {

    public int id;
    public int account;
    public int service;
    public double amount;
    public String type;
    //---------------------for new profile
    public String state;
    public String sum;
    public String status;
    public long date;

    @Override
    public String toString() {
        return "id: " + id + "; account: " + account + "; service: " + service + " ;  amount: " + amount + " ; state: " + state;
    }

    public int getId() {
        return id;
    }

    public int getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public String getState() {
        return state;
    }
}