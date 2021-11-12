package com.mishasho.tienda.moda.userLogin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.main.MainActivity;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.userRegistration.RegisterActivity;
import com.mishasho.tienda.moda.userRegistration.RegisterVeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    /*START OF MENU VERIABLE*/
    Toolbar toolbar;
    /*END OF MENU VERIABLE*/

    Button btnUserLogin;

    EditText etLoginEmail, etLoginPassword;
    TextView tvValidationLoginEmail, tvValidationLoginPassword, tvLoginMsg;

    private boolean networkOK;
    LoginButton fBLogin;
    FrameLayout btnFBLogin;
    CallbackManager callbackManager;

    FrameLayout btnGoogleLogin;
    GoogleApiClient googleApiClient;
    public static final int REQ_CODE = 9001;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if((CustomSharedPrefs.getLoggedInUser(LoginActivity.this) != null)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


       printKeyHash(this);


        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_sign_in));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (CustomSharedPrefs.getLoggedInUser(LoginActivity.this) == null) {

            UtilityClass.buttonScaleIconLeft(this, findViewById(R.id.btn_facebook_inner), R.drawable.ic_facebook_white, 1);
            UtilityClass.buttonScaleIconLeft(this, findViewById(R.id.btn_google_inner), R.drawable.ic_google_white, 1);

            UtilityClass.textViewScaleIconLeft(this, findViewById(R.id.et_login_email), R.drawable.ic_email_fill_black, .5);

            Button btnSignUp = findViewById(R.id.btn_sign_up);
            Button btnSignVe = findViewById(R.id.btn_sign_ve);
            Button btnRecuperarContraseña = findViewById(R.id.btn_recuperar_contraseña);

            btnSignUp.setOnClickListener(this);
            btnRecuperarContraseña.setOnClickListener(this);
            btnSignVe.setOnClickListener(this);

            btnUserLogin = findViewById(R.id.btn_user_sign_in);

            etLoginEmail = findViewById(R.id.et_login_email);
            etLoginPassword = findViewById(R.id.et_login_password);

            tvValidationLoginEmail = findViewById(R.id.tv_validation_login_msg_email);
            tvValidationLoginPassword = findViewById(R.id.tv_validation_login_msg_password);
            tvLoginMsg = findViewById(R.id.tv_login_msg);

            btnUserLogin.setOnClickListener(this);


            btnFBLogin = findViewById(R.id.btn_fb_login);
            btnFBLogin.setOnClickListener(this);

            /*Signin with fb/gmail*/

            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

            btnGoogleLogin = findViewById(R.id.btn_google_login);
            btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });

            fBLogin = new LoginButton(this);
            callbackManager = CallbackManager.Factory.create();

            fBLogin.setReadPermissions("public_profile", "email");
            fBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            getFBData(object);
                        }
                    });

                    Bundle bundle = new Bundle();
                    bundle.putString("fields", "id, email, first_name, last_name, link, birthday, location");
                    graphRequest.setParameters(bundle);
                    graphRequest.executeAsync();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                }
            });

            if (AccessToken.getCurrentAccessToken() != null) {
            }
        }
    }



    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_fb_login:
                fBLogin.performClick();
                break;

            case R.id.btn_sign_up:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_recuperar_contraseña:
                Intent intent2 = new Intent(LoginActivity.this, RecuperarContraseñaActivity.class);
                startActivity(intent2);
                break;

            case R.id.btn_sign_ve:
                Intent intentA = new Intent(LoginActivity.this, RegisterVeActivity.class);
                startActivity(intentA);
                break;
            case R.id.btn_user_sign_in:
                String email = etLoginEmail.getText().toString().trim();
                String password = etLoginPassword.getText().toString().trim();
                boolean isFieldEmpty = validateField(email, password);

                if (!isFieldEmpty) {
                    networkOK = NetworkHelper.hasNetworkAccess(LoginActivity.this);
                    if (networkOK) loginWithEmail(email, password);
                    else Toast.makeText(LoginActivity.this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean validateField(String email, String password) {
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

        if (isEmpty(password)) {
            isFieldEmpty = true;
            tvValidationLoginPassword.setVisibility(View.VISIBLE);
            tvValidationLoginPassword.setText("La contraseña no puede estar vacía.");
        } else if (password.length() < 6 && !isEmpty(email)) {
            isFieldEmpty = true;
            tvValidationLoginPassword.setVisibility(View.VISIBLE);
            tvValidationLoginPassword.setText("La contraseña debe contener 6 caracteres como mínimo.");
        } else {
            tvValidationLoginPassword.setVisibility(View.GONE);
            tvValidationLoginPassword.setText("");
        }
        return isFieldEmpty;
    }


    private void loginWithEmail(final String email, final String password) {
        String JSON_URL = Constants.USER_LOGIN_URL;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loginResponse(response);
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
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void loginResponse(String response) {
        User user = new Gson().fromJson(response, User.class);
        tvLoginMsg.setVisibility(View.VISIBLE);
        if (user != null) {
            CustomSharedPrefs.setLoggedInUser(LoginActivity.this, user);
            Intent intentCategory = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentCategory);
        } else tvLoginMsg.setText("Invalid email/password.");
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    public void signout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.d("emailGoogl", "Loggedout");
            }
        });
    }


    private void userResistration(final User user) {
        String JSON_URL = Constants.USER_REG_SOCIAL_URL;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.trim();
                if (isInteger(response)) {
                    if (Integer.parseInt(response) > 0) {
                        user.setUser_id(response);
                        CustomSharedPrefs.setLoggedInUser(LoginActivity.this, user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();

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
                params.put("number", user.getUser_number());

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


    private void getGoogleData(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
            String id = googleSignInAccount.getId();
            String email = googleSignInAccount.getEmail();
            String name = googleSignInAccount.getDisplayName();
            String profileUrl = googleSignInAccount.getPhotoUrl().toString();
            if (id != null) {
                User user = new User();
                String nameArray[] = name.split(" ");
                user.setFirst_name(nameArray[0]);
                user.setLast_name(nameArray[1]);
                user.setEmail(email);
                user.setUsername(id);
                user.setMembership("-1");
                user.setUser_number("-1");
                user.setImage_name("-1");
                user.setAddress("-1");
                user.setPassword("-1");
                userResistration(user);

                CustomSharedPrefs.setProfileUrl(LoginActivity.this, profileUrl);
            }
        }
    }

    private void getFBData(JSONObject object) {
        try {
            URL profilePicture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=200&height=200");
            String id = object.getString("id");
            String profileUrl = profilePicture.toString();
            String firstName = object.getString("first_name");
            String lastName = object.getString("last_name");
            if (id != null) {

                User user = new User();
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setEmail("-1");
                user.setUsername(id);
                user.setMembership("-1");
                user.setUser_number("-1");
                user.setImage_name("-1");
                user.setAddress("-1");
                user.setPassword("-1");
                userResistration(user);



                CustomSharedPrefs.setProfileUrl(LoginActivity.this, profileUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getGoogleData(googleSignInResult);
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.d("FBKeyHash=", key);
                //Log.d("gogoleKeyHash=", md.toString());
            }
        } catch (PackageManager.NameNotFoundException e1) {
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }
        return key;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean isEmpty(String value) {
        return (value.length() < 1) ? true : false;
    }


}
