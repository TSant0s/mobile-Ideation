package com.example.ideation;

public class Reward {
    private int rewardID;
    private String name;
    private Double price;

    public Reward(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public int getRewardID() {
        return rewardID;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}
