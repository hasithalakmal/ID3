package com.slit.id3.bean;

public class Recode {

    private String duration;
    private String directorFacebookLikes;
    private String actorOneName;
    private String actorTwoName;
    private String actorThreeName;
    private String language;
    private String country;

    // TODO: 8/8/17  - comments



    private boolean isProfitable;

    public Recode() {
    }

    //temp construct
    public Recode(String directorFacebookLikes, String actorOneName, String language,
                  String country, boolean isProfitable) {
        this.directorFacebookLikes = directorFacebookLikes;
        this.actorOneName = actorOneName;
        this.language = language;
        this.country = country;
        this.isProfitable = isProfitable;
    }

    public Recode(String duration, String directorFacebookLikes, String actorOneName, String actorTwoName,
                  String actorThreeName, String language, String country) {
        this.duration = duration;
        this.directorFacebookLikes = directorFacebookLikes;
        this.actorOneName = actorOneName;
        this.actorTwoName = actorTwoName;
        this.actorThreeName = actorThreeName;
        this.language = language;
        this.country = country;
    }

    public Recode(String duration, String directorFacebookLikes, String actorOneName, String actorTwoName,
                  String actorThreeName, String language, String country, boolean isProfitable) {
        this.duration = duration;
        this.directorFacebookLikes = directorFacebookLikes;
        this.actorOneName = actorOneName;
        this.actorTwoName = actorTwoName;
        this.actorThreeName = actorThreeName;
        this.language = language;
        this.country = country;
        this.isProfitable = isProfitable;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirectorFacebookLikes() {
        return directorFacebookLikes;
    }

    public void setDirectorFacebookLikes(String directorFacebookLikes) {
        this.directorFacebookLikes = directorFacebookLikes;
    }

    public String getActorOneName() {
        return actorOneName;
    }

    public void setActorOneName(String actorOneName) {
        this.actorOneName = actorOneName;
    }

    public String getActorTwoName() {
        return actorTwoName;
    }

    public void setActorTwoName(String actorTwoName) {
        this.actorTwoName = actorTwoName;
    }

    public String getActorThreeName() {
        return actorThreeName;
    }

    public void setActorThreeName(String actorThreeName) {
        this.actorThreeName = actorThreeName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isProfitable() {
        return isProfitable;
    }

    public void setProfitable(boolean profitable) {
        isProfitable = profitable;
    }
}
