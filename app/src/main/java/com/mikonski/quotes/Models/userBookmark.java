package com.mikonski.quotes.Models;

public class userBookmark {
    String uid, post_id, post_type;

    public userBookmark() {
    }

    public userBookmark(String uid, String post_id, String post_type) {
        this.uid = uid;
        this.post_id = post_id;
        this.post_type = post_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
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
}
