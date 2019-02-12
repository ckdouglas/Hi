package com.example.hi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    String name, phone, id, profile_URL,last_text,status;


    public User(){}
    public User( String id, String name, String phone,String profile_URL) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.profile_URL = profile_URL;

    }

    public User(String name, String profile_URL){
        this.name = name;
        this.profile_URL = profile_URL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUid(String uid) {
        this.id = uid;
    }

    public String getProfile_URL() {
        return profile_URL;
    }

    public void setProfile_URL(String profile_URL) {
        this.profile_URL = profile_URL;
    }

    public String getName() {
            return name;
        }

    public String getPhone() {
            return phone;
        }

    public String getUid() {
            return id;
        }


    public String getLast_text() {
        return last_text;
    }

    public void setLast_text(String last_text) {
        this.last_text = last_text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    /**
     *
     * parcelable implementation
     *
     *
     */

    protected User(Parcel in) {
        name = in.readString();
        phone = in.readString();
        id = in.readString();
        profile_URL = in.readString();
        last_text = in.readString();
        status = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(id);
        dest.writeString(profile_URL);
        dest.writeString(last_text);
        dest.writeString(status);
    }
}
