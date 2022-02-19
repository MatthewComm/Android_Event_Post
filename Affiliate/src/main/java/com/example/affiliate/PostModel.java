package com.example.affiliate;

public class PostModel {
    String fUserID;
    String fDatePosted;
    String fPost;

    public PostModel(){

    }

    public PostModel(String adminName, String datePosted, String post) {
        this.fUserID = adminName;
        this.fDatePosted = datePosted;
        this.fPost = post;
    }

    public String getAdminName() {
        return fUserID;
    }

    public String getDatePosted() {
        return fDatePosted;
    }

    public String getPost() {
        return fPost;
    }

    public void setAdminName(String adminName) {
        fUserID = adminName;
    }

    public void setDatePosted(String datePosted) {
        fDatePosted = datePosted;
    }

    public void setPost(String post) {
        fPost = post;
    }
}
