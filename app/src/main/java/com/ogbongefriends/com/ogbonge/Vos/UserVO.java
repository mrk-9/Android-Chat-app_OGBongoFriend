package com.ogbongefriends.com.ogbonge.Vos;

import com.google.gson.JsonObject;

/**
 * Created by Icreon on 06-07-2015.
 */
public class UserVO {

    private int id;
    private String uuid;
    private String first_name;
    private String last_name;
    private String email;
    private int gender;


    public UserVO(JsonObject jsonObject) {
        try {
            this.id = jsonObject.get("id").getAsInt();
            this.uuid = jsonObject.get("uuid").getAsString();
            this.first_name = jsonObject.get("first_name").getAsString();
            this.last_name = jsonObject.get("last_name").getAsString();
            this.email = jsonObject.get("email").getAsString();
            this.gender = jsonObject.get("gender").getAsInt();
            this.profile_pic = jsonObject.get("profile_pic").getAsString();
            this.latitude = jsonObject.get("latitude").getAsString();
            this.longitude = jsonObject.get("longitude").getAsString();
            this.points = jsonObject.get("points").getAsInt();
            this.date_of_birth = jsonObject.get("date_of_birth").getAsString();
            this.status = jsonObject.get("status").getAsInt();
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String profile_pic;
    private String latitude;
    private String longitude;
    private int points;
    private String date_of_birth;
    private int status;








}
