package com.example.ideation;

public class userGroups {
    private int userGroupID,userID_fk,groupID_fk;
    private String name, description,email;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getUserGroupID() {
        return userGroupID;
    }

    public void setUserGroupID(int userGroupID) {
        this.userGroupID = userGroupID;
    }

    public int getUserID_fk() {
        return userID_fk;
    }

    public void setUserID_fk(int userID_fk) {
        this.userID_fk = userID_fk;
    }

    public int getGroupID_fk() {
        return groupID_fk;
    }

    public void setGroupID_fk(int groupID_fk) {
        this.groupID_fk = groupID_fk;
    }
}
