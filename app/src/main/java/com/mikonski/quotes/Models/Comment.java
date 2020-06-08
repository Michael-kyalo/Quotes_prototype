package com.mikonski.quotes.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment {
    private  String comment_id, comment_body,uid,post_id;
    @ServerTimestamp
    Date date;

    public Comment() {
    }


    public Comment(String comment_id, String comment_body, String uid, String post_id, Date date) {
        this.comment_id = comment_id;
        this.comment_body = comment_body;
        this.uid = uid;
        this.post_id = post_id;
        this.date = date;

    }

    public String getComment_id() {
        return comment_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
