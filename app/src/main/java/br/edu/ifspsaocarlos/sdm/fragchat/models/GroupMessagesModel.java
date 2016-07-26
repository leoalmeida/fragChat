package br.edu.ifspsaocarlos.sdm.fragchat.models;

/**
 * Created by LeonardoAlmeida on 15/07/16.
 */


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.edu.ifspsaocarlos.sdm.fragchat.view.messenger.ActivityDefault;

public class GroupMessagesModel implements Comparable<GroupMessagesModel>{

        public static final short ALL_MESSAGES = -1;
        public static final short STATUS_SAVED = 0;
        public static final short STATUS_SENDING = 1;
        public static final short STATUS_SENT = 2;
        public static final short STATUS_RECEIVED = 3;
        public static final short STATUS_READ = 4;
        public static final short STATUS_FAILED = 5;

        private long id;
        private long groupID;
        private String sender;
        private long senderID;
        private long receiverID;
        private Date requestedDate;
        private Date receivedDate;
        private String subject;
        private String msg;
        private int status = STATUS_SENT;

        /**
         * Instantiates a new conversation.
         *
         * @param msg
         *            the msg
         * @param requestedDate
         *            the requestedDate
         * @param sender
         *            the sender
         */
        public GroupMessagesModel(long groupID, String subject, String msg, Date requestedDate, UserModel sender, long receiverID) {
            this.groupID = groupID;
            this.subject = subject;
            this.msg = msg;
            this.requestedDate = requestedDate;
            this.sender = sender.getNome();
            this.senderID = sender.getId();
            this.receiverID = receiverID;
        }

        /**
         * Instantiates a new conversation.
         */
        public GroupMessagesModel(long id) {
            this.id = id;
        }

        public GroupMessagesModel(JSONObject jsonObject, int msgstatus) {
            try {
                this.id = jsonObject.getLong("id");
                this.senderID = jsonObject.getLong("origem_id");
                this.sender = "";
                this.receiverID = jsonObject.getLong("destino_id");
                this.subject = jsonObject.getString("assunto");
                this.msg = jsonObject.getString("corpo");
                this.groupID = jsonObject.getLong("destino_id");
                this.requestedDate = new Date(0);
                this.status = msgstatus;
            }catch (JSONException je) {
                this.status = STATUS_FAILED;
            }
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getGroupID() {
            return groupID;
        }

        public void setGroupID(long groupID) {
            this.groupID = groupID;
        }

        public long getReceiverID() {
            return receiverID;
        }

        public void setReceiverID(long receiverID) {
            this.receiverID = receiverID;
        }

        public Date getReceivedDate() {
            return receivedDate;
        }

        public void setReceivedDate(Date receivedDate) {
            this.receivedDate = receivedDate;
        }

        /**
         * Gets the subject.
         *
         * @return the subject
         */
        public String getSubject() {
        return subject;
    }

        /**
         * Sets the subject.
         *
         * @param subject
         *            the new subject
         */
        public void setSubject(String subject) {
        this.subject = subject;
    }

        /**
         * Gets the msg.
         *
         * @return the msg
         */
        public String getMsg() {
            return msg;
        }

        /**
         * Sets the msg.
         *
         * @param msg
         *            the new msg
         */
        public void setMsg(String msg) {
            this.msg = msg;
        }

        /**
         * Checks if is sent.
         *
         * @return true, if is sent
         */
        public boolean isSent(long userID) {
            return (userID == receiverID);
        }

        /**
         * Gets the requestedDate.
         *
         * @return the requestedDate
         */
        public Date getRequestedDate()
        {
            return requestedDate;
        }

        /**
         * Sets the requestedDate.
         *
         * @param requestedDate
         *            the new requestedDate
         */
        public void setRequestedDate(Date requestedDate)
        {
            this.requestedDate = requestedDate;
        }

        /**
         * Gets the senderID.
         *
         * @return the sender
         */
        public long getSenderID()
        {
            return senderID;
        }
        public void setSenderID(long senderID) { this.senderID = senderID; }


        /**
         * Sets the sender.
         *
         * @param sender
         *            the new sender
         */

        public void setSender(String sender) { this.sender = sender; }
        public String getSender() { return sender; }

        /**
         * Gets the status.
         *
         * @return the status
         */
        public int getStatus()
        {
            return status;
        }

        /**
         * Sets the status.
         *
         * @param status
         *            the new status
         */
        public void setStatus(int status)
        {
            this.status = status;
        }

        public boolean isSent()
        {
            return (ActivityDefault.loggedUserProfile.getId() == senderID);
        }


    @Override
    public int compareTo(GroupMessagesModel msg) {
        if (this.id < msg.id) {
            return -1;
        }
        if (this.id > msg.id) {
            return 1;
        }
        return 0;
    }
}
