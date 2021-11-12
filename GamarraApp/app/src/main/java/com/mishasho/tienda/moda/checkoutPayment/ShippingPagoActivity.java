package com.mishasho.tienda.moda.checkoutPayment;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.Admob;
import com.mishasho.tienda.moda.model.User;

import java.util.HashMap;
import java.util.Map;

public class ShippingPagoActivity extends AppCompatActivity
        implements View.OnClickListener {

    androidx.appcompat.widget.Toolbar toolbar;


    RelativeLayout pbarContainer;
    public User userShipping;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UtilityClass.cartEmptyRedirect(ShippingPagoActivity.this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        UtilityClass.cartEmptyRedirect(ShippingPagoActivity.this);
        return true;
    }

    EditText etFirstName, etLastName, etAddress1, etAddress2, etCity, etZip, etState, etCountry,etDistrict,etProvince,etBusinessname,etMobile,etEmail;
    String firstName, lastName, address1, address2, city, zip, state, country,district,province,business,mobile,email;
    FrameLayout btnCompletePagoAddress;
    boolean isFieldEmpty = false;
    String totalprice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_pago);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_shipping_pago));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        pbarContainer = findViewById(R.id.pbar_container);
        pbarContainer.setVisibility(View.GONE);

        // ADDMOB
        gettingAdmob();


        totalprice = getIntent().getStringExtra(Constants.TOTAL_PRICE);
        userShipping = UtilityClass.jsonToUser(getIntent().getStringExtra(Constants.CONFIRM_PAYMENT_INTENT));

        UtilityClass.buttonScaleIconRight(this, findViewById(R.id.btn_complete_paddress_inner), R.drawable.ic_arrow_right_white, .8, 1.1);
        etFirstName = findViewById(R.id.et_shipping_pfirst_name);
        etLastName = findViewById(R.id.et_shipping_plast_name);
        etAddress1 = findViewById(R.id.et_shipping_paddress_1);
        etAddress2 = findViewById(R.id.et_shipping_paddress_2);
        etCity = findViewById(R.id.et_shipping_pcity);
        etZip = findViewById(R.id.et_shipping_pzip);
        etState = findViewById(R.id.et_shipping_pstate);
        etCountry = findViewById(R.id.et_shipping_pcountry);

        etDistrict = findViewById(R.id.et_shipping_pdistrict);
        etProvince = findViewById(R.id.et_shipping_pprovince);
        etBusinessname = findViewById(R.id.et_shipping_pbusinessname);
        etMobile = findViewById(R.id.et_shipping_pmobile);
        etEmail = findViewById(R.id.et_shipping_pemail);

        btnCompletePagoAddress = findViewById(R.id.btn_complete_paddress);
        btnCompletePagoAddress.setOnClickListener(this);

       /* User loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
        etFirstName.setText(loggedInUser.getFirst_name());
        etLastName.setText(loggedInUser.getLast_name());*/
       /* if (CustomSharedPrefs.getLoggedInUser(this) != null) {

            pbarContainer.setVisibility(View.VISIBLE);
            String JSON_URL = Constants.GET_ADDRESS + CustomSharedPrefs.getLoggedInUserId(ShippingPagoActivity.this);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.trim().equals("")){

                        Address gsonObj = new Gson().fromJson(response, Address.class);
                        etAddress1.setText(gsonObj.getAddress_line_1());
                        if(gsonObj.getAddress_line_2().equals("-1")) gsonObj.setAddress_line_2("");
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

        }*/
    }


    private void settingAdmob(Admob admob) {
        View adContainer = findViewById(R.id.adMobView);
        AdView mAdView = new AdView(ShippingPagoActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        MobileAds.initialize(ShippingPagoActivity.this, admob.getApp_id());
        mAdView.setAdUnitId(admob.getAdd_unit_id());
        ((RelativeLayout) adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void gettingAdmob() {

        String JSON_URL = Constants.GET_ADMOB;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Admob admob = new Gson().fromJson(response, Admob.class);
                settingAdmob(admob);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.getCache().clear();
        queue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        UtilityClass.cartEmptyRedirect(ShippingPagoActivity.this);
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
        city = etCity.getText().toString().trim();
        zip = etZip.getText().toString().trim();
        state = etState.getText().toString().trim();
        country = etCountry.getText().toString().trim();

        district = etDistrict.getText().toString().trim();
        province = etProvince.getText().toString().trim();
        business = etBusinessname.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        email = etEmail.getText().toString().trim();


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

        if (isEmpty(district)) {
            isFieldEmpty = true;
            etDistrict.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etDistrict.setBackgroundResource(R.drawable.edittext_round);
        }

        if (isEmpty(province)) {
            isFieldEmpty = true;
            etProvince.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etProvince.setBackgroundResource(R.drawable.edittext_round);
        }

        if (isEmpty(business)) {
            isFieldEmpty = true;
            etBusinessname.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etBusinessname.setBackgroundResource(R.drawable.edittext_round);
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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_complete_paddress:
                validateAddress();

                if (!isFieldEmpty) {

                    final User user = new User();
                    user.setFirst_name(firstName);
                    user.setLast_name(lastName);
                    final String Address = address1 + address2 + ", Distrito : " + city + ", Codigo Postal : " + zip
                            + ", Provincia : " + state + ", Páis :" + country + ", Districto :" + district  + ", Provincia :" + province;

                    if(isEmpty(address2)) address2 = "-1";
                    user.setAddress(Address);

                    updateAddress();

                    Intent intentUser = new Intent(ShippingPagoActivity.this, ConfirmActivity.class);
                    intentUser.putExtra(Constants.CONFIRM_PAYMENT_INTENT2, UtilityClass.userToJson(user));
                    intentUser.putExtra(Constants.CONFIRM_PAYMENT_INTENT, UtilityClass.userToJson(userShipping));
                    intentUser.putExtra(Constants.TOTAL_PRICE, totalprice);
                    startActivity(intentUser);

                }
                break;
        }
    }

    private void updateAddress(){
        String JSON_URL = Constants.INSERT_PAYMENT_ADDRESS;
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

                params.put("user_id", CustomSharedPrefs.getLoggedInUserId(ShippingPagoActivity.this));
                params.put("paddress_line_1", address1);
                params.put("paddress_line_2", address2);
                params.put("pcity", city);
                params.put("pzip_code", zip);
                params.put("pstate", state);
                params.put("pcountry", country);

                params.put("pdistrict", district);
                params.put("pprovince", province);
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("business_name", business);
                params.put("mobile", mobile);
                params.put("email", email);

                return params;

            }
        };

        queue.getCache().clear();
        queue.add(stringRequest);
    }


}
