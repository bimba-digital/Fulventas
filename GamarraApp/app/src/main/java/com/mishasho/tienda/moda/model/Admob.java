package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Admob implements Parcelable {

    private int admob_id;
    private String app_id;
    private String add_unit_id;
    private int admin_id;


    public int getAdmob_id() {
        return admob_id;
    }

    public void setAdmob_id(int admob_id) {
        this.admob_id = admob_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getAdd_unit_id() {
        return add_unit_id;
    }

    public void setAdd_unit_id(String add_unit_id) {
        this.add_unit_id = add_unit_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.admob_id);
        dest.writeString(this.app_id);
        dest.writeString(this.add_unit_id);
        dest.writeInt(this.admin_id);
    }

    public Admob() {
    }

    protected Admob(Parcel in) {
        this.admob_id = in.readInt();
        this.app_id = in.readString();
        this.add_unit_id = in.readString();
        this.admin_id = in.readInt();
    }

    public static final Parcelable.Creator<Admob> CREATOR = new Parcelable.Creator<Admob>() {
        @Override
        public Admob createFromParcel(Parcel source) {
            return new Admob(source);
        }

        @Override
        public Admob[] newArray(int size) {
            return new Admob[size];
        }
    };
}
