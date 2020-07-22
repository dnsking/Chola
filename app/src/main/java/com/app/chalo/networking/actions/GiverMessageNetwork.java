package com.app.chalo.networking.actions;

public class GiverMessageNetwork  extends NetworkAction {
    protected String action = "SendMessage";
    private String from;
    private String time;
    private String to;
    private String message;
    private String type;
    private String accept;
    public GiverMessageNetwork(){}
    public GiverMessageNetwork(String from, String time, String to, String message, String type){
        this. from=from;
        this. time=time;
        this. to=to;
        this. message=message;
        this. type=type;
    }
    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
