package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductSize implements Parcelable {

    private int size_id;
    private String sizeName;
    private  String size_name;

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public String getSizeName() {
        return sizeName;
    }
    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.size_id);
        dest.writeString(this.sizeName);
    }

    public ProductSize() {
    }

    protected ProductSize(Parcel in) {
        this.size_id = in.readInt();
        this.sizeName = in.readString();
    }

    public static final Creator<ProductSize> CREATOR = new Creator<ProductSize>() {
        @Override
        public ProductSize createFromParcel(Parcel source) {
            return new ProductSize(source);
        }

        @Override
        public ProductSize[] newArray(int size) {
            return new ProductSize[size];
        }
    };
}
