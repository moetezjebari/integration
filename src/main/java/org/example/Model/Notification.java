package org.example.Model;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private int destinataire_user_id ;
    private int source_user_id ;
    private String title ;
    private String message;
    private String timestamp ;
    private boolean vu ;
    private String type ;
    private int related_item_id;
    // constructeur de l ajout
    public Notification(int des , int source , String title , String message , boolean vu , String type , int related_item_id ) {
           this.destinataire_user_id  = des;
           this.source_user_id  = source;
           this.title = title;
           this.message = message;
           this.vu = vu;
           this.type = type;
           this.related_item_id = related_item_id;
    }
    // constructeur pour recuperer
    public Notification(int id ,int des , int source , String title , String message , String timestamp , boolean vu , String type , int related_item_id ){
           this.id = id;
           this.destinataire_user_id  = des;
           this.source_user_id  = source;
           this.title = title;
           this.message = message;
           this.timestamp = timestamp;
           this.vu = vu;
           this.type = type;
           this.related_item_id = related_item_id;
    }

    // getters
    public int getId() {return id ;}
    public int getDestinataire_user_id() {return destinataire_user_id ;}
    public int getSource_user_id() {return source_user_id ;}
    public String getTitle() {return title;}
    public String getMessage() {return message;}
    public String getTimestamp() {return timestamp;}
    public boolean isVu() {return vu;}
    public String getType() {return type;}
    public int getRelated_item_id() {return related_item_id;}

    // setters
    public void setId(int id) {this.id = id;}
    public void setDestinataire_user_id(String destinataire){this.destinataire_user_id  = destinataire_user_id ;}
    public void setSource_user_id(int source){this.source_user_id  = source;}
    public void setTitle(String title){this.title = title;}
    public void setMessage(String message){this.message = message;}
    public void setTimestamp(String timestamp){this.timestamp = timestamp;}
    public void setVu(boolean vu){this.vu = vu;}
    public void setType(String type){this.type = type;}
    public void setRelated_item_id(int related_item_id){this.related_item_id = related_item_id;}

}
