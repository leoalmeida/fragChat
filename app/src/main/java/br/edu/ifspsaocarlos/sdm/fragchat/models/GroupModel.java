package br.edu.ifspsaocarlos.sdm.fragchat.models;

/**
 * Created by LeonardoAlmeida on 19/07/16.
 */

public class GroupModel {

        public static final int STATUS_DISABLED = 0;
        public static final int STATUS_ACTIVE = 1;
        public static final int STATUS_EXPIRED = 2;
        public static final int STATUS_BLOCKED = 3;

        private long id = -1;
        private String name;
        private String description;
        private long imgIcon = -1;
        private long imgHeader = -1;
        private boolean status;

        public GroupModel(){
            this.name = this.description = "";
            this.status = true;
        }

        public GroupModel(String name, String description){
            this.name = name;
            this.description = description;
            this.status = true;
        }

        public GroupModel(long id, String name, String description){
            this.id = id;
            this.name = name;
            this.description = description;
            this.status = true;
        }
        public GroupModel(String name, String description, long imgIcon, long imgHeader){
            this.name = name;
            this.description = description;
            this.imgIcon = imgIcon;
            this.imgHeader = imgHeader;
            this.status = true;
        }

        public long getId() {
        return this.id;
    }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getImgIcon() {
            return this.imgIcon;
        }

        public void setImgIcon(long imgIcon) {
            this.imgIcon = imgIcon;
        }

        public long getImgHeader() {
            return this.imgHeader;
        }

        public void setImgHeader(long imgHeader) {
            this.imgHeader = imgHeader;
        }

        public boolean isStatus() { return this.status; }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public void toggleStatus() {
            this.status = !this.status;
        }

}
