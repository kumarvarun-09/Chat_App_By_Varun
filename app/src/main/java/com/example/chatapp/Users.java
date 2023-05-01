package com.example.chatapp;

import android.widget.EditText;

public class Users {
    String userName;
    String email;
    String userId;
    String password;
    String profilePic;
    String status;

    long timeStamp;

    public Users()
    {

    }

    public Users(String userId, String userName, String email, String password, String imageUriStr, String statusStr)
    {
        this.userName = userName;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.profilePic = imageUriStr;
        this.status = statusStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public String getLastMessage() {
//        return lastMessage;
//    }
//
//    public void setLastMessage(String lastMessage) {
//        this.lastMessage = lastMessage;
//    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
