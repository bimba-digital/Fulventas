package com.mishasho.tienda.moda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String user_id;
    private String first_name;
    private String last_name;
    private String image_name;
    private String username;
    private String email;
    private String password;
    private String address;
    private String user_number;
    private String membership;
    private String store;
    private String gallery;
    /* add*/
    private String department;
    private String district;
    private String province;
    private String mobile;
    private String useraddress;

    private String dni;
    private String ruc;
    private String razonsocial;
    private String telefono;
    private String nombretienda;
    private String galeria;
    private String direcciontienda;
    private String numstand;




    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }


    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }


    /*add*/
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombretienda() {
        return nombretienda;
    }

    public void setNombretienda(String nombretienda) {
        this.nombretienda = nombretienda;
    }

    public String getGaleria() {
        return galeria;
    }

    public void setGaleria(String galeria) {
        this.galeria = galeria;
    }

    public String getDirecciontienda() {
        return direcciontienda;
    }

    public void setDirecciontienda(String direcciontienda) {
        this.direcciontienda = direcciontienda;
    }

    public String getNumstand() {
        return numstand;
    }

    public void setNumstand(String numstand) {
        this.numstand = numstand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.image_name);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.address);
        dest.writeString(this.user_number);
        dest.writeString(this.membership);

        dest.writeString(this.store);
        dest.writeString(this.gallery);

        /*add*/
        dest.writeString(this.department);
        dest.writeString(this.district);
        dest.writeString(this.province);
        dest.writeString(this.mobile);
        dest.writeString(this.useraddress);

        dest.writeString(this.dni);
        dest.writeString(this.ruc);
        dest.writeString(this.razonsocial);
        dest.writeString(this.telefono);
        dest.writeString(this.nombretienda);
        dest.writeString(this.galeria);
        dest.writeString(this.direcciontienda);
        dest.writeString(this.numstand);

    }

    public User() {
    }

    protected User(Parcel in) {
        this.user_id = in.readString();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.image_name = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.address = in.readString();
        this.user_number = in.readString();
        this.membership = in.readString();

        this.store = in.readString();
        this.gallery = in.readString();

        /*add*/
        this.department = in.readString();
        this.district = in.readString();
        this.province = in.readString();
        this.mobile = in.readString();
        this.useraddress = in.readString();

        this.dni = in.readString();
        this.ruc = in.readString();
        this.razonsocial = in.readString();
        this.telefono = in.readString();
        this.nombretienda = in.readString();
        this.galeria = in.readString();
        this.direcciontienda = in.readString();
        this.numstand = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
