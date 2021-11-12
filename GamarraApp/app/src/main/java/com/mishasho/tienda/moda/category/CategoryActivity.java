package com.mishasho.tienda.moda.category;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.main.MainActivity;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.userLogin.LoginActivity;
import com.mishasho.tienda.moda.userProfile.ProfileActivity;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.userProfile.UserFavActivity;
import com.mishasho.tienda.moda.userProfile.UserOrderActivity;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.cart.CartActivity;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.model.ProductCategory;
import com.mishasho.tienda.moda.services.CategoryService;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryActivity extends ActivityBaseCartIcon{

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;
    /*END OF MENU VERIABLE*/

    ArrayList<Product> productGridList;
    RecyclerView recyclerViewProduct;

    /*START OF NAVIGATION DRAWER*/
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;
    /*END OF NAVIGATION DRAWER*/

    ArrayList<ProductCategory> categoryList;
    RecyclerView recyclerViewCategory;


    private boolean netwotkOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toolbarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        categoryList = new ArrayList<>();

        recyclerViewCategory = findViewById(R.id.rv_product_category_grid);

        //END FOR RECYLER PRODUCT VIEW

        /*START OF INTENT SERVICES*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mbroadcastReceiver, new IntentFilter(CategoryService.CATEGORY_MESSAGE));

        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {

            Intent intent = new Intent(this, CategoryService.class);
            intent.setData(Uri.parse(Constants.CATEGORY_GRID_URL ));
            this.startService(intent);

        } else {
            Toast.makeText(this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
        }
        /*END OF INTENT SERVICES*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        settingNavDrawer();
    }

    private BroadcastReceiver mbroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ProductCategory[] productCategories =
                    (ProductCategory[]) intent.getParcelableArrayExtra(CategoryService.CATEGORY_PAYLOAD);
            categoryList = new ArrayList<>(Arrays.asList(productCategories));

           settingRecylerView();

        }
    };

    private void settingRecylerView(){
        CategoryRecylerViewGridAdapter productRecylerViewAdapter = new CategoryRecylerViewGridAdapter(categoryList, this);

        recyclerViewCategory.setHasFixedSize(false);
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewCategory.setNestedScrollingEnabled(false);
        recyclerViewCategory.setAdapter(productRecylerViewAdapter);
    }

   @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mbroadcastReceiver);
    }

    User loggedInUser;

    private void settingNavDrawer(){

        /*START OF NAVIGATION DRAWER */
        drawerLayout = findViewById(R.id.dl_navigation_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        loggedInUser = CustomSharedPrefs.getLoggedInUser(CategoryActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        ImageView ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);

        if(!CustomSharedPrefs.getProfileUrl(CategoryActivity.this).equals("")){
            Picasso.get().load(CustomSharedPrefs.getProfileUrl(CategoryActivity.this)).into(ivProfileImage);
        }

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loggedInUser == null){
                    Intent intentLogin = new Intent(CategoryActivity.this, LoginActivity.class);
                    intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                    startActivity(intentLogin);
                }else{
                    Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        if(loggedInUser != null){
            tvName.setText(loggedInUser.getFirst_name());
        }


        nvMainNav = findViewById(R.id.nv_main_nav);

        MenuItem loggedOut = nvMainNav.getMenu().findItem(R.id.nav_logout);
        if(loggedInUser == null) loggedOut.setTitle(getString(R.string.string_login));
        else loggedOut.setTitle(getString(R.string.string_logout));

        nvMainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intentMain = new Intent(CategoryActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_account:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                   /* case R.id.nav_category:
                        Intent intentCategory = new Intent(CategoryActivity.this, CategoryActivity.class);
                        startActivity(intentCategory);
                        drawerLayout.closeDrawers();
                        break;*/

                    case R.id.nav_orders:

                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentOrder = new Intent(CategoryActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                        break;

                    case R.id.nav_cart:
                        Intent intentCart = new Intent(CategoryActivity.this, CartActivity.class);
                        startActivity(intentCart);
                        break;
                    case R.id.nav_favourites:
                        if(loggedInUser == null){
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        }else{
                            Intent intentFav = new Intent(CategoryActivity.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }
                        break;

                    case R.id.nav_logout:
                        if (loggedInUser != null) {
                            new AlertDialog.Builder(CategoryActivity.this)
                                    .setTitle("Cerrar sesión")
                                    .setMessage("¿Quieres cerrar sesión?")
                                    .setIcon(R.drawable.ic_logout)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            CustomSharedPrefs.setProfileUrl(CategoryActivity.this,"");
                                            UtilityClass.signOutGoogle(mGoogleApiClient);
                                            UtilityClass.signOutFB();
                                            UtilityClass.signOutEmail(CategoryActivity.this);
                                            Intent intentLogin = new Intent(CategoryActivity.this, LoginActivity.class);
                                            startActivity(intentLogin);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();

                        } else {
                            item.setTitle(getString(R.string.string_login));
                            Intent intentLogin = new Intent(CategoryActivity.this, LoginActivity.class);
                            startActivity(intentLogin);
                        }
                        break;
                }

                return false;
            }
        });
        /*END OF NAVIGATION DRAWER */

    }

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }


}
