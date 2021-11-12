package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderedProduct implements Parcelable {

    private int ordered_id;
    private int order_id;
    private int product_id;
    private double product_price;
    private String product_title;
    private String product_color;
    private String product_size;
    private int ordered_quantity;
    private String product_image;

    public int getOrdered_id() {
        return ordered_id;
    }

    public void setOrdered_id(int ordered_id) {
        this.ordered_id = ordered_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
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

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ordered_id);
        dest.writeInt(this.order_id);
        dest.writeInt(this.product_id);
        dest.writeDouble(this.product_price);
        dest.writeString(this.product_title);
        dest.writeString(this.product_color);
        dest.writeString(this.product_size);
        dest.writeInt(this.ordered_quantity);
        dest.writeString(this.product_image);
    }

    public OrderedProduct() {
    }

    protected OrderedProduct(Parcel in) {
        this.ordered_id = in.readInt();
        this.order_id = in.readInt();
        this.product_id = in.readInt();
        this.product_price = in.readDouble();
        this.product_title = in.readString();
        this.product_color = in.readString();
        this.product_size = in.readString();
        this.ordered_quantity = in.readInt();
        this.product_image = in.readString();
    }

    public static final Creator<OrderedProduct> CREATOR = new Creator<OrderedProduct>() {
        @Override
        public OrderedProduct createFromParcel(Parcel source) {
            return new OrderedProduct(source);
        }

        @Override
        public OrderedProduct[] newArray(int size) {
            return new OrderedProduct[size];
        }
    };
}

