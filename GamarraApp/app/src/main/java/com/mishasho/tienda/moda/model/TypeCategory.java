package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeCategory implements Parcelable {

    private int id;
    private String title;
    private int sort;
    private String image_name;
    private  int types;
    private  int status;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setId(int id) {
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

    public void setTypes(int types) {
        this.types = types;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getId() {
        return id;
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

    public int getTypes() {
        return types;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.sort);
        dest.writeString(this.image_name);
        dest.writeInt(this.types);
        dest.writeInt(this.status);
    }

    public TypeCategory() {
    }

    protected TypeCategory(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.sort = in.readInt();
        this.image_name = in.readString();
        this.types = in.readInt();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<TypeCategory> CREATOR = new Parcelable.Creator<TypeCategory>() {
        @Override
        public TypeCategory createFromParcel(Parcel source) {
            return new TypeCategory(source);
        }

        @Override
        public TypeCategory[] newArray(int size) {
            return new TypeCategory[size];
        }
    };
}


