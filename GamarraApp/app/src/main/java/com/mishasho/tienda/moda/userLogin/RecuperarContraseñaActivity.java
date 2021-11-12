package com.mishasho.tienda.moda.userLogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.model.Types;

import java.util.ArrayList;

public class RecuperarContraseñaActivity extends ActivityBaseCartIcon
        implements View.OnClickListener {

    // SPONSORED PRODUCTS GRID



    /*START OF MENU VERIABLE*/
    Toolbar toolbar;
    Button btnAddCart;
    /*END OF MENU VERIABLE*/

    /*ENCABEZADO */
    TextView type_nombre;
    ArrayList<Types> typesList;
    RecyclerView recyclerViewTypeCategoria;
    private Types typesItem;
    String hombre_title,mujer_title,ninho_title,bebe_title;
    String hombre_id,mujer_id,ninho_id,bebe_id;
    TextView tvValidationLoginEmail;
    EditText etEmail;
    TextView tvLoginMsg;
    private boolean networkOK;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenha);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_recuperar_contraseña));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        //type_nombre.setText(typesItem.getTitle());
        //getTypeCategory();

        etEmail =  findViewById(R.id.et_recuperar_email);
        tvValidationLoginEmail = findViewById(R.id.tv_validation_login_msg_email);
        tvLoginMsg = findViewById(R.id.tv_login_msg);
        Button btnRecuperarContraseña = findViewById(R.id.btn_recuperar_registration);
        btnRecuperarContraseña.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
       switch (v.getId()) {
            case R.id.btn_recuperar_registration:

                String email = etEmail.getText().toString().trim();
                boolean isFieldEmpty = validateField(email);
                if (!isFieldEmpty) {
                    networkOK = NetworkHelper.hasNetworkAccess(RecuperarContraseñaActivity.this);
                    if (networkOK) loginWithEmail(email);
                    else Toast.makeText(RecuperarContraseñaActivity.this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // settingNavDrawer();
        invalidateOptionsMenu();
    }

    private void loginWithEmail(final String email) {
        String JSON_URL = Constants.RECUPERAR_CONTRASEÑA_URL +email ;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Types t = new Gson().fromJson(response, Types.class);
                   loginResponse(t.getM());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);


    }

    // creo una funcionn para mostrar
    private void showdialogo() {

        new AlertDialog.Builder(this)
                .setTitle( "Recuperación de Contraseña")
                .setMessage( "La Contraseña se envió a su correo revise su correo dentro de 5 minutos.")
                // creamos el boton ok en caso presione, se ejecuta el bloque de codigo
               .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intentCategory = new Intent(RecuperarContraseñaActivity.this, LoginActivity.class);
                        startActivity(intentCategory);
                    }
                })
                .show();
    }

    private void loginResponse(String response) {
        tvLoginMsg.setVisibility(View.VISIBLE);
        if (response.equals("1")) {
//            Intent intentCategory = new Intent(RecuperarContraseñaActivity.this, LoginActivity.class);
//            startActivity(intentCategory);
            showdialogo();
            Toast.makeText(getApplicationContext(), "Email enviado", Toast.LENGTH_LONG).show();
        } else tvLoginMsg.setText("Invalid email/password.");
    }
    private boolean validateField(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        boolean isFieldEmpty = false;

        if (isEmpty(email)) {
            isFieldEmpty = true;
            tvValidationLoginEmail.setVisibility(View.VISIBLE);
            tvValidationLoginEmail.setText("El correo electrónico no puede estar vacío.");
        } else if (!email.matches(emailPattern) && !isEmpty(email)) {
            isFieldEmpty = true;
            tvValidationLoginEmail.setVisibility(View.VISIBLE);
            tvValidationLoginEmail.setText("Email inválido.");
        } else {
            tvValidationLoginEmail.setVisibility(View.GONE);
            tvValidationLoginEmail.setText("");
        }
        return isFieldEmpty;
    }
    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }

}
