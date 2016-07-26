package br.edu.ifspsaocarlos.sdm.fragchat.models;

import java.util.Date;

/**
 * Created by LeonardoAlmeida on 18/07/16.
 */

public class UserTokenModel {

    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_EXPIRED = 2;

    private long id = -1;
    private long userID;
    private String email;
    private String token;
    private Date requestDate;
    private Date releaseDate;
    private boolean status;

    public UserTokenModel(long userID, String email, String token){
        this.userID = userID;
        this.email = email;
        this.token = token;
        this.requestDate = new java.sql.Date(new Date().getTime());
        this.releaseDate = new java.sql.Date(new Date().getTime()+2000);
        this.status = true;
    }

    public UserTokenModel (long id, long userID, String token, long requestDate, long releaseDate, boolean status){
        this.id = id;
        this.userID = userID;
        this.token = token;
        this.requestDate = new java.sql.Date(requestDate);
        this.releaseDate = new java.sql.Date(releaseDate);
        this.status = status;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
