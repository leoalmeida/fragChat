package br.edu.ifspsaocarlos.sdm.fragchat.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeonardoAlmeida on 08/06/16.
 */
public class UserModel {

    public static final short STATUS_FAILED = -1;
    public static final short STATUS_OFFLINE = 0;
    public static final short STATUS_ONLINE = 1;
    public static final short STATUS_EXPIRED = 2;
    public static final short STATUS_BLOCKED = 3;

    private long id = -1;
    private String nome;
    private String email;
    private String apelido;
    private String bio;
    private String detalhes;
    private int imgAvatar = -1;
    private int imgHeader = 1;
    private short status;


    public UserModel(long id, String nome, String email, String apelido, String bio,
                     String detalhes, int imgAvatar, int imgHeader, short status){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.apelido = apelido;
        this.bio = bio;
        this.detalhes = detalhes;
        this.imgAvatar = imgAvatar;
        this.imgHeader = imgHeader;
        this.status = status;
    }


    public UserModel(){
        this.nome = this.email = this.bio = this.detalhes = this.apelido = "";
        this.status = STATUS_OFFLINE;
    }

    public UserModel(String nome,String email){
        this.nome = nome;
        this.email = email;
    }
    public UserModel(long id, String nome,String email){
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public UserModel(JSONObject jsonObject, int imgAvatar, int imgHeader) {
        try {
            this.id = jsonObject.getLong("id");
            this.email = jsonObject.getString("apelido") + "@teste.com";
            this.nome = jsonObject.getString("nome_completo");
            this.apelido = jsonObject.getString("apelido");
            this.status = STATUS_EXPIRED;
        }catch (JSONException je) {
            this.status = STATUS_FAILED;
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getApelido() {
        return apelido;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(int imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public int getImgHeader() { return imgHeader; }

    public void setImgHeader(int imgHeader) {
        this.imgHeader = imgHeader;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public boolean isOnline() {
        return (this.status==STATUS_ONLINE)?true:false;
    }

    public void setStatus(short online) {
        this.status = online;
    }
    public void toggleOnline() {
        this.status = (this.status==STATUS_ONLINE)?STATUS_OFFLINE:STATUS_ONLINE;
    }

}
