package br.edu.ifspsaocarlos.sdm.fragchat.models;

/**
 * Created by LeonardoAlmeida on 19/07/16.
 */

public class GroupUsersModel {
    private long groupid = -1;
    private long senderid = -1;
    private long receiverid = -1;

    public long getGroupID() {
        return groupid;
    }

    public void setGroupID(long groupid) {
        this.groupid = groupid;
    }

    public long getSenderID() {
        return senderid;
    }

    public long getReceiverID() {
        return receiverid;
    }

    public GroupUsersModel(long senderid, long receiverid){
        this.senderid = senderid;
        this.receiverid = receiverid;
    }

    public GroupUsersModel(long senderid, long receiverid, long groupid){
        this.groupid = groupid;
        this.senderid = senderid;
        this.receiverid = receiverid;
    }
}
