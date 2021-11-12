package com.mishasho.tienda.moda.userProfile;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.model.Address;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.R;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends ActivityBaseCartIcon
        implements View.OnClickListener {

    TextView toobarTitle;
    androidx.appcompat.widget.Toolbar toolbar;
    Button btnMyOrders;
    Button btnMyFavourites;

    TextView tvUserName;
    boolean isFieldEmpty = false;
    ImageView ivProfileImage;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    User loggedInUser;
    EditText etFirstName, etLastName, etAddress1, etAddress2, etCity, etZip, etState, etCountry;
    String firstName, lastName, address1, address2, city, zip, state, country;
    FrameLayout btnCompleteAddress;
    RelativeLayout pbarContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText("User");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pbarContainer = findViewById(R.id.pbar_container);
        pbarContainer.setVisibility(View.GONE);

        ivProfileImage = findViewById(R.id.iv_profile_image);

        if(CustomSharedPrefs.getLoggedInUser(ProfileActivity.this) != null){

            if(!CustomSharedPrefs.getProfileUrl(ProfileActivity.this).equals("")){
                Picasso.get().load(CustomSharedPrefs.getProfileUrl(ProfileActivity.this)).into(ivProfileImage);
            }

            // START OF ORDER/FAVOURITE BUTTON
            btnMyOrders = findViewById(R.id.btn_my_orders);
            btnMyOrders.setOnClickListener(this);

            btnMyFavourites = findViewById(R.id.btn_my_favourite);
            btnMyFavourites.setOnClickListener(this);
            // END OF ORDER/FAVOURITE BUTTON

            loggedInUser = CustomSharedPrefs.getLoggedInUser(ProfileActivity.this);
            tvUserName = findViewById(R.id.tv_user_name);
            tvUserName.setText(loggedInUser.getFirst_name() + " -- " + loggedInUser.getLast_name());

            etFirstName = findViewById(R.id.et_shipping_first_name);
            etLastName = findViewById(R.id.et_shipping_last_name);
            etAddress1 = findViewById(R.id.et_shipping_address_1);
            etAddress2 = findViewById(R.id.et_shipping_address_2);
            etCity = findViewById(R.id.et_shipping_city);
            etZip = findViewById(R.id.et_shipping_zip);
            etState = findViewById(R.id.et_shipping_state);
            etCountry = findViewById(R.id.et_shipping_country);

            btnCompleteAddress = findViewById(R.id.btn_complete_address);
            btnCompleteAddress.setOnClickListener(this);

            User loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
            etFirstName.setText(loggedInUser.getFirst_name());
            etLastName.setText(loggedInUser.getLast_name());
            if (CustomSharedPrefs.getLoggedInUser(this) != null) settingAddress();

        }else{
            Toast.makeText(ProfileActivity.this, "Por favor inicie sesión para ver su perfil", Toast.LENGTH_LONG).show();
        }
    }


    private void settingAddress(){
        pbarContainer.setVisibility(View.VISIBLE);
        String JSON_URL = Constants.GET_ADDRESS + CustomSharedPrefs.getLoggedInUserId(ProfileActivity.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("")) {

                    Address gsonObj = new Gson().fromJson(response, Address.class);
                    etAddress1.setText(gsonObj.getAddress_line_1());
                    if (gsonObj.getAddress_line_2().equals("-1")) gsonObj.setAddress_line_2("");
                    etAddress2.setText(gsonObj.getAddress_line_2());
                    etCity.setText(gsonObj.getCity());
                    etZip.setText(gsonObj.getZip_code());
                    etState.setText(gsonObj.getState());
                    etCountry.setText(gsonObj.getCountry());

                }
                pbarContainer.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }

    private void validateAddress() {

        firstName = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        address1 = etAddress1.getText().toString().trim();
        address2 = etAddress2.getText().toString().trim();
        city = etCity.getText().toString().trim();
        zip = etZip.getText().toString().trim();
        state = etState.getText().toString().trim();
        country = etCountry.getText().toString().trim();

        if (isEmpty(firstName)) {
            isFieldEmpty = true;
            etFirstName.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etFirstName.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(lastName)) {
            isFieldEmpty = true;
            etLastName.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etLastName.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(address1)) {
            isFieldEmpty = true;
            etAddress1.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etAddress1.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(city)) {
            isFieldEmpty = true;
            etCity.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etCity.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(zip)) {
            isFieldEmpty = true;
            etZip.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etZip.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(state)) {
            isFieldEmpty = true;
            etState.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etState.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(country)) {
            isFieldEmpty = true;
            etCountry.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etCountry.setBackgroundResource(R.drawable.edittext_round);
        }
    }

    private void updateAddress() {
        String JSON_URL = Constants.INSERT_ADDRESS;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(ProfileActivity.this, "Actualizado exitosamente", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("responseV", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", CustomSharedPrefs.getLoggedInUserId(ProfileActivity.this));
                params.put("address_line_1", address1);
                params.put("address_line_2", address2);
                params.put("city", city);
                params.put("zip_code", zip);
                params.put("state", state);
                params.put("country", country);

                return params;

            }
        };

        queue.getCache().clear();
        queue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_orders:
                Intent intentOrder = new Intent(ProfileActivity.this, UserOrderActivity.class);
                startActivity(intentOrder);
                break;

            case R.id.btn_my_favourite:
                Intent intentFav = new Intent(ProfileActivity.this, UserFavActivity.class);
                startActivity(intentFav);
                break;

            case R.id.btn_complete_address:

                validateAddress();

                if (!isFieldEmpty) {

                    final User user = new User();
                    user.setFirst_name(firstName);
                    user.setLast_name(lastName);
                    final String Address = address1 + address2 + ", City : " + city + ", zip Code : " + zip
                            + ", State : " + state + ", Country :" + country;

                    if (isEmpty(address2)) address2 = "-1";
                    user.setAddress(Address);

                    updateAddress();

                }
                break;
        }
    }


}
