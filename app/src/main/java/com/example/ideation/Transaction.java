package com.example.ideation;

public class Transaction {
    private int transacationID,userID_fk,rewardsID_fk;
    private String name;
    private Double price;


    public Transaction(int userID_fk, int rewardsID_fk) {
        this.userID_fk = userID_fk;
        this.rewardsID_fk = rewardsID_fk;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public int getTransacationID() {
        return transacationID;
    }

    public int getUserID_fk() {
        return userID_fk;
    }

    public int getRewardsID_fk() {
        return rewardsID_fk;
    }
}
