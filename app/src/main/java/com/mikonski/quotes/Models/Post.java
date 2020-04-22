package com.mikonski.quotes.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {
    String post_id;
    String post_type;
    String post_body;
    String uid;


    public Post() {
    }

    public Post(String post_id, String post_type, String post_body, String uid) {
        this.post_id = post_id;
        this.post_type = post_type;
        this.post_body = post_body;
        this.uid = uid;

    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getPost_body() {
        return post_body;
    }

    public void setPost_body(String post_body) {
        this.post_body = post_body;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
