package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Culqui implements Parcelable {

    private String culqi_title;
    private String culqi_authorization;
    private String culqi_publickey;


    public String getCulqi_title() {
        return culqi_title;
    }

    public void setCulqi_title(String culqi_title) {
        this.culqi_title = culqi_title;
    }

    public String getCulqi_authorization() {
        return culqi_authorization;
    }

    public void setCulqi_authorization(String culqi_authorization) {
        this.culqi_authorization = culqi_authorization;
    }

    public String getCulqi_publickey() {
        return culqi_publickey;
    }

    public void setCulqi_publickey(String culqi_publickey) {
        this.culqi_publickey = culqi_publickey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(this.culqi_title);
       dest.writeString(this.culqi_authorization);
       dest.writeString(this.culqi_publickey);

    }

    public Culqui() {
    }

    protected Culqui(Parcel in) {
        this.culqi_title = in.readString();
        this.culqi_authorization = in.readString();
        this.culqi_publickey = in.readString();
    }

    public static final Creator<Culqui> CREATOR = new Creator<Culqui>() {
        @Override
        public Culqui createFromParcel(Parcel source) {
            return new Culqui(source);
        }

        @Override
        public Culqui[] newArray(int size) {
            return new Culqui[size];
        }
    };
}
