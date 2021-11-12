package com.mishasho.tienda.moda.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.mishasho.tienda.moda.database.DBFavouriteProducts;

import java.util.Date;

public class Gallery implements Parcelable {

    private String user_id;
    private String gallery;
    private String store;
    private String image_name;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(this.user_id);
       dest.writeString(this.gallery);
       dest.writeString(this.store);
       dest.writeString(this.image_name);



    }

    public Gallery() {
    }

    protected Gallery(Parcel in) {
        this.user_id = in.readString();
        this.gallery = in.readString();
        this.store = in.readString();
        this.image_name = in.readString();



    }

    public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel source) {
            return new Gallery(source);
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };
}
