package com.techelevator.tenmo.model;

public class UserLists {

    private String username;

    public UserLists() { }

    public UserLists(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "username='" + username +
                '}';
    }
}
