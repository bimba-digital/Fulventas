package com.mishasho.tienda.moda.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.model.User;

import java.util.List;

public class CustomSharedPrefs {

    public static void setFavProductsInPref(Context context, DataSource dataSource){

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREF_FAVOURITE, Context.MODE_PRIVATE).edit();
        List<Integer> favProductIds = dataSource.getAllFavProductIds();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < favProductIds.size(); i++) {
            sb.append(String.valueOf(favProductIds.get(i))).append(",");
        }

        editor.putString(Constants.SHARED_PREF_FAVOURITE_PRODUCTS, sb.toString());
        editor.commit();

    }

    public static String[] getFavProductsInPref(Context context, DataSource dataSource){
        SharedPreferences myPrefs = context.getSharedPreferences(Constants.SHARED_PREF_FAVOURITE, Context.MODE_PRIVATE);
        String favProductIds = myPrefs.getString(Constants.SHARED_PREF_FAVOURITE_PRODUCTS, "");
        String[] favProductsArrayId = favProductIds.split(",");
        return favProductsArrayId;
    }



    public static void setLoggedInUser(Context context, User user){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREF_USER, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(Constants.SHARED_PREF_LOGGEDIN_USER, json);
        editor.commit();
    }

    public static User getLoggedInUser(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SHARED_PREF_USER, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = myPref.getString(Constants.SHARED_PREF_LOGGEDIN_USER,"");
        User user = gson.fromJson(json, User.class);
        return user;
    }




    public static String getLoggedInUserId(Context context){
        return getLoggedInUser(context).getUser_id();
    }

    public static void removeLoggedInUser(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREF_USER, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }


    public static void setCurrency(Context context, String currency){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREF_CURRENCY, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SHARED_PREF_CURRENCY_IN, currency);
        editor.commit();
    }

    public static String getCurrency(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SHARED_PREF_CURRENCY, Context.MODE_PRIVATE);
        String currency = myPref.getString(Constants.SHARED_PREF_CURRENCY_IN,"");
        return currency;
    }

    public static void setCartEmpty(Context context, boolean emptyCart){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREF_EMPTY_CART, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.SHARED_PREF_EMPTY_CART_IN, emptyCart);
        editor.commit();
    }

    public static boolean getCartEmpty(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SHARED_PREF_EMPTY_CART, Context.MODE_PRIVATE);
        return myPref.getBoolean(Constants.SHARED_PREF_EMPTY_CART_IN,true);
    }

    public static void setFirebaseToken(Context context, String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREF_FCMTOKEN, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SHARED_PREF_FCMTOKEN_IN, token);
        editor.commit();
    }

    public static String getFirebaseToken(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SHARED_PREF_FCMTOKEN, Context.MODE_PRIVATE);
        return myPref.getString(Constants.SHARED_PREF_FCMTOKEN_IN,"");
    }

    public static void setProfileUrl(Context context, String profileUrl){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SHARED_PREF_PROFILEIMAGE, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SHARED_PREF_PROFILEIMAGE_IN, profileUrl);
        editor.commit();
    }

    public static String getProfileUrl(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SHARED_PREF_PROFILEIMAGE, Context.MODE_PRIVATE);
        return myPref.getString(Constants.SHARED_PREF_PROFILEIMAGE_IN,"");
    }

}
