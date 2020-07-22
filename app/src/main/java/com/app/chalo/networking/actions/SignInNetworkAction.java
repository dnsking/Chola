package com.app.chalo.networking.actions;

public class SignInNetworkAction extends NetworkAction {
    private String action="SignIn";
    private String phoneNumber;

    public SignInNetworkAction(){}
    public SignInNetworkAction(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }
}
