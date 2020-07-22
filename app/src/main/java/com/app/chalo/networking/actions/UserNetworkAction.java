package com.app.chalo.networking.actions;

public class UserNetworkAction  extends NetworkAction {
    protected String action = "AddUser";
    private String phoneNumber;
    private String userName;
    private String contributionId;
    private String type;
    private String lat;
    private String lon;
    private long timeExpiration;
    private String description;
    private String plates;
    private String color;
    private boolean match;
    private boolean viewed;
    private boolean isAccepted;

    public UserNetworkAction(
            String userName, String contributionId, String type, String lat, String lon, long timeExpiration
             , String description, String plates, String color, boolean match, boolean viewed, boolean isAccepted
    ){

        this.userName=userName;
                this.contributionId=contributionId;
        this.type=type;
        this.lat=lat;
        this.lon=lon;
        this.timeExpiration=timeExpiration;
        this.description=description;
        this. plates=plates;
        this. color=color;
        this. match=match;
        this.viewed=viewed;
        this.isAccepted=isAccepted;
    }
    public UserNetworkAction(

             String contributionId,
             String phoneNumber,
             String type,
             String lat,
             String lon,
             long timeExpiration,
             String description,
             String plates,
             String color,
             String userName
    ){

       this.contributionId=contributionId;
        this.phoneNumber=phoneNumber;
        this. type=type;
        this. lat=lat;
        this. lon=lon;
        this.timeExpiration=timeExpiration;
        this.description=description;
        this.plates=plates;
        this. color=color;
        this. userName=userName;
    }
    public UserNetworkAction(){}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public long getTimeExpiration() {
        return timeExpiration;
    }

    public void setTimeExpiration(long timeExpiration) {
        this.timeExpiration = timeExpiration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlates() {
        return plates;
    }

    public void setPlates(String plates) {
        this.plates = plates;
    }
    public String getContributionId() {
        return contributionId;
    }

    public void setContributionId(String contributionId) {
        this.contributionId = contributionId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
