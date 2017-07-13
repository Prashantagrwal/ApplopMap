package com.example.dell.applopmap.Model;



public class GetClass
{
String pace_id,user_review;

    public GetClass(String pace_id, String user_review) {
        this.pace_id = pace_id;
        this.user_review = user_review;
    }

    public GetClass() {
    }

    public String getPace_id() {
        return pace_id;
    }

    public void setPace_id(String pace_id) {
        this.pace_id = pace_id;
    }

    public String getUser_review() {
        return user_review;
    }

    public void setUser_review(String user_review) {
        this.user_review = user_review;
    }
}
