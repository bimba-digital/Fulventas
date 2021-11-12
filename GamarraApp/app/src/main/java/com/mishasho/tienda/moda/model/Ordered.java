package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ordered implements Parcelable {

    int order_id;
    double order_price;
    String order_method;
    String order_status;
    String order_time;
    ArrayList<OrderedProduct> orderedProduct;
    String generated_order_id;


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public String getOrder_method() {
        return order_method;
    }

    public void setOrder_method(String order_method) {
        this.order_method = order_method;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public ArrayList<OrderedProduct> getOrderedProduct() {
        return orderedProduct;
    }

    public void setOrderedProduct(ArrayList<OrderedProduct> orderedProduct) {
        this.orderedProduct = orderedProduct;
    }

    public String getGenerated_order_id() {
        return generated_order_id;
    }

    public void setGenerated_order_id(String generated_order_id) {
        this.generated_order_id = generated_order_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.order_id);
        dest.writeDouble(this.order_price);
        dest.writeString(this.order_method);
        dest.writeString(this.order_status);
        dest.writeString(this.order_time);
        dest.writeTypedList(this.orderedProduct);
        dest.writeString(this.generated_order_id);
    }

    public Ordered() {
    }

    protected Ordered(Parcel in) {
        this.order_id = in.readInt();
        this.order_price = in.readDouble();
        this.order_method = in.readString();
        this.order_status = in.readString();
        this.order_time = in.readString();
        this.orderedProduct = in.createTypedArrayList(OrderedProduct.CREATOR);
        this.generated_order_id = in.readString();
    }

    public static final Creator<Ordered> CREATOR = new Creator<Ordered>() {
        @Override
        public Ordered createFromParcel(Parcel source) {
            return new Ordered(source);
        }

        @Override
        public Ordered[] newArray(int size) {
            return new Ordered[size];
        }
    };
}

