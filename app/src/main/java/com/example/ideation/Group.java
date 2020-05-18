package com.example.ideation;
import com.google.gson.annotations.SerializedName;


public class Group {





    @SerializedName("Body")
    private int groupId;
    private String name,email,description,type;
    private int userID_creator;



    public Group(String name, String description, String type,String email) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.email = email;


    }

    public String getEmail() {
        return email;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getUserID_creator() {
        return userID_creator;
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
