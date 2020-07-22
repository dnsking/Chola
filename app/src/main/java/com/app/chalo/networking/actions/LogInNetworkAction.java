package com.app.chalo.networking.actions;

public class LogInNetworkAction  extends NetworkAction {
    private String action = "Login";
    private String phoneNumber;
    private String userName;
    private String code;
    private String password;
    public LogInNetworkAction(){}
    public LogInNetworkAction(String phoneNumber, String password){
        this.phoneNumber = phoneNumber;
        this.userName = phoneNumber;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
