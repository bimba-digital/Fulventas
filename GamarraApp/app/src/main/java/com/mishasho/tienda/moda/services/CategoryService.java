package com.mishasho.tienda.moda.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.mishasho.tienda.moda.Util.HttpHelper;
import com.mishasho.tienda.moda.model.ProductCategory;

import java.io.IOException;

public class CategoryService extends IntentService{

    public static final String CATEGORY_MESSAGE = "categoryMessage";
    public static final String CATEGORY_PAYLOAD = "categoryPayload";


    public CategoryService() {
        super("CategoryService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Uri url = intent.getData();
        String returnValues = null;
        try {
            returnValues = HttpHelper.downloadUrl(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ProductCategory[] ProductCategories = gson.fromJson(returnValues, ProductCategory[].class);

        Intent myIntent = new Intent(CATEGORY_MESSAGE);
        myIntent.putExtra(CATEGORY_PAYLOAD, ProductCategories);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(myIntent);

    }
}
