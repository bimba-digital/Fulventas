package com.mishasho.tienda.moda.services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mishasho.tienda.moda.Util.HttpHelper;
import com.mishasho.tienda.moda.Util.RequestPackage;

import java.io.IOException;

public class UserRegistrationService  extends IntentService {

    public static final String USER_REGISTRATION_POST = "userRegistrationPost";
    public static final String USER_REGISTRATION_POST_PAYLOAD = "userRegistrationPostPayload";
    public static final String USER_REGISTRATION_REQUEST_PACKAGE = "userRegistrationRequestPackage";

    public UserRegistrationService() { super("UserRegistrationService"); }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(USER_REGISTRATION_REQUEST_PACKAGE);

        String returnValues = null;
        try {
            returnValues = HttpHelper.downloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* Gson gson = new Gson();
        String[] successMessage = gson.fromJson(returnValues, String[].class);*/

        Intent myIntent = new Intent(USER_REGISTRATION_POST);
        myIntent.putExtra(USER_REGISTRATION_POST_PAYLOAD, returnValues);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(myIntent);

    }
}
