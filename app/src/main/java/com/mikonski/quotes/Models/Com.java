package com.mikonski.quotes.Models;

public class Com {
    private  String comment_id;
    private String comment_body;
    private String uid;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    private String post_id;

    public Com() {
    }

    public Com(String comment_id, String comment_body, String uid, String post_id) {
        this.comment_id = comment_id;
        this.comment_body = comment_body;
        this.uid = uid;
        this.post_id = post_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
