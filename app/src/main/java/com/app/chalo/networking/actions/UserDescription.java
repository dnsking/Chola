package com.app.chalo.networking.actions;

public class UserDescription {
    private long lat;
    private long lon;
    private long timeExpiration;
    private String description;
    private String plates;
    private String[] imgs;

    public UserDescription(){}

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
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

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }
}
