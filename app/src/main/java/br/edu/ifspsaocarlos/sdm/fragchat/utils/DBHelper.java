package br.edu.ifspsaocarlos.sdm.fragchat.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.fragchat.models.GroupMessagesModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.GroupModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.GroupUsersModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserSecurityModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserTokenModel;

/**
 * Created by LeonardoAlmeida on 08/06/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String APPSTATUS_TABLE_NAME = "status";
    public static final String USERS_TABLE_NAME = "users";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String GROUPS_TABLE_NAME = "groups";
    public static final String GROUP_USERS_TABLE_NAME = "users_groups";
    public static final String USERS_SECURITY_TABLE_NAME = "users_security";
    public static final String USERS_TOKEN_MNG_TABLE_NAME = "users_token";
    public static final String CHAT_MESSAGES_TABLE_NAME = "chat_messages";

    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_APELIDO = "apelido";
    public static final String USERS_COLUMN_DETALHES = "detalhes";
    public static final String USERS_COLUMN_BIO = "bio";
    public static final String USERS_COLUMN_STATUS = "detalhes";
    public static final String USERS_COLUMN_IMGLINK_AVATAR = "avatar";
    public static final String USERS_COLUMN_IMGLINK_HEADER = "header";

    public static final String CONTACT_COLUMN_ID = "id";
    public static final String CONTACT_COLUMN_NAME = "name";
    public static final String CONTACT_COLUMN_EMAIL = "email";
    public static final String CONTACT_COLUMN_APELIDO = "apelido";
    public static final String CONTACT_COLUMN_DETALHES = "detalhes";
    public static final String CONTACT_COLUMN_BIO = "bio";
    public static final String CONTACT_COLUMN_STATUS = "status";
    public static final String CONTACT_COLUMN_IMGLINK_AVATAR = "avatar";
    public static final String CONTACT_COLUMN_IMGLINK_HEADER = "header";

    public static final String GROUPS_COLUMN_ID = "id";
    public static final String GROUPS_COLUMN_NAME = "name";
    public static final String GROUPS_COLUMN_DESCRIPTION = "description";
    public static final String GROUPS_COLUMN_IMGLINK_ICON = "icon";
    public static final String GROUPS_COLUMN_IMGLINK_HEADER = "header";
    public static final String GROUPS_COLUMN_STATUS = "status";

    public static final String GROUP_USERS_COLUMN_SENDERID = "userid";
    public static final String GROUP_USERS_COLUMN_RECEIVERID = "receiverid";
    public static final String GROUP_USERS_COLUMN_GROUPID = "groupid";

    public static final String USERS_SECURITY_COLUMN_ID = "id";
    public static final String USERS_SECURITY_COLUMN_USERID  = "userid";
    public static final String USERS_SECURITY_COLUMN_EMAIL = "email";
    public static final String USERS_SECURITY_COLUMN_PWD = "password";
    public static final String USERS_SECURITY_COLUMN_LASTPWD = "last_password";
    public static final String USERS_SECURITY_COLUMN_GENDATE = "generate_date";
    public static final String USERS_SECURITY_COLUMN_LASTCHGDATE = "last_change_date";
    public static final String USERS_SECURITY_COLUMN_STATUS = "status";

    public static final String USERS_TOKEN_COLUMN_ID = "id";
    public static final String USERS_TOKEN_COLUMN_USERID = "userid";
    public static final String USERS_TOKEN_COLUMN_EMAIL = "email";
    public static final String USERS_TOKEN_COLUMN_TOKEN = "token";
    public static final String USERS_TOKEN_COLUMN_REQUEST_DATE = "request_date";
    public static final String USERS_TOKEN_COLUMN_RELEASE_DATE = "release_date";
    public static final String USERS_TOKEN_COLUMN_STATUS = "status";

    public static final String USERS_CHAT_MESSAGES_COLUMN_ID = "id";
    public static final String USERS_CHAT_MESSAGES_COLUMN_SENDERID = "senderid";
    public static final String USERS_CHAT_MESSAGES_COLUMN_SENDER = "senderName";
    public static final String USERS_CHAT_MESSAGES_COLUMN_GROUPID = "groupid";
    public static final String USERS_CHAT_MESSAGES_COLUMN_RECEIVERID = "receiverid";
    public static final String USERS_CHAT_MESSAGES_COLUMN_MSG = "msg";
    public static final String USERS_CHAT_MESSAGES_COLUMN_REQUESTEDDATE = "requestedDate";
    public static final String USERS_CHAT_MESSAGES_COLUMN_RECEIVEDDATE = "receivedDate";
    public static final String USERS_CHAT_MESSAGES_COLUMN_STATUS = "status";

    public static final String APPSTATUS_COLUMN_ID = "id";
    public static final String APPSTATUS_COLUMN_STATUS = "status";
    public static final short APPSTATUS_LOADING = 1;
    public static final short APPSTATUS_DONE = 0;
    public static final short APPSTATUS_NOTFOUND = -1;

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + APPSTATUS_TABLE_NAME +
                        "(id INTEGER primary key, " +
                        " status short)"
        );
        db.execSQL(
                "create table " + USERS_TABLE_NAME +
                        "(id INTEGER primary key autoincrement, " +
                        " name text," +
                        " email text, " +
                        " apelido text," +
                        " detalhes text, " +
                        " bio text, " +
                        " avatar INTEGER, " +
                        " header INTEGER, " +
                        " status short)"
        );

        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "(id INTEGER primary key, " +
                        " name text," +
                        " email text, " +
                        " apelido text," +
                        " detalhes text, " +
                        " bio text, " +
                        " avatar INTEGER, " +
                        " header INTEGER, " +
                        " status short)"
        );

        db.execSQL(
                "create table " + GROUPS_TABLE_NAME +
                        "(id INTEGER primary key autoincrement, " +
                        " name text," +
                        " description text, " +
                        " icon INTEGER, " +
                        " header INTEGER, " +
                        " status short)"
        );

        db.execSQL(
                "create table " + GROUP_USERS_TABLE_NAME +
                        "(userid INTEGER, " +
                        " receiverid INTEGER," +
                        " groupid INTEGER," +
                        " foreign key (userid) references users(id), " +
                        " foreign key (receiverid) references users(id), " +
                        " foreign key (groupid) references groups(id), " +
                        " PRIMARY KEY (userid, receiverid))"
        );

        db.execSQL(
                "create table " + USERS_SECURITY_TABLE_NAME +
                        "(id INTEGER primary key autoincrement, " +
                        " userid INTEGER, " +
                        " email text, " +
                        " password text, " +
                        " last_password text, " +
                        " generate_date INTEGER, " +
                        " last_change_date INTEGER, " +
                        " status short, " +
                        " foreign key (userid) references users(id))"
        );

        db.execSQL(
                "create table " + USERS_TOKEN_MNG_TABLE_NAME +
                        "(id INTEGER primary key autoincrement, " +
                        " userid INTEGER, " +
                        " email text, " +
                        " token text, " +
                        " request_date INTEGER, " +
                        " release_date INTEGER, " +
                        " status short, " +
                        " foreign key (userid) references users(id))"
        );

        db.execSQL(
                "create table " + CHAT_MESSAGES_TABLE_NAME +
                        "(id INTEGER primary key autoincrement, " +
                        " groupid INTEGER, " +
                        " subject text, " +
                        " msg text, " +
                        " requestedDate INTEGER, " +
                        " receivedDate INTEGER, " +
                        " senderName text, " +
                        " senderid INTEGER, " +
                        " receiverid INTEGER, " +
                        " status short, " +
                        " foreign key (groupid) references groups(id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GROUPS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_SECURITY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TOKEN_MNG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_MESSAGES_TABLE_NAME);
        onCreate(db);
    }

    public short getAppStatus(long statusID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + APPSTATUS_TABLE_NAME + " where " + APPSTATUS_COLUMN_ID + "=" + statusID + "", null);
        res.moveToFirst();

        short status = APPSTATUS_NOTFOUND;

        if (res.getCount() > 0) {
            status = res.getShort(res.getColumnIndex(DBHelper.APPSTATUS_COLUMN_STATUS));
        }

        if (!res.isClosed()) {
            res.close();
        }

        return status;
    }

    public boolean toggleAppStatus(long id,short status) {

        if (status < 0 && status > 1) return false;

        ContentValues contentValues;
        SQLiteDatabase db = this.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put(APPSTATUS_COLUMN_ID, id);
        contentValues.put(APPSTATUS_COLUMN_STATUS, status);
        db.insertWithOnConflict(APPSTATUS_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        return true;
    }

    public long insertContacts(List<UserModel> contactsProfile) {
        long counter = 0;
        if (contactsProfile == null) return counter;

        SQLiteDatabase db = this.getWritableDatabase();

        for (UserModel contact:contactsProfile) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACT_COLUMN_ID, contact.getId());
            contentValues.put(CONTACT_COLUMN_NAME, contact.getNome());
            contentValues.put(CONTACT_COLUMN_EMAIL, contact.getEmail());
            contentValues.put(CONTACT_COLUMN_APELIDO, contact.getApelido());
            contentValues.put(CONTACT_COLUMN_STATUS, UserModel.STATUS_BLOCKED);
            db.insertWithOnConflict(CONTACTS_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            counter++;
        }

        return counter;
    }

    public boolean insertContactList(List<UserModel> contactsProfile, long tokenID , String token) {

        if (contactsProfile == null) return false;

        if (!validateToken(tokenID, token)) return false;

        SQLiteDatabase db = this.getWritableDatabase();

        for (UserModel contact:contactsProfile) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACT_COLUMN_ID, contact.getId());
            contentValues.put(CONTACT_COLUMN_NAME, contact.getNome());
            contentValues.put(CONTACT_COLUMN_EMAIL, contact.getEmail());
            contentValues.put(CONTACT_COLUMN_APELIDO, contact.getApelido());
            contentValues.put(CONTACT_COLUMN_BIO, contact.getBio());
            contentValues.put(CONTACT_COLUMN_DETALHES, contact.getDetalhes());
            contentValues.put(CONTACT_COLUMN_IMGLINK_AVATAR, contact.getImgAvatar());
            contentValues.put(CONTACT_COLUMN_IMGLINK_HEADER, contact.getImgHeader());
            contentValues.put(CONTACT_COLUMN_STATUS, UserModel.STATUS_ONLINE);
            db.insertWithOnConflict(CONTACTS_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }

        return true;
    }

    public boolean insertContact(UserModel contactProfile, long tokenID , String token) {

        if (!validateToken(tokenID, token)) return false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_COLUMN_ID, contactProfile.getId());
        contentValues.put(CONTACT_COLUMN_NAME, contactProfile.getNome());
        contentValues.put(CONTACT_COLUMN_EMAIL, contactProfile.getEmail());
        contentValues.put(CONTACT_COLUMN_APELIDO, contactProfile.getApelido());
        contentValues.put(CONTACT_COLUMN_BIO, contactProfile.getBio());
        contentValues.put(CONTACT_COLUMN_DETALHES, contactProfile.getDetalhes());
        contentValues.put(CONTACT_COLUMN_IMGLINK_AVATAR, contactProfile.getImgAvatar());
        contentValues.put(CONTACT_COLUMN_IMGLINK_HEADER, contactProfile.getImgHeader());
        contentValues.put(CONTACT_COLUMN_STATUS, UserModel.STATUS_ONLINE);
        db.insertWithOnConflict(CONTACTS_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        return true;
    }

    public long getLastContactID(){

        long lastid = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " order by " + CONTACT_COLUMN_ID + " desc ", null);

        res.moveToFirst();

        if (res.getCount() > 0) {
            lastid = res.getInt(res.getColumnIndex(CONTACT_COLUMN_ID));
        }

        if (!res.isClosed()) {
            res.close();
        }

        return lastid;
    }

    public List<UserModel> getContactlist(long userId) {
        UserModel user;
        ArrayList<UserModel> array_list = new ArrayList<UserModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " where " + CONTACT_COLUMN_ID + " != '" + userId + "'", null);
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " order by " + CONTACT_COLUMN_NAME , null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            user = new UserModel();
            user.setId(res.getInt(res.getColumnIndex(CONTACT_COLUMN_ID)));
            user.setNome(res.getString(res.getColumnIndex(CONTACT_COLUMN_NAME)));
            user.setEmail(res.getString(res.getColumnIndex(CONTACT_COLUMN_EMAIL)));
            user.setBio(res.getString(res.getColumnIndex(CONTACT_COLUMN_BIO)));
            user.setDetalhes(res.getString(res.getColumnIndex(CONTACT_COLUMN_DETALHES)));
            user.setStatus(res.getShort(res.getColumnIndex(DBHelper.CONTACT_COLUMN_STATUS)));
            user.setImgAvatar(res.getInt(res.getColumnIndex(CONTACT_COLUMN_IMGLINK_AVATAR)));
            array_list.add(user);
            res.moveToNext();
        }

        if (!res.isClosed()) {
            res.close();
        }

        return array_list;
    }

    public UserModel getContactProfile(long id, UserTokenModel token) {
        UserModel userModel = null;

        if (id < 0) return userModel;
        if (!validateToken(token)) return userModel;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " where " + CONTACT_COLUMN_ID + "=" + id + "", null);
        res.moveToFirst();

        if (res.getCount() > 0) {
            userModel = new UserModel(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_ID)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_EMAIL)));

            userModel.setBio(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_DETALHES)));
            userModel.setBio(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_BIO)));
            userModel.setStatus(res.getShort(res.getColumnIndex(DBHelper.CONTACT_COLUMN_STATUS)));
            userModel.setImgHeader(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_IMGLINK_HEADER)));
            userModel.setImgAvatar(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_IMGLINK_AVATAR)));
        }

        if (!res.isClosed()) {
            res.close();
        }

        return userModel;
    }

    public UserModel findContactByName(String name) {
        UserModel userModel = null;

        if (name == "" ) return userModel;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " " +
                "where " + CONTACT_COLUMN_NAME + " = '" + name + "'" , null);
        res.moveToFirst();

        if (res.getCount() > 0) {
            userModel = new UserModel(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_ID)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_EMAIL)));
            userModel.setApelido(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_APELIDO)));
        }

        if (!res.isClosed()) {
            res.close();
        }

        return userModel;
    }

    public UserModel findContactByEmail(String email) {
        UserModel userModel = null;

        if (email == "" ) return userModel;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " " +
                "where " + CONTACT_COLUMN_EMAIL + " = '" + email + "'" , null);
        res.moveToFirst();

        if (res.getCount() > 0) {
            userModel = new UserModel(res.getLong(res.getColumnIndex(DBHelper.CONTACT_COLUMN_ID)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_EMAIL)));
            userModel.setApelido(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_APELIDO)));
            userModel.setDetalhes(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_DETALHES)));
            userModel.setBio(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_BIO)));
        }

        if (!res.isClosed()) {
            res.close();
        }

        return userModel;
    }

    public UserTokenModel updateContact(UserModel userProfile, UserSecurityModel securityModel) {
        ContentValues contentValues;
        SQLiteDatabase db = this.getWritableDatabase();

        if (userProfile.getId() > 0) {
            contentValues = new ContentValues();
            contentValues.put(CONTACT_COLUMN_ID, userProfile.getId());
            contentValues.put(CONTACT_COLUMN_NAME, userProfile.getNome());
            contentValues.put(CONTACT_COLUMN_EMAIL, userProfile.getEmail());
            contentValues.put(CONTACT_COLUMN_APELIDO, userProfile.getApelido());
            contentValues.put(CONTACT_COLUMN_BIO, userProfile.getBio());
            contentValues.put(CONTACT_COLUMN_DETALHES, userProfile.getDetalhes());
            contentValues.put(CONTACT_COLUMN_IMGLINK_AVATAR, userProfile.getImgAvatar());
            contentValues.put(CONTACT_COLUMN_IMGLINK_HEADER, userProfile.getImgHeader());
            contentValues.put(CONTACT_COLUMN_STATUS, UserModel.STATUS_ONLINE);
            db.insertWithOnConflict(CONTACTS_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
         }

        if (securityModel != null) {
            contentValues = new ContentValues();
            contentValues.put(USERS_SECURITY_COLUMN_USERID, securityModel.getUserID());
            contentValues.put(USERS_SECURITY_COLUMN_EMAIL, securityModel.getEmail());
            contentValues.put(USERS_SECURITY_COLUMN_PWD, securityModel.getPassword());
            contentValues.put(USERS_SECURITY_COLUMN_LASTPWD, securityModel.getLastPassword());
            contentValues.put(USERS_SECURITY_COLUMN_GENDATE, securityModel.getGenDate().getTime());
            contentValues.put(USERS_SECURITY_COLUMN_LASTCHGDATE, securityModel.getLastChangeDate().getTime());
            securityModel.setId(db.insertWithOnConflict(USERS_SECURITY_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE));
        }

        UserTokenModel token = new UserTokenModel(
                                        userProfile.getId(),
                                        userProfile.getEmail(),
                                        generateToken());

        contentValues = new ContentValues();
        contentValues.put(USERS_TOKEN_COLUMN_USERID, token.getUserID());
        contentValues.put(USERS_TOKEN_COLUMN_EMAIL, token.getEmail());
        contentValues.put(USERS_TOKEN_COLUMN_TOKEN, token.getToken());
        contentValues.put(USERS_TOKEN_COLUMN_REQUEST_DATE, token.getRequestDate().getTime());
        contentValues.put(USERS_TOKEN_COLUMN_RELEASE_DATE, token.getReleaseDate().getTime());
        contentValues.put(USERS_TOKEN_COLUMN_STATUS, token.getUserID());

        token.setId(db.insertWithOnConflict(USERS_TOKEN_MNG_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_IGNORE));

        return token;

    }

    public UserModel getContactProfile(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " where " + CONTACT_COLUMN_ID + "=" + id + "", null);
        res.moveToFirst();

        UserModel userModel = new UserModel(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_ID)),
                res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_NAME)),
                res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_EMAIL)));
        userModel.setApelido(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_APELIDO)));
        userModel.setDetalhes(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_DETALHES)));
        userModel.setBio(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_BIO)));
        userModel.setStatus(res.getShort(res.getColumnIndex(DBHelper.CONTACT_COLUMN_STATUS)));
        userModel.setImgHeader(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_IMGLINK_HEADER)));
        userModel.setImgAvatar(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_IMGLINK_AVATAR)));

        if (!res.isClosed()) {
            res.close();
        }

        return userModel;
    }

    private String generateToken() {
        return "123456789";
    }

    public UserModel validateContact(String user) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " where " + CONTACT_COLUMN_EMAIL + "='" + user + "'", null);

        UserModel userobj = null;

        if(res.moveToFirst()) {
            userobj = new UserModel(
                    res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_ID)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_EMAIL)));
            userobj.setApelido(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_APELIDO)));
            userobj.setDetalhes(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_DETALHES)));
            userobj.setBio(res.getString(res.getColumnIndex(DBHelper.CONTACT_COLUMN_BIO)));
            userobj.setImgAvatar(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_IMGLINK_AVATAR)));
            userobj.setImgHeader(res.getInt(res.getColumnIndex(DBHelper.CONTACT_COLUMN_IMGLINK_HEADER)));
            userobj.setStatus(res.getShort(res.getColumnIndex(DBHelper.CONTACT_COLUMN_STATUS)));

        }

            return userobj;
    }

    public boolean validateToken(UserTokenModel token){

        if (token == null) return false;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select " + USERS_TOKEN_COLUMN_STATUS + " from " + USERS_TOKEN_MNG_TABLE_NAME + " where " + USERS_TOKEN_COLUMN_ID + " = '" + token.getId() + "' and "
                                    + USERS_TOKEN_COLUMN_TOKEN + " = '" + token.getToken() + "'", null );

        boolean tokenStatus = (res.getCount()>0)?true:false;
        if (!res.isClosed()) {
            res.close();
        }

        return tokenStatus;
    }

    public boolean validateToken(long tokenID, String token){

        if (token == "" || token == null) return false;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select " + USERS_TOKEN_COLUMN_STATUS + " from " + USERS_TOKEN_MNG_TABLE_NAME + " where " + USERS_TOKEN_COLUMN_ID + " = '" + tokenID + "' and "
                + USERS_TOKEN_COLUMN_TOKEN + " = '" + token + "'", null );

        boolean tokenStatus = (res.getCount()>0)?true:false;
        if (!res.isClosed()) {
            res.close();
        }

        return tokenStatus;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USERS_TABLE_NAME);
        return numRows;
    }

    public boolean updateUserProfile(UserModel userProfile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_DETALHES, userProfile.getDetalhes());
        contentValues.put(USERS_COLUMN_BIO, userProfile.getBio());
        contentValues.put(USERS_COLUMN_IMGLINK_AVATAR, userProfile.getImgAvatar());
        contentValues.put(USERS_COLUMN_IMGLINK_HEADER, userProfile.getImgHeader());
        db.update(USERS_TABLE_NAME, contentValues, USERS_COLUMN_ID + " = ? ", new String[]{Long.toString(userProfile.getId())});
        return true;
    }

    public boolean updateUserStatus(long userID, boolean online) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_STATUS, (online) ? 0 : 1);
        db.update(USERS_TABLE_NAME, contentValues, USERS_COLUMN_ID + " = ? ", new String[]{Long.toString(userID)});
        return true;
    }


    public long deleteUserProfile(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USERS_TABLE_NAME,
                USERS_COLUMN_ID + " = ? ",
                new String[]{Long.toString(id)});
    }

    public List<UserModel> getAllUsers(long userId) {
        UserModel user;
        ArrayList<UserModel> array_list = new ArrayList<UserModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + USERS_TABLE_NAME + " where " + USERS_COLUMN_ID + " != '" + userId + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            user = new UserModel();
            user.setId(res.getInt(res.getColumnIndex(USERS_COLUMN_ID)));
            user.setNome(res.getString(res.getColumnIndex(USERS_COLUMN_NAME)));
            user.setEmail(res.getString(res.getColumnIndex(USERS_COLUMN_EMAIL)));
            user.setBio(res.getString(res.getColumnIndex(USERS_COLUMN_BIO)));
            user.setDetalhes(res.getString(res.getColumnIndex(USERS_COLUMN_DETALHES)));
            user.setStatus(res.getShort(res.getColumnIndex(DBHelper.USERS_COLUMN_STATUS)));
            user.setImgAvatar(res.getInt(res.getColumnIndex(USERS_COLUMN_IMGLINK_AVATAR)));
            array_list.add(user);
            res.moveToNext();
        }

        if (!res.isClosed()) {
            res.close();
        }

        return array_list;
    }

    public GroupModel createGroup(GroupModel group, long senderid, long receiverid){

        if (group == null) return group;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GROUPS_COLUMN_NAME, group.getName());
        contentValues.put(GROUPS_COLUMN_DESCRIPTION, group.getDescription());
        contentValues.put(GROUPS_COLUMN_IMGLINK_ICON, group.getImgIcon());
        contentValues.put(GROUPS_COLUMN_IMGLINK_HEADER, group.getImgHeader());
        contentValues.put(GROUPS_COLUMN_STATUS, GroupModel.STATUS_ACTIVE);
        group.setId(db.insert(GROUPS_TABLE_NAME, null, contentValues));

        joinGroup(group.getId(), senderid, receiverid);

        return group;
    }

    public boolean joinGroup(long groupid, long senderid, long receiverid) {

        if (groupid <= 0 || senderid <= 0 || receiverid <= 0) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GROUP_USERS_COLUMN_GROUPID, groupid);
        contentValues.put(GROUP_USERS_COLUMN_SENDERID, senderid);
        contentValues.put(GROUP_USERS_COLUMN_RECEIVERID, receiverid);
        db.insert(GROUP_USERS_TABLE_NAME, null, contentValues);

        return true;
    }

    public GroupModel getGroupDescription(long groupId){
        GroupModel group = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GROUPS_TABLE_NAME + " where " + GROUPS_COLUMN_ID + "=" + groupId + "", null);

        if (res.getCount()>0) {
            res.moveToFirst();
            group = new GroupModel();
            group.setId(res.getInt(res.getColumnIndex(GROUPS_COLUMN_ID)));
            group.setName(res.getString(res.getColumnIndex(GROUPS_COLUMN_NAME)));
            group.setDescription(res.getString(res.getColumnIndex(GROUPS_COLUMN_DESCRIPTION)));
            group.setImgIcon(res.getInt(res.getColumnIndex(GROUPS_COLUMN_IMGLINK_ICON)));
            group.setImgHeader(res.getInt(res.getColumnIndex(GROUPS_COLUMN_IMGLINK_HEADER)));
            group.setStatus((res.getShort(res.getColumnIndex(DBHelper.GROUPS_COLUMN_STATUS)) == GroupModel.STATUS_BLOCKED) ? false : true);
        }

        if (!res.isClosed()) {
            res.close();
        }

        return group;
    }
    public long findGroupID(long senderId, long receveiverId) {
        if (senderId <= 0 || receveiverId <= 0) return -1;

        long groupID = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + GROUP_USERS_COLUMN_GROUPID + " from " + GROUP_USERS_TABLE_NAME +
                                " where " + GROUP_USERS_COLUMN_SENDERID + " = " + senderId +
                                " AND " + GROUP_USERS_COLUMN_RECEIVERID + " = " + receveiverId, null);
        res.moveToFirst();

        if (res.getCount()>0) {
            groupID = res.getInt(res.getColumnIndex(GROUP_USERS_COLUMN_GROUPID));
        }

        if (!res.isClosed()) {
            res.close();
        }

        return groupID;
    }

    public List<GroupUsersModel> getGroupUsersList(long groupid) {
        ArrayList<GroupUsersModel> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GROUP_USERS_TABLE_NAME + " where " + GROUP_USERS_COLUMN_GROUPID + " = '" + groupid + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(new GroupUsersModel(  res.getInt(res.getColumnIndex(GROUP_USERS_COLUMN_SENDERID)),
                                                res.getInt(res.getColumnIndex(GROUP_USERS_COLUMN_RECEIVERID)),
                                                res.getInt(res.getColumnIndex(GROUP_USERS_COLUMN_GROUPID))));
            res.moveToNext();
        }

        if (!res.isClosed()) {
            res.close();
        }

        return array_list;
    }

    public List<UserModel> getGroupUsersProfiles(long groupid, long userId) {
        ArrayList<UserModel> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GROUP_USERS_TABLE_NAME + " where " + GROUP_USERS_COLUMN_GROUPID + " = '" + groupid + "' order by " + GROUP_USERS_COLUMN_SENDERID, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            long userid = res.getInt(res.getColumnIndex(GROUP_USERS_COLUMN_SENDERID));
            array_list.add(getContactProfile(userId));

            long receiverid = res.getInt(res.getColumnIndex(GROUP_USERS_COLUMN_RECEIVERID));
            array_list.add(getContactProfile(receiverid));

            res.moveToNext();
        }

        if (!res.isClosed()) {
            res.close();
        }
        return array_list;
    }

    public long getLastMessageID(long useridSender, long useridReceiver){

        long lastid = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CHAT_MESSAGES_TABLE_NAME +
                " where " + USERS_CHAT_MESSAGES_COLUMN_SENDERID + " = " + useridSender +
                " and " + USERS_CHAT_MESSAGES_COLUMN_RECEIVERID + " = " + useridReceiver +
                " order by " + USERS_CHAT_MESSAGES_COLUMN_ID + " desc ", null);

        res.moveToFirst();

        if (res.getCount() > 0) {
            lastid = res.getInt(res.getColumnIndex(USERS_CHAT_MESSAGES_COLUMN_ID));
        }

        if (!res.isClosed()) {
            res.close();
        }

        return lastid;
    }

    public List<GroupMessagesModel> requestMessages(long useridSender, long useridReceiver, UserTokenModel token){

        if (!validateToken(token)) return null;

        String url = "select * from " + CHAT_MESSAGES_TABLE_NAME +
                " where " + USERS_CHAT_MESSAGES_COLUMN_SENDERID + " = " + useridSender +
                " and " + USERS_CHAT_MESSAGES_COLUMN_RECEIVERID + " = " + useridReceiver +
                " order by " + USERS_CHAT_MESSAGES_COLUMN_ID + " desc ";

        List<GroupMessagesModel> returnList = getRequiredMessages(url);

        for (GroupMessagesModel msgItem:returnList) {
            msgItem.setStatus(GroupMessagesModel.STATUS_READ);
            updateMessageStatus(msgItem, token);
        }

        return returnList;
    }

    public List<GroupMessagesModel> requestLastMessages(long useridSender, long useridReceiver, UserTokenModel token) {

        if (!validateToken(token)) return null;

        String url = "select * from " + CHAT_MESSAGES_TABLE_NAME +
                " where " + USERS_CHAT_MESSAGES_COLUMN_SENDERID + " = " + useridSender +
                " and " + USERS_CHAT_MESSAGES_COLUMN_RECEIVERID + " = " + useridReceiver +
                " and " + USERS_CHAT_MESSAGES_COLUMN_STATUS + " = " + GroupMessagesModel.STATUS_RECEIVED  +
                " order by " + USERS_CHAT_MESSAGES_COLUMN_ID + " desc ";

        List<GroupMessagesModel> returnList = getRequiredMessages(url);

        for (GroupMessagesModel msgItem:returnList) {
            msgItem.setStatus(GroupMessagesModel.STATUS_READ);
            updateMessageStatus(msgItem, token);
        }

        return returnList;
    }

    public List<GroupMessagesModel> requestAllMessages(long conversationGroupId, UserTokenModel token) {

        if (!validateToken(token)) return null;

        String url = "select * from " + CHAT_MESSAGES_TABLE_NAME +
                    " where " + USERS_CHAT_MESSAGES_COLUMN_GROUPID + "=" + conversationGroupId;

        List<GroupMessagesModel> returnList = getRequiredMessages(url);

        for (GroupMessagesModel msgItem:returnList) {
            msgItem.setStatus(GroupMessagesModel.STATUS_READ);
            updateMessageStatus(msgItem, token);
        }

        return returnList;
    }

    private List<GroupMessagesModel> getRequiredMessages(String url) {

        List<GroupMessagesModel> groupMessagesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(url, null);

        try {
            while (res.moveToNext()) {
                GroupMessagesModel chatModel = new GroupMessagesModel(
                        res.getInt(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_ID)));
                chatModel.setSender(res.getString(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_SENDER)));
                chatModel.setSenderID(res.getInt(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_SENDERID)));
                chatModel.setReceiverID(res.getInt(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_RECEIVERID)));
                chatModel.setGroupID(res.getInt(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_GROUPID)));
                chatModel.setMsg(res.getString(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_MSG)));
                chatModel.setRequestedDate(new Date(res.getInt(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_REQUESTEDDATE))));
                chatModel.setReceivedDate(new Date(res.getInt(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_RECEIVEDDATE))));
                chatModel.setStatus(res.getShort(res.getColumnIndex(DBHelper.USERS_CHAT_MESSAGES_COLUMN_STATUS)));

                groupMessagesList.add(chatModel);
            }
        } finally {
            res.close();
        }

        return groupMessagesList;
    }

    public boolean saveMessage(GroupMessagesModel chatMessage, UserTokenModel token) {

        if (chatMessage == null) return false;

        if (!validateToken(token)) return false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_GROUPID, chatMessage.getGroupID());
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_MSG, chatMessage.getMsg());
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_RECEIVERID, chatMessage.getReceiverID());
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_SENDER, chatMessage.getSender());
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_SENDERID, chatMessage.getSenderID());
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_REQUESTEDDATE, chatMessage.getRequestedDate().getTime());
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_STATUS, chatMessage.getStatus());
        db.insertWithOnConflict(CHAT_MESSAGES_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        return true;
    }

    public boolean saveReceivedMessage(GroupMessagesModel chatMessage, long tokenID , String token) {

        if (chatMessage==null) return false;

        if (!validateToken(tokenID, token)) return false;

        SQLiteDatabase db = this.getWritableDatabase();

        //for (GroupMessagesModel chatMessage:sentMsgs) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_GROUPID, chatMessage.getGroupID());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_MSG, chatMessage.getMsg());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_RECEIVERID, chatMessage.getReceiverID());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_SENDER, chatMessage.getSender());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_SENDERID, chatMessage.getSenderID());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_REQUESTEDDATE, chatMessage.getRequestedDate().getTime());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_STATUS, chatMessage.getStatus());
        db.insertWithOnConflict(CHAT_MESSAGES_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        //}

        return true;
    }

    public boolean saveReceivedMessages(List<GroupMessagesModel> sentMsgs, long tokenID , String token) {

        if (sentMsgs==null) return false;

        if (!validateToken(tokenID, token)) return false;

        SQLiteDatabase db = this.getWritableDatabase();

        for (GroupMessagesModel chatMessage:sentMsgs) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_GROUPID, chatMessage.getGroupID());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_MSG, chatMessage.getMsg());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_RECEIVERID, chatMessage.getReceiverID());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_SENDER, chatMessage.getSender());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_SENDERID, chatMessage.getSenderID());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_REQUESTEDDATE, chatMessage.getRequestedDate().getTime());
            contentValues.put(USERS_CHAT_MESSAGES_COLUMN_STATUS, chatMessage.getStatus());
            db.insertWithOnConflict(CHAT_MESSAGES_TABLE_NAME, BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }

        return true;
    }

    public boolean updateMessageStatus(GroupMessagesModel chatMessage, UserTokenModel token) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_CHAT_MESSAGES_COLUMN_STATUS, chatMessage.getStatus());
        db.update(CHAT_MESSAGES_TABLE_NAME, contentValues, USERS_CHAT_MESSAGES_COLUMN_ID + " = ? ", new String[]{Long.toString(chatMessage.getId())});

        return true;

    }

    public UserTokenModel getUserToken(long userid) {
        UserTokenModel token  = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + USERS_TOKEN_MNG_TABLE_NAME + " where " + USERS_TOKEN_COLUMN_USERID + "=" + userid + "", null);
        res.moveToFirst();

        if (res.getCount()>0) {
            token = new UserTokenModel(
                    res.getInt(res.getColumnIndex(DBHelper.USERS_TOKEN_COLUMN_ID)),
                    res.getInt(res.getColumnIndex(DBHelper.USERS_TOKEN_COLUMN_USERID)),
                    res.getString(res.getColumnIndex(DBHelper.USERS_TOKEN_COLUMN_TOKEN)),
                    res.getInt(res.getColumnIndex(DBHelper.USERS_TOKEN_COLUMN_REQUEST_DATE)),
                    res.getInt(res.getColumnIndex(DBHelper.USERS_TOKEN_COLUMN_RELEASE_DATE)),
                    res.getShort(res.getColumnIndex(DBHelper.USERS_TOKEN_COLUMN_STATUS)) == UserTokenModel.STATUS_DISABLED ? false : true);
        }

        if (!res.isClosed()) {
            res.close();
        }

        return token;
    }
}