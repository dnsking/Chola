package com.app.chalo.networking.actions;

import com.google.android.material.textfield.TextInputLayout;

public class AddContentNetworkAction extends  NetworkAction {
    private String channelId;
    private String title;
    private String pubId;
    private String pubTime;
    private String userName;
    private String action = "AddContent";
    public AddContentNetworkAction() {
    }
    public AddContentNetworkAction(String channelId,String title
    ,String pubId,String pubTime,String userName){
        this.channelId = channelId;
        this.title = title;
        this.pubId = pubId;
        this.pubTime = pubTime;
        this.userName = userName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
