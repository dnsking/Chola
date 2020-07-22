package com.app.chalo.networking.actions;

public class CreateChannelNetworkAction extends  NetworkAction{
    private String channelId;
    private String userName;
    private String about;
    private String dateCreated;
    private String password;
    private String action ="CreateChannel";
    public CreateChannelNetworkAction() {
    }

    public CreateChannelNetworkAction(String channelId, String userName, String about,String dateCreated,String password) {
        this.channelId = channelId;
        this.userName = userName;
        this.about = about;
        this.dateCreated = dateCreated;
        this.password = password;
    }
    public CreateChannelNetworkAction(String userName,String password,String action) {
        this.userName = userName;
        this.action = action;
        this.password = password;
    }
    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
