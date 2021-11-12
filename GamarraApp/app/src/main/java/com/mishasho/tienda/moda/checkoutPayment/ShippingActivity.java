package com.mishasho.tienda.moda.checkoutPayment;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.Address;
import com.mishasho.tienda.moda.model.User;

import java.util.HashMap;
import java.util.Map;

public class ShippingActivity extends AppCompatActivity
        implements View.OnClickListener {

    androidx.appcompat.widget.Toolbar toolbar;


    RelativeLayout pbarContainer;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UtilityClass.cartEmptyRedirect(ShippingActivity.this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        UtilityClass.cartEmptyRedirect(ShippingActivity.this);
        return true;
    }

    EditText etFirstName, etLastName, etAddress1, etAddress2, etZip, etState, etCountry,etMobile,etEmail,etReception_name;
    String firstName, lastName, address1, address2, city, zip, state, country,mobile,email,reception_name;
    Spinner etCity;
    FrameLayout btnCompleteAddress;
    boolean isFieldEmpty = false, isDistrito = false;
    String totalprice = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_shipping));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        pbarContainer = findViewById(R.id.pbar_container);
        pbarContainer.setVisibility(View.GONE);

        // ADDMOB
        //gettingAdmob();
        totalprice = getIntent().getStringExtra(Constants.TOTAL_PRICE);

        UtilityClass.buttonScaleIconRight(this, findViewById(R.id.btn_complete_address_inner), R.drawable.ic_arrow_right_white, .8, 1.1);

        etFirstName = findViewById(R.id.et_shipping_first_name);
        etLastName = findViewById(R.id.et_shipping_last_name);
        etAddress1 = findViewById(R.id.et_shipping_address_1);
        etAddress2 = findViewById(R.id.et_shipping_address_2);
        etCity = findViewById(R.id.et_shipping_city);
        etZip = findViewById(R.id.et_shipping_provincia);
        etState = findViewById(R.id.et_shipping_state);
        etCountry = findViewById(R.id.et_shipping_country);

        etMobile = findViewById(R.id.et_shipping_pmobile);
        etEmail = findViewById(R.id.et_shipping_pemail);

        etReception_name = findViewById(R.id.et_recepcion_name);

        btnCompleteAddress = findViewById(R.id.btn_complete_address);
        btnCompleteAddress.setOnClickListener(this);

        User loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
        etFirstName.setText(loggedInUser.getFirst_name());
        etLastName.setText(loggedInUser.getLast_name());
        etEmail.setText(loggedInUser.getEmail());
        etMobile.setText(loggedInUser.getMobile());
        if (CustomSharedPrefs.getLoggedInUser(this) != null) {

            pbarContainer.setVisibility(View.VISIBLE);
            String JSON_URL = Constants.GET_ADDRESS + CustomSharedPrefs.getLoggedInUserId(ShippingActivity.this);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.trim().equals("")){

                        Address gsonObj = new Gson().fromJson(response, Address.class);
                        etAddress1.setText(gsonObj.getAddress_line_1());
                        if(gsonObj.getAddress_line_2().equals("-1")) gsonObj.setAddress_line_2("");
                        etAddress2.setText(gsonObj.getAddress_line_2());
                       // etCity.setText(gsonObj.getCity());
                       // etZip.setText(gsonObj.getProvince());
                      //  etState.setText(gsonObj.getState());
                      // etCountry.setText(gsonObj.getCountry());

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
    }




    @Override
    protected void onResume() {
        UtilityClass.cartEmptyRedirect(ShippingActivity.this);
        super.onResume();
    }

    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }

    private void validateAddress() {

        firstName = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        address1 = etAddress1.getText().toString().trim();
        address2 = etAddress2.getText().toString().trim();
        city = etCity.getSelectedItem().toString().trim();
        zip = etZip.getText().toString().trim();
        state = etState.getText().toString().trim();
        country = etCountry.getText().toString().trim();

        mobile = etMobile.getText().toString().trim();
        email = etEmail.getText().toString().trim();
       // Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
        reception_name = etReception_name.getText().toString().trim();

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
        if (isEmpty(city) || city.indexOf("leccione") > 0) {
            isFieldEmpty = true;
            isDistrito = true;
            etCity.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            isDistrito = false;
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
        if (isEmpty(mobile)) {
            isFieldEmpty = true;
            etMobile.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etMobile.setBackgroundResource(R.drawable.edittext_round);
        }

        if (isEmpty(email)) {
            isFieldEmpty = true;
            etEmail.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etEmail.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(reception_name)) {
            isFieldEmpty = true;
            etReception_name.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etReception_name.setBackgroundResource(R.drawable.edittext_round);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_complete_address:
                validateAddress();

                Log.d("ppp", city + " "  +isDistrito);
               if (isDistrito) {
                   Toast.makeText(getApplicationContext(), "Debes de seleccionar un distrito", Toast.LENGTH_LONG).show();
               } else {

                   if (!isFieldEmpty) {

                       final User user = new User();
                       user.setFirst_name(firstName);
                       user.setLast_name(lastName);
                       final String Address = address1 + address2 + ", Distrito : " + city + ", Codigo Postal : " + zip
                               + ", Provincia : " + state + ", Páis :" + country;

                       if (isEmpty(address2)) address2 = "-1";
                       user.setAddress(Address);

                       updateAddress();

                       Intent intentUser = new Intent(ShippingActivity.this, CulquiActivity.class);
                       //  intentUser.putExtra(Constants.CONFIRM_PAYMENT_INTENT, UtilityClass.userToJson(user));
                       intentUser.putExtra(Constants.TOTAL_PRICE, totalprice);
                       startActivity(intentUser);
                   /* Intent intentUser = new Intent(ShippingActivity.this, ShippingPagoActivity.class);
                    intentUser.putExtra(Constants.CONFIRM_PAYMENT_INTENT, UtilityClass.userToJson(user));
                    intentUser.putExtra(Constants.TOTAL_PRICE, totalprice);
                    startActivity(intentUser);*/

                   }
               }
                break;
        }
    }

    private void updateAddress(){
        String JSON_URL = Constants.INSERT_ADDRESS;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


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

                params.put("user_id", CustomSharedPrefs.getLoggedInUserId(ShippingActivity.this));
                params.put("address_line_1", address1);
                params.put("address_line_2", address2);
                params.put("city", city);
                params.put("province", zip);
                params.put("state", state);
                params.put("country", country);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("reception_name", reception_name);


                return params;

            }
        };

        queue.getCache().clear();
        queue.add(stringRequest);
    }


}
