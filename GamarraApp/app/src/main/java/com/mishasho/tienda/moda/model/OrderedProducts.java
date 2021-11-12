package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderedProducts implements Parcelable {

    int product_id;
    String product_title;
    String product_color;
    String product_size;
    int ordered_quantity;


    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public int getOrdered_quantity() {
        return ordered_quantity;
    }

    public void setOrdered_quantity(int ordered_quantity) {
        this.ordered_quantity = ordered_quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.product_id);
        dest.writeString(this.product_title);
        dest.writeString(this.product_color);
        dest.writeString(this.product_size);
        dest.writeInt(this.ordered_quantity);
    }

    public OrderedProducts() {
    }

    protected OrderedProducts(Parcel in) {
        this.product_id = in.readInt();
        this.product_title = in.readString();
        this.product_color = in.readString();
        this.product_size = in.readString();
        this.ordered_quantity = in.readInt();
    }

    public static final Parcelable.Creator<OrderedProducts> CREATOR = new Parcelable.Creator<OrderedProducts>() {
        @Override
        public OrderedProducts createFromParcel(Parcel source) {
            return new OrderedProducts(source);
        }

        @Override
        public OrderedProducts[] newArray(int size) {
            return new OrderedProducts[size];
        }
    };
}
