package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PushNotification implements Parcelable {

    String title;
    String desc;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.desc);
    }

    public PushNotification() {
    }

    protected PushNotification(Parcel in) {
        this.title = in.readString();
        this.desc = in.readString();
    }

    public static final Parcelable.Creator<PushNotification> CREATOR = new Parcelable.Creator<PushNotification>() {
        @Override
        public PushNotification createFromParcel(Parcel source) {
            return new PushNotification(source);
        }

        @Override
        public PushNotification[] newArray(int size) {
            return new PushNotification[size];
        }
    };
}
