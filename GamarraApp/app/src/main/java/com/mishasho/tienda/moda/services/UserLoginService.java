package com.mishasho.tienda.moda.services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.mishasho.tienda.moda.Util.HttpHelper;
import com.mishasho.tienda.moda.Util.RequestPackage;
import com.mishasho.tienda.moda.model.User;

import java.io.IOException;

public class UserLoginService extends IntentService {

    public static final String USER_LOGIN_POST = "userLoginPost";
    public static final String USER_LOGIN_POST_PAYLOAD = "userLoginPostPayload";
    public static final String USER_LOGIN_REQUEST_PACKAGE = "userLoginRequestPackage";

    public UserLoginService() { super("UserRegistrationService"); }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(USER_LOGIN_REQUEST_PACKAGE);

        String returnValues = null;
        try {
            returnValues = HttpHelper.downloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        User[] Users = null;
        if(returnValues.length() > 0) {
            Gson gson = new Gson();
            Users = gson.fromJson(returnValues, User[].class);
        }
        Intent myIntent = new Intent(USER_LOGIN_POST);
        myIntent.putExtra(USER_LOGIN_POST_PAYLOAD, Users);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(myIntent);

    }
}
