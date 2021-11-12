package com.mishasho.tienda.moda.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.mishasho.tienda.moda.Util.HttpHelper;
import com.mishasho.tienda.moda.Util.RequestPackage;
import com.mishasho.tienda.moda.model.ProductImages;

import java.io.IOException;

public class ProductImagesService extends IntentService{

    public static final String PRODUCT_IMAGES_MESSAGE = "productImagesMessage";
    public static final String PRODUCT_IMAGES_PAYLOAD = "productImagesPayload";

    public static final String PRODUCT_IMAGES_REQUEST_PACKAGE = "productImagesRequestPackage";


    public ProductImagesService() {
        super("ProductImagesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(PRODUCT_IMAGES_REQUEST_PACKAGE);
        String returnValues = null;

        try {
            returnValues = HttpHelper.downloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new Gson();
        ProductImages[] productImages = gson.fromJson(returnValues, ProductImages[].class);

        Intent myIntent = new Intent(PRODUCT_IMAGES_MESSAGE);
        myIntent.putExtra(PRODUCT_IMAGES_PAYLOAD, productImages);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(myIntent);

    }
}
