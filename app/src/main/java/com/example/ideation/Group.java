package com.example.ideation;
import com.google.gson.annotations.SerializedName;


public class Group {





    @SerializedName("Body")
    private int groupId;
    private String name;
    private String description;
    private String type;


    public Group(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
