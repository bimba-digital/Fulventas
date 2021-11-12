package com.mishasho.tienda.moda.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.checkoutPayment.ShippingActivity;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.main.CustomProductInventory;
import com.mishasho.tienda.moda.main.MainActivity;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.model.SelectedProduct;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.search.SearchActivity;
import com.mishasho.tienda.moda.type.TypeActivity;
import com.mishasho.tienda.moda.userLogin.LoginActivity;
import com.mishasho.tienda.moda.userProfile.ProfileActivity;
import com.mishasho.tienda.moda.userProfile.UserFavActivity;
import com.mishasho.tienda.moda.userProfile.UserOrderActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    public static double totalPrice = 0;
    public static int cantidadTotal = 0;
    public static int totalPeso;
    public static int totalPesoEnvio=0;

    TextView tvTotalWriting, tvTotalWeight, tvTotalWeightEnvio;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;

    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;
    public ArrayList<String> searchListArray;
    ArrayList<Product> productGridList;
    ImageButton btnSearchFiltre, btnList1;
    SearchView etSearch;

    private List<SelectedProduct> selectedProductList;
    private DataSource dataSource;

    TextView tvNoProducts, title_list;

    // New RN
    private TextView mensajeGarantia;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dataSource = new DataSource(this);
        settingProductGrid();
        UtilityClass.buttonScaleIconRight(this, findViewById(R.id.btn_cart_checkout), R.drawable.ic_arrow_right_white, .8, 1.1);

        /*START OF TOOLBAR*/

        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toolbarLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        toolbarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);

        //  btnClear = findViewById(R.id.btn_clear);
        etSearch = findViewById(R.id.et_seach_product);
        btnSearchFiltre = findViewById(R.id.btn_filtre);
        btnList1 = findViewById(R.id.btn_filtre_list);
        btnSearchFiltre.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        title_list =  findViewById(R.id.id_lista_title);
        title_list.setText(Html.fromHtml("<u>"+ this.getString(R.string.title_cart)+ "</u> "  ));


        // Funcionalidad de mensaje Garantia
        mensajeGarantia = findViewById(R.id.btnGarantia);
        mensajeGarantia.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   AlertDialog.Builder builder= new AlertDialog.Builder(CartActivity.this);
                                                   builder
                                                           .setMessage("Su pedido está seguro  con nosotros, llegara a su domicilio con toda la seguridad.")
                                                           .setTitle("Garantía del Pedido")
                                                           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                               @Override public void onClick(DialogInterface dialog, int which)
                                                               {
                                                                   dialog.cancel();
                                                               }
                                                           });

                                                   AlertDialog alert = builder.create();
                                                   alert.show();

                                               }
                                           });




                /*END OF TOOLBAR*/
                tvTotalWriting = findViewById(R.id.tv_total_price);

                tvTotalWeight = findViewById(R.id.tv_total_weight_val);
                tvTotalWeightEnvio = findViewById(R.id.tv_total_weight_envio_val);

        /*START OF CART */
        selectedProductList = dataSource.getAllCartProducts();


        StringBuilder inventoryIds = new StringBuilder();
        for(SelectedProduct selectedProduct : selectedProductList){
            inventoryIds.append(selectedProduct.getInvetory_id() + "-");
            //Log.d("temp1 list", " "+ selectedProduct.getInvetory_id());
        }

        getInventories(inventoryIds.toString());

        totalPrice = 0;
        cantidadTotal = 0 ;

        totalPeso = 0;
        for (SelectedProduct selectedProduct : selectedProductList) {
            totalPrice = totalPrice + getTotalPrice(selectedProduct);
            cantidadTotal = cantidadTotal+ getTotalCantidad(selectedProduct);
            Log.d("PesoI",  String.valueOf(getTotalWeight(selectedProduct)));

            totalPeso = totalPeso + getTotalWeight(selectedProduct);
           // Toast.makeText(CartActivity.this, cantidadTotal, Toast.LENGTH_LONG).show();
        }



        // Muestro el Peso
        tvTotalWeight.setText(String.valueOf(totalPeso) + " gr");
        Log.d("PesoX", String.valueOf(totalPeso));

        // Muestro la comision
        // Si excelos 10 KG
        totalPesoEnvio = getAdicionalEnvio(totalPeso);

        if (totalPesoEnvio == 0) {
            tvTotalWeightEnvio.setText("S/ 15");
            Toast.makeText(getApplicationContext(), "Se excedió el Peso para el Envió para el envio por favor maximo 10 kilos.", Toast.LENGTH_LONG).show();
            totalPesoEnvio = 15;
        }
        else {
            tvTotalWeightEnvio.setText("S/ " + String.valueOf(totalPesoEnvio));
        }

        // Muestro el total de envio + comision
        totalPrice = totalPrice + totalPesoEnvio;

        tvTotalWriting.setText(UtilityClass.getNumberFormat(this, totalPrice));

        Button btn_cart_checkout = findViewById(R.id.btn_cart_checkout);
        btn_cart_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   if(cantidadTotal >= 13){
                   // openDialog();
              //  } else {

                    if (CustomSharedPrefs.getLoggedInUser(CartActivity.this) == null) {
                        Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                        intent.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_CHECKOUT);
                        //intent.putExtra(Constants.TO_REGISTER, Constants.FROM_CART);
                        startActivity(intent);
                    } else {

                        String totalPriceStr = String.valueOf(totalPrice);

                        if (totalPrice > 0) {
                            CustomSharedPrefs.setCartEmpty(CartActivity.this, false);
                            Intent intent = new Intent(CartActivity.this, ShippingActivity.class);
                            intent.putExtra(Constants.TOTAL_PRICE, totalPriceStr);
                            startActivity(intent);

                        } else {

                            Toast.makeText(CartActivity.this, "Por favor agregue un producto", Toast.LENGTH_LONG).show();
                        }
                    }

                }


           // }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // mSelectedProduct = null;
        // invalidateOptionsMenu();
        settingNavDrawer();
    }

    public void openDialog()
    {
        CartDialog exampleDialog = new CartDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }



    ArrayList<CustomProductInventory> customProductInventoryList;

    private void getInventories(String inventory_ids){
        String JSON_URL = Constants.INVENTORY_BY_ID + "?inventory_ids=" + inventory_ids;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<CustomProductInventory>>() {}.getType();
                customProductInventoryList = new Gson().fromJson(response, listType);

                CartListAdapter cartListAdapter = new CartListAdapter(CartActivity.this, R.layout.item_list_cart_product,
                        selectedProductList, tvTotalWriting, tvTotalWeight, tvTotalWeightEnvio, cantidadTotal, customProductInventoryList);
                ListView lvCartProductList = findViewById(R.id.lv_cart_product_list);
                lvCartProductList.setAdapter(cartListAdapter);
                lvCartProductList.setDivider(null);

//                lvCartProductList.setOnTouchListener(new ListView.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int action = event.getAction();
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                // Disallow ScrollView to intercept touch events.
//                                v.getParent().requestDisallowInterceptTouchEvent(true);
//                                break;
//
//                            case MotionEvent.ACTION_UP:
//                                // Allow ScrollView to intercept touch events.
//                                v.getParent().requestDisallowInterceptTouchEvent(false);
//                                break;
//                        }
//
//                        // Handle ListView touch events.
//                        v.onTouchEvent(event);
//                        return true;
//                    }
//                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private double getTotalPrice(SelectedProduct selectedProduct) {
        return selectedProduct.getPrice() * selectedProduct.getQunatity();
    }

    private int getTotalCantidad(SelectedProduct selectedProduct) {
        return selectedProduct.getQunatity();
    }


    // Agrego la suma total del PESO del producto
    private int getTotalWeight(SelectedProduct selectedProduct) {
        return Integer.parseInt(selectedProduct.getWeight()) * selectedProduct.getQunatity();
    }

    // ===================================
    // Valiido el costo de envio
    public static int getAdicionalEnvio(int monto) {
        int comision = 0;

        // SI es menor a 5kg => 10 S/
        // SI es mayor o iugal a 10 kg=> 15 S/
        // SI es mayor a 10 KG => 0
        if (monto <= 5000){
            comision=10;
        } else if (monto > 5000 && monto <10000) {
            comision = 15;
        } else {
            comision = 0;
        }

        return comision;
    }

    private void settingProductGrid() {

        String JSON_URL = Constants.PRODUCT_GRID_URL;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseV", response);
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                productGridList = new Gson().fromJson(response, listType);
                searchListArray = new ArrayList<>();
                for(Product pro:productGridList) {
                    searchListArray.add(pro.getTitle());

                }

                final   SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) etSearch.findViewById(R.id.search_src_text);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(),  R.layout.textview_auto_complete, searchListArray);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                searchAutoComplete.setAdapter(dataAdapter);

                searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                        String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                        searchAutoComplete.setText("");
                        String idEtex1="idEtex";
                        Intent intent = new Intent(CartActivity.this, SearchActivity.class);
                        intent.putExtra("seacrhedtext",queryString);
                        intent.putExtra("idEtex",idEtex1);

                        startActivity(intent);


                    }
                });
                // Below event is triggered when submit search query.
                etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchAutoComplete.setText("");
                        String idEtex1="idEtex";
                        Intent intent = new Intent(CartActivity.this, SearchActivity.class);
                        intent.putExtra("seacrhedtext",query);
                        intent.putExtra("idEtex",idEtex1);
                        startActivity(intent);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);

    }



    User loggedInUser;

    private void settingNavDrawer() {

        /*START OF NAVIGATION DRAWER */
        drawerLayout = findViewById(R.id.dl_navigation_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        loggedInUser = CustomSharedPrefs.getLoggedInUser(CartActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        //tvName.setText("Iniciar sesión / Regístrate aquí");
        ImageView ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);
        Picasso.get().load(R.drawable.profile_image).into(ivProfileImage);

        if (!CustomSharedPrefs.getProfileUrl(CartActivity.this).equals("")) {
            Picasso.get().load(CustomSharedPrefs.getProfileUrl(CartActivity.this)).into(ivProfileImage);
        }

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loggedInUser == null) {
                    Intent intentLogin = new Intent(CartActivity.this, LoginActivity.class);
                    intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                    startActivity(intentLogin);
                } else {
                    Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        if (loggedInUser != null) tvName.setText(loggedInUser.getFirst_name());

        nvMainNav = findViewById(R.id.nv_main_nav);

        MenuItem loggedOut = nvMainNav.getMenu().findItem(R.id.nav_logout);
        if (loggedInUser == null) loggedOut.setTitle(getString(R.string.string_login));
        else loggedOut.setTitle(getString(R.string.string_logout));

        nvMainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intentMain = new Intent(CartActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_account:
                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(CartActivity.this, LoginActivity.class);
                            startActivity(intentLogin);
                        } else {
                            Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_sub_hombres:
                        String title_hombre="Hombre";
                        String idHombres="1";
                        Intent subHombres = new Intent(CartActivity.this, TypeActivity.class);
                        subHombres.putExtra("subTitleHombre",title_hombre);
                        subHombres.putExtra("subIdHombres",idHombres);
                        startActivity(subHombres);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_mujer:
                        String title_mujeres="Mujer";
                        String idMujeres="2";
                        Intent subMujeres = new Intent(CartActivity.this, TypeActivity.class);
                        subMujeres.putExtra("subTitleMujeres",title_mujeres);
                        subMujeres.putExtra("subIdMujeres",idMujeres);
                        startActivity(subMujeres);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_ninhos:
                        String title_ninhos="Niño";
                        String idNinhos="3";
                        Intent subNinhos = new Intent(CartActivity.this, TypeActivity.class);
                        subNinhos.putExtra("subTitleNinhos",title_ninhos);
                        subNinhos.putExtra("subIdNinhos",idNinhos);
                        startActivity(subNinhos);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_bebes:
                        String title_bebes="Bebe";
                        String idBebes="4";
                        Intent subBebes = new Intent(CartActivity.this, TypeActivity.class);
                        subBebes.putExtra("subTitleBebes",title_bebes);
                        subBebes.putExtra("subIdBebes",idBebes);
                        startActivity(subBebes);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_orders:

                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(CartActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        } else {
                            Intent intentOrder = new Intent(CartActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                        break;

                    case R.id.nav_cart:
                        Intent intentCart = new Intent(CartActivity.this, CartActivity.class);
                        startActivity(intentCart);
                        break;

                    case R.id.nav_favourites:
                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(CartActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        } else {
                            Intent intentFav = new Intent(CartActivity.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }
                        break;

                    case R.id.nav_logout:
                        if (loggedInUser != null) {
                            new AlertDialog.Builder(CartActivity.this)
                                    .setTitle("Cerrar sesión")
                                    .setMessage("¿Quieres cerrar sesión?")
                                    .setIcon(R.drawable.ic_logout)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            CustomSharedPrefs.setProfileUrl(CartActivity.this, "");
                                            UtilityClass.signOutGoogle(mGoogleApiClient);
                                            UtilityClass.signOutFB();
                                            UtilityClass.signOutEmail(CartActivity.this);
                                            Intent intentLogin = new Intent(CartActivity.this, LoginActivity.class);
                                            startActivity(intentLogin);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();

                        } else {
                            item.setTitle(getString(R.string.string_login));
                            Intent intentLogin = new Intent(CartActivity.this, LoginActivity.class);
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
