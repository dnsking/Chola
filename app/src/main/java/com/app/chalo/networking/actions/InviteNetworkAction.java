package com.app.chalo.networking.actions;

public class InviteNetworkAction extends NetworkAction{
    private String channelId;
    private String userName;
    private String action = "Invite";
    private String[] users;
    public InviteNetworkAction(String[] users,String userName,String channelId){
        this.users = users;
        this.userName = userName;
        this.channelId = channelId;
    }
    public InviteNetworkAction(){}
    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
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
}
