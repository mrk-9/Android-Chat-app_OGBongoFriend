package com.ogbongefriends.com.ogbonge.Vos;

import com.google.gson.JsonObject;

/**
 * Created by hp-hp on 17-02-2016.
 */
public class PhotoOfYouVO {
    private int id;


    private String photo_pic;
    private String fullPhotoUrl;
    private boolean isChecked;
    private boolean isSelected;




    public PhotoOfYouVO(JsonObject jsonObject) {
        try {
            this.id = jsonObject.get("id").getAsInt();
            this.photo_pic = jsonObject.get("photo_pic").getAsString();
            isChecked=false;
            isSelected=false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfullPhotoUrl() {
        return fullPhotoUrl;
    }

    public void setfullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }



    public String getPhoto_pic() {
        return photo_pic;
    }

    public void setPhoto_pic(String photo_pic) {
        this.photo_pic = photo_pic;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}


