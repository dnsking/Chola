package com.app.chalo.networking.actions;

public class PutUrlAction extends NetworkAction {
    private String action = "PutUrl";
    private String Key;
    public PutUrlAction(){}
    public PutUrlAction(String Key){
        this.Key = Key;
    }
    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
