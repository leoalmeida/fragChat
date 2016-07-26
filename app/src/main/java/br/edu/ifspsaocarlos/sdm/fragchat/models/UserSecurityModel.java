package br.edu.ifspsaocarlos.sdm.fragchat.models;

import java.util.Date;

/**
 * Created by LeonardoAlmeida on 18/07/16.
 */

public class UserSecurityModel {

    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_EXPIRED = 2;
    public static final int STATUS_BLOCKED = 3;

    private long id = -1;
    private long userID;
    private String email;
    private String password;
    private String lastPassword;
    private Date genDate;
    private Date lastChangeDate;
    private boolean status;


    public UserSecurityModel(long userID, String email, String password){
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.lastPassword = "";
        this.genDate = new java.sql.Date(new Date().getTime());
        this.lastChangeDate = new java.sql.Date(new Date().getTime()+2000);
        this.status = true;
    }

    public UserSecurityModel (long id, long userID, String email, String password, long genDate, long lastChangeDate, boolean status){
        this.id = id;
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.genDate = new java.sql.Date(genDate);
        this.lastChangeDate = new java.sql.Date(lastChangeDate);
        this.status = status;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastPassword() {
        return lastPassword;
    }

    public void setLastPassword(String lastPassword) {
        this.lastPassword = lastPassword;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }
}
