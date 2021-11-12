package com.mishasho.tienda.moda.userRegistration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.userLogin.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterVeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegisterLogin;
    Toolbar toolbar;
    Button btnUserRegistration, btnVerPoliticas;
    TextView tvTermsWriting;
    EditText etFirstName, etLastname, etEmail, etPassword,etRepetir, etDni, etRuc, etRanzonSocial,etTelefono,etNombreTienda,etGaleria,etdireccionTienda,etNumStand;
    TextView tvValidationEmail, tvValidationFirstName, tvValidationLastName, tvValidationPassword, tvRegMsg,tvValidationDni,tvValidationRuc, tvValidationRanzonSocial,tvValidationTelefono,tvValidationNombreTienda,tvValidationGaleria,tvValidationdireccionTienda,tvValidationNumStand;
    boolean fromCart = false;

    CheckBox checkBoxPoliticas;

    private boolean networkOK;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ve);

        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

//        toobarTitle.setText(this.getString(R.string.title_register));
        toobarTitle.setText(getString(R.string.register_vendor_main_btn));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (CustomSharedPrefs.getLoggedInUser(RegisterVeActivity.this) == null) {

            btnUserRegistration = findViewById(R.id.btn_user_registration);
            btnVerPoliticas = findViewById(R.id.btnVerPoliticas);

            btnUserRegistration.setOnClickListener(this);
            btnVerPoliticas.setOnClickListener(this);

            tvTermsWriting = findViewById(R.id.tv_terms_writing);
            etFirstName = findViewById(R.id.et_register_first_name);
            etLastname = findViewById(R.id.et_register_last_name);
//            etEmail = findViewById(R.id.et_register_email);
            etPassword = findViewById(R.id.et_register_password);
//            etRepetir= findViewById(R.id.et_repetir_password);
            /** add**/
//            etDni = findViewById(R.id.et_register_dni);
            etRuc = findViewById(R.id.et_register_ruc);
//            etRanzonSocial = findViewById(R.id.et_register_razonsocial);
            etTelefono = findViewById(R.id.et_register_telefono);
//            etNombreTienda = findViewById(R.id.et_register_nombretienda);
//            etGaleria = findViewById(R.id.et_register_galeria);
//            etdireccionTienda = findViewById(R.id.et_register_direciontienda);
//            etNumStand = findViewById(R.id.et_register_numstand);
//            Checkox Validar
            checkBoxPoliticas = findViewById(R.id.checkBox1);

            /** add**/

            tvValidationEmail = findViewById(R.id.tv_validation_msg_email);
            tvValidationFirstName = findViewById(R.id.tv_validation_msg_first_name);
            tvValidationLastName = findViewById(R.id.tv_validation_msg_last_name);
            tvValidationPassword = findViewById(R.id.tv_validation_msg_password);
            tvRegMsg = findViewById(R.id.tv_reg_msg);
            /** add**/
//            tvValidationDni = findViewById(R.id.tv_validation_msg_dni);
            tvValidationRuc = findViewById(R.id.tv_validation_msg_ruc);
//            tvValidationRanzonSocial = findViewById(R.id.tv_validation_msg_razonsocial);
            tvValidationTelefono = findViewById(R.id.tv_validation_msg_telefono);
//            tvValidationNombreTienda = findViewById(R.id.tv_validation_msg_nombretienda);
//            tvValidationGaleria = findViewById(R.id.tv_validation_msg_galeria);
//            tvValidationdireccionTienda = findViewById(R.id.tv_validation_msg_direciontienda);
//            tvValidationNumStand = findViewById(R.id.tv_validation_msg_numstand);
            /** add**/


            // Agregar Iconos al campos
//            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_email), R.drawable.ic_email_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_first_name), R.drawable.ic_user_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_last_name), R.drawable.ic_user_fill_black, .5);

            // Nuevos
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_telefono), R.drawable.baseline_phone_black_24dp, .5);
//            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_galeria), R.drawable.baseline_shopping_cart_black_24dp, .5);
//            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_direciontienda), R.drawable.baseline_directions_bus_black_24dp, .5);
//            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_numstand), R.drawable.baseline_shopping_cart_black_24dp, .5);
//            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_nombretienda), R.drawable.baseline_shopping_cart_black_24dp, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_ruc), R.drawable.baseline_business_center_black_24dp, .5);


            /* START OF CLICKABLE LINK AREA */
            SpannableString tearmsAndCondition = new SpannableString(getResources().getString(R.string.recomendaciones_vendedor));
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                }
            };

            int stringCount = tearmsAndCondition.length();

            tearmsAndCondition.setSpan(clickableSpan, stringCount - 20, stringCount, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            final TextView textView = findViewById(R.id.tv_terms_writing);
            textView.setText(tearmsAndCondition);
            textView.setMovementMethod(LinkMovementMethod.getInstance());

           // btnRegisterLogin = findViewById(R.id.btn_register_login);
           // btnRegisterLogin.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_user_registration:
                User user = validateUser();

                if (user != null) {
                    networkOK = NetworkHelper.hasNetworkAccess(RegisterVeActivity.this);
                    if (networkOK) userResistration(user);
                    else
                        Toast.makeText(RegisterVeActivity.this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register_login:
                Intent intent = new Intent(RegisterVeActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.btnVerPoliticas:
                showdialogo();
                break;
        }

    }

    // Mostrar recomendaciones
    private void showdialogo() {

        new AlertDialog.Builder(this)
                .setTitle( "TERMINOS Y CONDICIONES")
                .setMessage( (getResources().getString(R.string.register_terms_writing)) )
                // creamos el boton ok en caso presione, se ejecuta el bloque de codigo
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void userResistration(final User user) {
        String JSON_URL = Constants.USER_REGISTRATION_VENDER_URL;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.trim();
                if (isInteger(response)) {
                    if (Integer.parseInt(response) > 0) {
                        DialogAlertVeActivity dialogAlertVeActivity = new DialogAlertVeActivity();
                        dialogAlertVeActivity.show(getFragmentManager(), "PRODUCT_DETAIL_DIALOG");
                        /*user.setUser_id(response);
                        CustomSharedPrefs.setLoggedInUser(RegisterVeActivity.this, user);
                        Intent intent = new Intent(RegisterVeActivity.this, MainActivity.class);
                        startActivity(intent);*/
                        clearInputs();
                    }
                } else Toast.makeText(RegisterVeActivity.this, response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("responseV", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", user.getFirst_name());
                params.put("last_name", user.getLast_name());
                params.put("username", user.getUsername());
                params.put("email", "");
                params.put("password", user.getPassword());
                params.put("image_name", user.getImage_name());
                params.put("address", "");
                params.put("membership", user.getMembership());
                params.put("number", user.getMembership());

//                params.put("dni", user.getDni());
                params.put("dni", "");
                params.put("ruc", user.getRuc());
//                params.put("razonsocial", user.getRazonsocial());
                params.put("razonsocial","");
                params.put("telefono", user.getTelefono());
                params.put("nombretienda", "");
                params.put("galeria", "");
//                params.put("direcciontienda", user.getDirecciontienda());
                params.put("direcciontienda", "");
                params.put("numstand", "");

                return params;
            }
        };

        queue.getCache().clear();
        queue.add(stringRequest);
    }


    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }


    private User validateUser() {

        // Valido el Checkbox
        if (!checkBoxPoliticas.isChecked()){
            Toast.makeText(this,"Tienes aceptar las Politicas de Privacidad", Toast.LENGTH_SHORT).show();
            return null;
        }



        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String firstName = etFirstName.getText().toString().trim();
        String lastname = etLastname.getText().toString().trim();
        String email = ""; //etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nueva = "" ; //etRepetir.getText().toString().trim();

//        String dni = etDni.getText().toString().trim();
        String ruc = etRuc.getText().toString().trim();
        String razonSocial = ""; //etRanzonSocial.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String nombreTienda = ""; //etNombreTienda.getText().toString().trim();
        String galeria =  ""; //etGaleria.getText().toString().trim();
        String direccionTienda =""; // etdireccionTienda.getText().toString().trim();
        String numStand = ""; //etNumStand.getText().toString().trim();


        boolean isFieldEmpty = false;

        if (isEmpty(firstName)) {
            tvValidationFirstName.setVisibility(View.VISIBLE);
            tvValidationFirstName.setText("El primer nombre no puede estar vacío.");
        } else {
            tvValidationFirstName.setVisibility(View.GONE);
            tvValidationFirstName.setText("");
        }

        if (isEmpty(lastname)) {
            isFieldEmpty = true;
            tvValidationLastName.setVisibility(View.VISIBLE);
            tvValidationLastName.setText("El apellido no puede estar vacío.");
        } else {
            tvValidationLastName.setVisibility(View.GONE);
            tvValidationLastName.setText("");
        }

//        if (isEmpty(email)) {
//            isFieldEmpty = true;
//            tvValidationEmail.setVisibility(View.VISIBLE);
//            tvValidationEmail.setText("El correo electrónico no puede estar vacío.");
//        } else if (!email.matches(emailPattern) && !isEmpty(email)) {
//            isFieldEmpty = true;
//            tvValidationEmail.setVisibility(View.VISIBLE);
//            tvValidationEmail.setText("Email inválido.");
//        } else {
//            tvValidationEmail.setVisibility(View.GONE);
//            tvValidationEmail.setText("");
//        }

        if (isEmpty(password)) {
            isFieldEmpty = true;
            tvValidationPassword.setVisibility(View.VISIBLE);
            tvValidationPassword.setText("La contraseña no puede estar vacía.");
        } else if (password.length() < 6 && !isEmpty(email)) {
            isFieldEmpty = true;
            tvValidationPassword.setVisibility(View.VISIBLE);
            tvValidationPassword.setText("La contraseña debe contener 6 caracteres como mínimo.");
        } else {
            tvValidationPassword.setVisibility(View.GONE);
            tvValidationPassword.setText("");
        }
// ++       if (!(password.equalsIgnoreCase(nueva))){
//            isFieldEmpty = true;
//            tvValidationPassword.setVisibility(View.VISIBLE);
//            tvValidationPassword.setText("La contraseña no son iguales.");
//        }

//        if (isEmpty(dni)) {
//            isFieldEmpty = true;
//            tvValidationDni.setVisibility(View.VISIBLE);
//            tvValidationDni.setText("El D.N.I. no puede estar vacío.");
//        }else if (dni.length()  > 8  && !isEmpty(dni)) {
//            isFieldEmpty = true;
//            tvValidationDni.setVisibility(View.VISIBLE);
//            tvValidationDni.setText("El D.N.I. debe contener 8 numeros.");
//        } else {
//            tvValidationDni.setVisibility(View.GONE);
//            tvValidationDni.setText("");
//        }

        if (isEmpty(ruc)) {
            isFieldEmpty = true;
            tvValidationRuc.setVisibility(View.VISIBLE);
            tvValidationRuc.setText("El RUC no puede estar vacío.");
        }else if (ruc.length()  > 11  && !isEmpty(ruc)) {
            isFieldEmpty = true;
            tvValidationRuc.setVisibility(View.VISIBLE);
            tvValidationRuc.setText("El RUC debe contener 11 numeros.");
        } else {
            tvValidationRuc.setVisibility(View.GONE);
            tvValidationRuc.setText("");
        }

//        if (isEmpty(razonSocial)) {
//            isFieldEmpty = true;
//            tvValidationRanzonSocial.setVisibility(View.VISIBLE);
//            tvValidationRanzonSocial.setText("La razon social no puede estar vacío.");
//        }else {
//            tvValidationRanzonSocial.setVisibility(View.GONE);
//            tvValidationRanzonSocial.setText("");
//        }

        if (isEmpty(telefono)) {
            isFieldEmpty = true;
            tvValidationTelefono.setVisibility(View.VISIBLE);
            tvValidationTelefono.setText("El teléfono no puede estar vacío.");
        }else if (telefono.length()  > 9  && !isEmpty(telefono)) {
            isFieldEmpty = true;
            tvValidationTelefono.setVisibility(View.VISIBLE);
            tvValidationTelefono.setText("El teléfono debe contener 9 numeros.");
        } else {
            tvValidationTelefono.setVisibility(View.GONE);
            tvValidationTelefono.setText("");
        }

//        if (isEmpty(nombreTienda)) {
//            isFieldEmpty = true;
//            tvValidationNombreTienda.setVisibility(View.VISIBLE);
//            tvValidationNombreTienda.setText("El nombre tienda no puede estar vacío.");
//        }else {
//            tvValidationNombreTienda.setVisibility(View.GONE);
//            tvValidationNombreTienda.setText("");
//        }
//
//        if (isEmpty(galeria)) {
//            isFieldEmpty = true;
//            tvValidationGaleria.setVisibility(View.VISIBLE);
//            tvValidationGaleria.setText("La galeria no puede estar vacío.");
//        }else {
//            tvValidationGaleria.setVisibility(View.GONE);
//            tvValidationGaleria.setText("");
//        }
//
//        if (isEmpty(direccionTienda)) {
//            isFieldEmpty = true;
//            tvValidationdireccionTienda.setVisibility(View.VISIBLE);
//            tvValidationdireccionTienda.setText("La dirección tienda no puede estar vacío.");
//        }else {
//            tvValidationdireccionTienda.setVisibility(View.GONE);
//            tvValidationdireccionTienda.setText("");
//        }

//        if (isEmpty(numStand)) {
//            isFieldEmpty = true;
//            tvValidationNumStand.setVisibility(View.VISIBLE);
//            tvValidationNumStand.setText("El numero stand no puede estar vacío.");
//        }else {
//            tvValidationNumStand.setVisibility(View.GONE);
//            tvValidationNumStand.setText("");
//        }



        if (!isFieldEmpty) {
            User user = new User();
            user.setFirst_name(firstName);
            user.setLast_name(lastname);
            user.setEmail(email);
            user.setPassword(password);
            user.setUser_number("-1");
            user.setMembership("-1");
            user.setAddress("-1");
            user.setUsername("Usuario");
            user.setImage_name("-1");
//            user.setDni(dni);
            user.setDni("");
            user.setRuc(ruc);
            user.setRazonsocial(razonSocial);
            user.setRazonsocial("");
            user.setTelefono(telefono);
            user.setNombretienda(nombreTienda);
            user.setGaleria(galeria);
            user.setDirecciontienda(direccionTienda);
            user.setNumstand(numStand);

            return user;
        } else return null;

    }

    private void clearInputs() {
        etFirstName.setText("");
        etLastname.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etRepetir.setText("");
//        etDni.setText("");
        etRuc.setText("");
        etRanzonSocial.setText("");
        etTelefono.setText("");
        etNombreTienda.setText("");
        etGaleria.setText("");
        etdireccionTienda.setText("");
        etNumStand.setText("");
    }

    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }


}
