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
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.userProfile.UserOrderActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpinionActivity extends AppCompatActivity
        implements View.OnClickListener {

    androidx.appcompat.widget.Toolbar toolbar;
    String mensaje;

    RelativeLayout pbarContainer;
    public User userShipping;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UtilityClass.cartEmptyRedirect(OpinionActivity.this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        UtilityClass.cartEmptyRedirect(OpinionActivity.this);
        return true;
    }

    EditText etEmail, etSubject, etSuggestion;
    String email, subject, suggestion, fecha;
    FrameLayout btnCompleteEnviar;
    boolean isFieldEmpty = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        openDialog();
        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_opinion));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        pbarContainer = findViewById(R.id.pbar_container);
        pbarContainer.setVisibility(View.GONE);

       // userShipping = UtilityClass.jsonToUser(getIntent().getStringExtra(Constants.CONFIRM_PAYMENT_INTENT));

        UtilityClass.buttonScaleIconRight(this, findViewById(R.id.btn_complete_enviar_inner), R.drawable.ic_arrow_right_white, .8, 1.1);

        etSubject = findViewById(R.id.et_opinion_subject);
        etSubject.setText("SUGERENCIAS");
        etSuggestion = findViewById(R.id.et_opinion_suggestion);
        etEmail = findViewById(R.id.et_opinion_email);

        btnCompleteEnviar = findViewById(R.id.btn_complete_enviar);
        btnCompleteEnviar.setOnClickListener(this);

       User loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
       if (loggedInUser.getEmail()!=null){
           etEmail.setText(loggedInUser.getEmail());
       }

        fechaActual();
    }



    @Override
    protected void onResume() {
        UtilityClass.cartEmptyRedirect(OpinionActivity.this);
        super.onResume();
    }

    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }

    private void validateAddress() {

        subject = etSubject.getText().toString().trim();
        suggestion = etSuggestion.getText().toString().trim();
        email = etEmail.getText().toString().trim();

        if (isEmpty(subject)) {
            isFieldEmpty = true;
            etSubject.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etSubject.setBackgroundResource(R.drawable.edittext_round);
        }
        if (isEmpty(suggestion)) {
            isFieldEmpty = true;
            etSuggestion.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etSuggestion.setBackgroundResource(R.drawable.edittext_round);
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
            case R.id.btn_complete_enviar:
                validateAddress();

                if (!isFieldEmpty) {
                    updateAddress();
                    Intent intent = new Intent(OpinionActivity.this, UserOrderActivity.class);
                    startActivity(intent);


                }
                break;
        }
    }

    private void updateAddress(){
        String JSON_URL = Constants.INSERT_OPINIONS;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(getApplicationContext(), "Enviado" , Toast.LENGTH_LONG).show();

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
                params.put("user_id", CustomSharedPrefs.getLoggedInUserId(OpinionActivity.this));
                params.put("subject", subject);
                params.put("date", fecha);
                params.put("suggestion", suggestion);
                params.put("email", email);

                return params;

            }
        };

        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void fechaActual(){
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//yyyy/MM/dd HH:mm:ss dd/MM/yyyy
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        fecha = thisDate;
    }

    public void openDialog()
    {
        Bundle extras = getIntent().getExtras();
        mensaje = extras.getString("msn");
        CulqiDialog culqiDialog = CulqiDialog.newInstancia(mensaje);
        culqiDialog.show(getSupportFragmentManager(), "example dialog");
    }

}
