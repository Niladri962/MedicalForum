package com.example.smedicalapp;
import com.google.firebase.Timestamp;
public class Medical {

    private String title;
    private String Info;
    private String imageUrl;

    private String userId;
    private Timestamp timeAdded;
    private String userName;

    // When using firebase, always create empty constructor
    public Medical() {
    }

    public Medical(String title, String Info, String imageUrl, String userId, Timestamp timeAdded, String userName) {
        this.title = title;
        this.Info = Info;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String Info) {
        this.Info = Info;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
