package com.app.chalo.networking.actions;

public class GetPasswordNetworkAction extends NetworkAction {




    protected String action = "GetPassword";
    private String phoneNumber;
    private String userName;
    private String code;
    public GetPasswordNetworkAction(){}
    public GetPasswordNetworkAction(String phoneNumber, String userName, String code){
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.code = code;
    }
    public GetPasswordNetworkAction(MatchCodeNetworkAction matchCodeNetworkAction) {
        this.phoneNumber = matchCodeNetworkAction.getPhoneNumber();
        this.userName = matchCodeNetworkAction.getPhoneNumber();
        this.code = matchCodeNetworkAction.getCode();
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
