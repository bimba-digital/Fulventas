package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Politics implements Parcelable {

    private String politicas;

    public String getPoliticas() {
        return politicas;
    }

    public void setPoliticas(String politicas) {
        this.politicas = politicas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(this.politicas);




    }

    public Politics() {
    }

    protected Politics(Parcel in) {
        this.politicas = in.readString();


    }

    public static final Creator<Politics> CREATOR = new Creator<Politics>() {
        @Override
        public Politics createFromParcel(Parcel source) {
            return new Politics(source);
        }

        @Override
        public Politics[] newArray(int size) {
            return new Politics[size];
        }
    };
}
