package com.app.chalo.networking.actions;

public class MatchCodeNetworkAction extends NetworkAction {
    protected String action = "MatchCode";
    private String phoneNumber;
    private String userName;
    private String code;
    public MatchCodeNetworkAction(){}
    public MatchCodeNetworkAction(String phoneNumber, String userName, String code){
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.code = code;
    }
    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
