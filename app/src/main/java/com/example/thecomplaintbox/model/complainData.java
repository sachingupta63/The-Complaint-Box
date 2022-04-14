package com.example.thecomplaintbox.model;

public class complainData {
    private String email;
    private String data;

    public complainData(String email, String data) {
        this.email = email;
        this.data = data;
    }

    public  complainData(){

    }


    public String getemail() {
        return email;
    }

    public void setemail(String uid) {
        this.email = uid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
