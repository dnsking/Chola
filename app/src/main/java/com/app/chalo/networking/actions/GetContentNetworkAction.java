package com.app.chalo.networking.actions;

public class GetContentNetworkAction extends NetworkAction {
    private String action ="GetContent";
    private String channelId;
    public GetContentNetworkAction(){}
    public GetContentNetworkAction(String channelId){
        this.channelId = channelId;
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
}
