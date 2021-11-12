package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Types implements Parcelable {

    private String id;
    private String title;
    private int sort;
    private String image_name;
    private  String m;
    private  String p;
    private  String user_message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }



    public String getTitle() {
        return title;
    }

    public int getSort() {
        return sort;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getM() {
        return m;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getUser_message() {
        return user_message;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.sort);
        dest.writeString(this.image_name);
    }

    public Types(String id, String title, String image_name ) {
        this.id=id;
        this.title=title;
        this.image_name = image_name;
    }
    public Types( ) {
    }

    protected Types(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.sort = in.readInt();
        this.image_name = in.readString();
    }

    public static final Parcelable.Creator<Types> CREATOR = new Parcelable.Creator<Types>() {
        @Override
        public Types createFromParcel(Parcel source) {
            return new Types(source);
        }

        @Override
        public Types[] newArray(int size) {
            return new Types[size];
        }
    };
}
