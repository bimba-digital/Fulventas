package com.mishasho.tienda.moda.checkoutPayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mishasho.tienda.moda.Culqi.Card;
import com.mishasho.tienda.moda.Culqi.Token;
import com.mishasho.tienda.moda.Culqi.TokenCallback;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Validation.Validation;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.model.Culqui;
import com.mishasho.tienda.moda.model.SelectedProduct;
import com.mishasho.tienda.moda.model.Types;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CulquiActivity extends AppCompatActivity {

    Validation validation;
    ArrayList<Types> listatipes =  new ArrayList<>();
    ProgressDialog progress;
    String totalPrice = "";
    TextView txtcardnumber, txtcvv, txtmonth, txtyear, txtemail, kind_card, result;
    FrameLayout btnCompletePagoAddress;
    DataSource dataSource;

    public ArrayList<Culqui> culquiList;
    String apiKeyCulqui = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culqui);

        dataSource = new DataSource(this);
        obtnerApiKeyCulqui();

        validation = new Validation();
        totalPrice = getIntent().getStringExtra(Constants.TOTAL_PRICE);
        progress = new ProgressDialog(this);
        progress.setMessage("Validando informacion de la tarjeta");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        txtcardnumber = (TextView) findViewById(R.id.txt_cardnumber);

        txtcvv = (TextView) findViewById(R.id.txt_cvv);

        txtmonth = (TextView) findViewById(R.id.txt_month);

        txtyear = (TextView) findViewById(R.id.txt_year);

        txtemail = (TextView) findViewById(R.id.txt_email);

        kind_card = (TextView) findViewById(R.id.kind_card);

        result = (TextView) findViewById(R.id.token_id);

        btnCompletePagoAddress = findViewById(R.id.btn_pay);



        txtcvv.setEnabled(false);

        txtcardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    txtcvv.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtcardnumber.getText().toString();
                if(s.length() == 0) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                if(validation.luhn(text)) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_sucess);
                } else {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                int cvv = validation.bin(text, kind_card);
                if(cvv > 0) {
                    txtcvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cvv)});
                    txtcvv.setEnabled(true);
                } else {
                    txtcvv.setEnabled(false);
                    txtcvv.setText("");
                }
            }
        });

        txtyear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtyear.getText().toString();
                if(validation.year(text)){
                    txtyear.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtyear.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        txtmonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtmonth.getText().toString();
                if(validation.month(text)){
                    txtmonth.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtmonth.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        btnCompletePagoAddress.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            progress.show();
            Card card = new Card(txtcardnumber.getText().toString(), txtcvv.getText().toString(), 9, 2025, txtemail.getText().toString());

//            Token token = new Token("sk_test_Z8J42wIX9dTbBBs8");
            Token token = new Token("pk_live_GHX1MzU8Wp1gK94o");

            token.createToken(getApplicationContext(), card, new TokenCallback() {
                @Override
                public void onSuccess(JSONObject token) {
                    try {
                        result.setText(token.get("id").toString());
                       confirmarCulqui(token.get("id").toString(),totalPrice,txtemail.getText().toString());

                    } catch (Exception ex){
                        progress.hide();
                    }
                    progress.hide();
                }

                @Override
                public void onError(Exception error) {
                    progress.hide();
                    Toast.makeText(getApplicationContext(),"error de envio" , Toast.LENGTH_LONG).show();
                }
            });

            }
        });

    }

    private void obtnerApiKeyCulqui() {

        String JSON_URL = Constants.APIKEY_CULQUI;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseV", response);
                Type listType = new TypeToken<List<Culqui>>() {
                }.getType();
                culquiList = new Gson().fromJson(response, listType);
                for (Culqui tmp : culquiList) {
                    apiKeyCulqui = tmp.getCulqi_authorization();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void confirmarCulqui(final String apiKey,final String monto,final String email) {
        String JSON_URL = Constants.INSERT_CULQUI + "?correo=" + email + "&token=" + apiKey + "&monto=" + monto;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseV", response);
                Types t = new Gson().fromJson(response, Types.class);
                //Toast.makeText(getApplicationContext(),t.getM(), Toast.LENGTH_LONG).show();
                if(t.getP().equals("1")){
                    Intent intent = new Intent(CulquiActivity.this, OpinionActivity.class);
                    intent.putExtra("msn",t.getM());
                    startActivity(intent);
                    dopayment("CULQI");
                }else {
                    openDialog(t.getM());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("responseV", error.getMessage());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000,  DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.getCache().clear();
        queue.add(stringRequest);
    }
    public void openDialog(String value)
    {
        CulqiDialog culqiDialog = CulqiDialog.newInstancia(value);
        culqiDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void dopayment(final String pay_ment_method) {
       // pbarContainer.setVisibility(View.VISIBLE);
        String JSON_URL = Constants.PATH_TO_PAYMENT;
        RequestQueue queueOrder = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                final String responsed_order_id = response.toString().trim();
                ArrayList<SelectedProduct> cartModels = (ArrayList<SelectedProduct>) dataSource.getAllCartProducts();

                for (final SelectedProduct cartModel : cartModels) {

                    final SelectedProduct currentCartModel = cartModel;

                    String JSON_URL = Constants.PATH_TO_PAYMENT_COMPLETE;
                    RequestQueue queue = Volley.newRequestQueue(CulquiActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            dataSource.removeAllCartProducts();
                           // pbarContainer.setVisibility(View.GONE);
                            CustomSharedPrefs.setCartEmpty(CulquiActivity.this, true);
                         //  Intent intent = new Intent(CulquiActivity.this, OpinionActivity.class);
                         //  startActivity(intent);
                          /*  Intent intent = new Intent(ConfirmActivity.this, UserOrderActivity.class);
                            startActivity(intent);*/
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

                            params.put("product_id", cartModel.getId());
                            params.put("order_id", responsed_order_id);
                            params.put("ordered_quantity", String.valueOf(cartModel.getQunatity()));
                            params.put("product_size_id", String.valueOf(cartModel.getProductSize().getSize_id()));
                            params.put("product_color_id", String.valueOf(cartModel.getProductColor().getColor_id()));
                            params.put("inventory_id", String.valueOf(cartModel.getInvetory_id()));

                            return params;

                        }
                    };
                    queue.getCache().clear();
                    queue.add(stringRequest);
                }
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
                params.put("order_method", pay_ment_method);
                params.put("order_amount", totalPrice);
                params.put("order_user_id", CustomSharedPrefs.getLoggedInUser(CulquiActivity.this).getUser_id());

                return params;
            }
        };
        queueOrder.getCache().clear();
        queueOrder.add(stringRequest);
    }

}
