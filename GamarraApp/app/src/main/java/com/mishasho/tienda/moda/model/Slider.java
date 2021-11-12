package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Slider implements Parcelable {

    private int id;
    private String title;
    private  String description;
    private  String image_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.image_name);
    }

    public Slider() {
    }

    protected Slider(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.image_name = in.readString();
    }

    public static final Creator<Slider> CREATOR = new Creator<Slider>() {
        @Override
        public Slider createFromParcel(Parcel source) {
            return new Slider(source);
        }

        @Override
        public Slider[] newArray(int size) {
            return new Slider[size];
        }
    };
}
