package com.mishasho.tienda.moda.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("address_id")
    private int address_id;
    @SerializedName("address_line_1")
    private String address_line_1;
    @SerializedName("address_line_2")
    private String address_line_2;
    @SerializedName("city")
    private String city;
    @SerializedName("zip_code")
    private String zip_code;
    @SerializedName("province")
    private String province;
    @SerializedName("state")
    private String state;
    @SerializedName("country")
    private String country;

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
