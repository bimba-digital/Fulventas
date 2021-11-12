package com.mishasho.tienda.moda.userRegistration;

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
import com.mishasho.tienda.moda.main.MainActivity;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.userLogin.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegisterLogin;
    Toolbar toolbar;
    Button btnUserRegistration;
    TextView tvTermsWriting;
    EditText etFirstName, etLastname, etEmail, etPassword, etRepetir, etDepartment,etDistrict, etProvince,etMobile,etUseraddress;
    TextView tvValidationEmail, tvValidationFirstName, tvValidationLastName, tvValidationPassword, tvRegMsg,tvValidationDepartment,tvValidationDistrict,tvValidationProvince,tvValidationMobile,tvValidationUseraddress;
    boolean fromCart = false;

    private boolean networkOK;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        toobarTitle.setText(this.getString(R.string.title_register));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (CustomSharedPrefs.getLoggedInUser(RegisterActivity.this) == null) {

            btnUserRegistration = findViewById(R.id.btn_user_registration);
            btnUserRegistration.setOnClickListener(this);

            tvTermsWriting = findViewById(R.id.tv_terms_writing);
            etFirstName = findViewById(R.id.et_register_first_name);
            etLastname = findViewById(R.id.et_register_last_name);
            etEmail = findViewById(R.id.et_register_email);
            etPassword = findViewById(R.id.et_register_password);
            etRepetir= findViewById(R.id.et_repetir_password);
            /*add*/
            etDepartment= findViewById(R.id.et_register_department);
            etDistrict= findViewById(R.id.et_register_district);
            etProvince= findViewById(R.id.et_register_province);
            etMobile= findViewById(R.id.et_register_mobile);
            etUseraddress= findViewById(R.id.et_register_useraddress);

            tvValidationEmail = findViewById(R.id.tv_validation_msg_email);
            tvValidationFirstName = findViewById(R.id.tv_validation_msg_first_name);
            tvValidationLastName = findViewById(R.id.tv_validation_msg_last_name);
            tvValidationPassword = findViewById(R.id.tv_validation_msg_password);
            tvRegMsg = findViewById(R.id.tv_reg_msg);
            /*add*/
            tvValidationDepartment = findViewById(R.id.tv_validation_msg_department);
            tvValidationDistrict = findViewById(R.id.tv_validation_msg_district);
            tvValidationProvince = findViewById(R.id.tv_validation_msg_province);
            tvValidationMobile = findViewById(R.id.tv_validation_msg_mobile);
            tvValidationUseraddress = findViewById(R.id.tv_validation_msg_useraddress);


            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_email), R.drawable.ic_email_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_first_name), R.drawable.ic_user_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_last_name), R.drawable.ic_user_fill_black, .5);
            /*add*/
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_department), R.drawable.ic_user_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_district), R.drawable.ic_user_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_province), R.drawable.ic_user_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_mobile), R.drawable.ic_user_fill_black, .5);
            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_register_useraddress), R.drawable.ic_user_fill_black, .5);
            /* START OF CLICKABLE LINK AREA */
            SpannableString tearmsAndCondition = new SpannableString(getResources().getString(R.string.register_terms_writing));
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

            btnRegisterLogin = findViewById(R.id.btn_register_login);
            btnRegisterLogin.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_user_registration:
                User user = validateUser();

                if (user != null) {
                    networkOK = NetworkHelper.hasNetworkAccess(RegisterActivity.this);
                    if (networkOK) userResistration(user);
                    else
                        Toast.makeText(RegisterActivity.this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register_login:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void userResistration(final User user) {
        String JSON_URL = Constants.USER_REGISTRATION_URL;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.trim();
                if (isInteger(response)) {
                    if (Integer.parseInt(response) > 0) {
                        user.setUser_id(response);
                        CustomSharedPrefs.setLoggedInUser(RegisterActivity.this, user);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        clearInputs();
                    }
                } else Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();

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
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());
                params.put("image_name", user.getImage_name());
                params.put("address", user.getAddress());
                params.put("membership", user.getMembership());
                params.put("number", user.getMembership());

                params.put("department", user.getDepartment());
                params.put("district", user.getDistrict());
                params.put("province", user.getProvince());
                params.put("mobile", user.getMobile());
                params.put("useraddress", user.getUseraddress());
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

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String firstName = etFirstName.getText().toString().trim();
        String lastname = etLastname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nueva = etRepetir.getText().toString().trim();

        String department = etDepartment.getText().toString().trim();
        String district = etDistrict.getText().toString().trim();
        String province = etProvince.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String useraddress = etUseraddress.getText().toString().trim();

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

        if (isEmpty(email)) {
            isFieldEmpty = true;
            tvValidationEmail.setVisibility(View.VISIBLE);
            tvValidationEmail.setText("El correo electrónico no puede estar vacío.");
        } else if (!email.matches(emailPattern) && !isEmpty(email)) {
            isFieldEmpty = true;
            tvValidationEmail.setVisibility(View.VISIBLE);
            tvValidationEmail.setText("Email inválido.");
        } else {
            tvValidationEmail.setVisibility(View.GONE);
            tvValidationEmail.setText("");
        }

        if (isEmpty(password)) {
            isFieldEmpty = true;
            tvValidationPassword.setVisibility(View.VISIBLE);
            tvValidationPassword.setText("La contraseña no puede estar vacía.");
        } else if (password.length() < 6 && !isEmpty(email)) {
            isFieldEmpty = true;
            tvValidationPassword.setVisibility(View.VISIBLE);
            tvValidationPassword.setText("La contraseña debe contener 6 caracteres como mínimo.");
           // a.equalsIgnoreCase(b)

       // } else if(password.equalsIgnoreCase(nueva) ) {
         //   isFieldEmpty = true;
          //  tvValidationPassword.setVisibility(View.VISIBLE);
           // tvValidationPassword.setText("La contraseña no son iguales."+password.toString()+nueva.toString());
        }  else {
            tvValidationPassword.setVisibility(View.GONE);
            tvValidationPassword.setText("");
        }
        if (!(password.equalsIgnoreCase(nueva))){
            isFieldEmpty = true;
            tvValidationPassword.setVisibility(View.VISIBLE);
            tvValidationPassword.setText("La contraseña no son iguales.");
        }
        /*add*/
        if (isEmpty(department)) {
            isFieldEmpty = true;
            tvValidationDepartment.setVisibility(View.VISIBLE);
            tvValidationDepartment.setText("El departamento no puede estar vacío.");
        } else {
            tvValidationDepartment.setVisibility(View.GONE);
            tvValidationDepartment.setText("");
        }

        if (isEmpty(district)) {
            isFieldEmpty = true;
            tvValidationDistrict.setVisibility(View.VISIBLE);
            tvValidationDistrict.setText("El districto no puede estar vacío.");
        } else {
            tvValidationDistrict.setVisibility(View.GONE);
            tvValidationDistrict.setText("");
        }

        if (isEmpty(province)) {
            isFieldEmpty = true;
            tvValidationProvince.setVisibility(View.VISIBLE);
            tvValidationProvince.setText("La provincia no puede estar vacío.");
        } else {
            tvValidationProvince.setVisibility(View.GONE);
            tvValidationProvince.setText("");
        }

        if (isEmpty(mobile)) {
            isFieldEmpty = true;
            tvValidationMobile.setVisibility(View.VISIBLE);
            tvValidationMobile.setText("El n° celular no puede estar vacío.");
        }else if(mobile.length() <9){
            isFieldEmpty = true;
            tvValidationMobile.setVisibility(View.VISIBLE);
            tvValidationMobile.setText("Digitar 9 digitos.");
        }else {
            tvValidationMobile.setVisibility(View.GONE);
            tvValidationMobile.setText("");
        }

        if (isEmpty(useraddress)) {
            isFieldEmpty = true;
            tvValidationUseraddress.setVisibility(View.VISIBLE);
            tvValidationUseraddress.setText("La dirección no puede estar vacío.");
        } else {
            tvValidationUseraddress.setVisibility(View.GONE);
            tvValidationUseraddress.setText("");
        }

        if (!isFieldEmpty) {
            User user = new User();
            user.setFirst_name(firstName);
            user.setLast_name(lastname);
            user.setEmail(email);
            user.setPassword(password);
            user.setUser_number("-1");
            user.setMembership("-1");
            user.setAddress("-1");
            user.setUsername("-1");
            user.setImage_name("-1");

            user.setDepartment(department);
            user.setDistrict(district);
            user.setProvince(province);
            user.setMobile(mobile);
            user.setUseraddress(useraddress);
            return user;
        } else return null;

    }

    private void clearInputs() {
        etFirstName.setText("");
        etLastname.setText("");
        etEmail.setText("");
        etPassword.setText("");

        etDepartment.setText("");
        etDistrict.setText("");
        etProvince.setText("");
        etMobile.setText("");
        etUseraddress.setText("");

    }

    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }


}
