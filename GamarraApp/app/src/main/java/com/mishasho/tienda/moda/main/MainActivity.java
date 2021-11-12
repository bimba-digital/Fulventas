package com.mishasho.tienda.moda.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.core.graphics.ColorUtils;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mishasho.tienda.moda.adapter.CardPagerAdapterS;
import com.mishasho.tienda.moda.model.Gallery;
import com.mishasho.tienda.moda.model.Slider;
import com.mishasho.tienda.moda.model.Types;

import com.mishasho.tienda.moda.search.SearchActivity;
import com.mishasho.tienda.moda.searching.SearchAdapter;
import com.mishasho.tienda.moda.type.TypeActivity;
import com.mishasho.tienda.moda.userRegistration.RegisterVeActivity;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;

import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.prductGrid.ProductRecylerViewAdapter;
import com.mishasho.tienda.moda.userLogin.LoginActivity;
import com.mishasho.tienda.moda.userProfile.ProfileActivity;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.userProfile.UserFavActivity;
import com.mishasho.tienda.moda.userProfile.UserOrderActivity;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.cart.CartActivity;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.model.SliderMain;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;

import androidx.appcompat.widget.SearchView;

import es.dmoral.toasty.Toasty;

public class MainActivity extends ActivityBaseCartIcon implements GoogleApiClient.OnConnectionFailedListener,DialogAlertActivityFiltre.SingleChoiceListener {

    private boolean netwotkOK;

    ArrayList<SliderMain> sliderMainList;
    public ArrayList<Product> productGridList;
    public ArrayList<Gallery> gallerytGridList;
    public ArrayList<Product> searchList;

    ViewPager vpSliderMain;
    RecyclerView recyclerViewProduct, search_bar;
    ViewPager vpPopularProducts, vptypes;
    private LinearLayout dotsIndicators;
    public ImageView imgen, img2;
    //Toolbar toolbar search;
    ImageButton btnClear;
   // EditText etSearch;
    SearchView etSearch;
    ImageButton btnSearchFiltre,btnList1;
    //end
    private CardPagerAdapterS mCardAdapter;

    TextView toobarTitle;
    ImageView toobarLogo;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;

    private  int filtrePosition = 0;
    private String idFiltre;
   // ArrayList<ProductCategory> categoryList;
    ArrayList<Types> typeList;
    ArrayList<Product> sliderPopularProductList;
    ArrayList<Slider> sliderList;

    //to get user session data
//    private UserSession session;

    RecyclerView recyclerView;
    DataSource dataSource;
    public ArrayList<String> searchListArray;
    private boolean filtreList = true;

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        settingNavDrawer();
    }

    private void settingCurrency() {
        String JSON_URL = Constants.CURRENCY;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void insetAppToken(final String token) {
        String JSON_URL = Constants.INSERT_FCM_TOKEN;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.trim().equals(""))
                    CustomSharedPrefs.setFirebaseToken(MainActivity.this, response);
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
                params.put("firebase_token", token);

                return params;
            }
        };
        queue.getCache().clear();
        queue.add(stringRequest);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad  = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        //this.setContentView(R.layout.main);
        setContentView(R.layout.activity_main);

        vptypes =  (ViewPager) findViewById(R.id.rv_product_category);
        recyclerViewProduct = findViewById(R.id.rv_p_detail_product_grid);
      //  search_bar = findViewById(R.id.search_bar);
        //  rvSerachProductGrid = findViewById(R.id.rv_p_detail_product_search);

        /* DATABASE*/
        dataSource = new DataSource(MainActivity.this);
        dataSource.open();
        TextView txt1 = findViewById(R.id.producto_text);
        txt1.setText(Html.fromHtml("<u>"+ this.getString(R.string.recent_product)+ "</u> "  ));

        if (CustomSharedPrefs.getFirebaseToken(MainActivity.this).equals("")) {
            FirebaseMessaging.getInstance().subscribeToTopic("message");
            insetAppToken(FirebaseInstanceId.getInstance().getToken());
        }

        CustomSharedPrefs.setFavProductsInPref(MainActivity.this, dataSource);

        settingToolBar();
        settingPopularProductSlider();

        CustomSharedPrefs.setCurrency(MainActivity.this, "S/");
        settingProductGrid();
        //settingCategories();
        settingTypes();
        settingSliderImagen();
       // settingMainSlider();
/*        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //   etSearch.setText("");
            }
        });*/
//        session.setFirstTimeLaunch(false);

        // Si es primer Login Muestra el Helper (Tour de funciones y bondades)
// +++       if (returnPin() == "nulo") {
//            //tap target view
//  ++          tapview();
////            session.setFirstTime(false);
//  ++      }
//        tapview();

        // Boton directo registrarte vendedor - 09.12.2020
        findViewById(R.id.btnNuevoVendedor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterVeActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnSubirProducto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://gamarritas.com/public/admin-login.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // Para Enlace directo al WS
//+++        findViewById(R.id.btnWhatsapp).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://api.whatsapp.com/send?phone=51942246210&text=Quiero%20m%C3%A1s%20informaci%C3%B3n!";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            }
//        });


        // RN - Click para cada cuadro dialogo
        // Click Para Cabellero
        findViewById(R.id.btnCaballeros).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TypeActivity.class);
                intent.putExtra(Constants.INTENT_TYPE_ID, "{\"id\":\"1\",\"image_name\":\"36a797a54ffce59f7d8a3aeddb7452e0.jpeg\",\"sort\":0,\"title\":\"Hombre\"}");
                startActivity(intent);
            }
        });

        // Click Para Damas
        findViewById(R.id.btnMuejres).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TypeActivity.class);
                intent.putExtra(Constants.INTENT_TYPE_ID, "{\"id\":\"2\",\"image_name\":\"2fbe7eb3ab2264224041d8b2b46990c0.jpeg\",\"sort\":0,\"title\":\"Mujer\"}");
                startActivity(intent);
            }
        });


        // Click Para niños
        findViewById(R.id.btnNinos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TypeActivity.class);
                intent.putExtra(Constants.INTENT_TYPE_ID, "{\"id\":\"3\",\"image_name\":\"73e9e44fa6978502df7d09223470d82c.jpeg\",\"sort\":0,\"title\":\"Niño\"}");
                startActivity(intent);
            }
        });

        // Click Para niñas
        findViewById(R.id.btnNinas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TypeActivity.class);
                intent.putExtra(Constants.INTENT_TYPE_ID, "{\"id\":\"4\",\"image_name\":\"b82e80cb6e422389ea47f090af7163d4.jpeg\",\"sort\":0,\"title\":\"Bebe\"}");
                startActivity(intent);
            }
        });

//+        Timer timer = new Timer();
//+        timer.scheduleAtFixedRate(new MyTime(),2000,3000);

        btnSearchFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //open_dialog(v);


                DialogAlertActivityFiltre dialogAlertActivityFiltre = new DialogAlertActivityFiltre();
                Bundle bund = new Bundle();
                bund.putInt("filtre",filtrePosition);
                dialogAlertActivityFiltre.setArguments(bund);
                dialogAlertActivityFiltre.show(getFragmentManager(), "PRODUCT_DETAIL_DIALOG");


            }
        });
        btnList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filtreList){
                    setingRecylerLis1();
                    //setingRecylerView();
                    filtreList = false;
                }else if(!filtreList)
                 {
                     setingRecylerView();
                    // setingRecylerLis1();
                    filtreList = true;
                }

            }
        });


        //recibirDatos();


    }




    // Funcion para el Helper, un Tour para las funcionalides
    private void tapview() {

        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.et_seach_product), "Búsqueda", "Con este potente buscador encuentras tu ropa idea!")
                                .targetCircleColor(R.color.colorAccent)
                                .titleTextColor(R.color.white_border)
                                .titleTextSize(25)
                                .descriptionTextSize(18)
                                .descriptionTextColor(R.color.white_border)
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.third),
                        TapTarget.forView(findViewById(R.id.btn_filtre_list), "Modo Lista", "Cuando des click aca puedes ver el resultado una fila o dos columnas.")
                                .targetCircleColor(R.color.colorAccent)
                                .titleTextColor(R.color.white_border)
                                .titleTextSize(25)
                                .descriptionTextSize(18)
                                .descriptionTextColor(R.color.white_border)
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.third),
                        TapTarget.forView(findViewById(R.id.btnCaballeros), "Categorias", "Puedes revisar las categorias mas relevantes!")
                                .targetCircleColor(R.color.colorAccent)
                                .titleTextColor(R.color.white_border)
                                .titleTextSize(25)
                                .descriptionTextSize(18)
                                .descriptionTextColor(R.color.white_border)
                                .drawShadow(true)
                                .cancelable(true)// Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)
                                .transparentTarget(true)
                                .outerCircleColor(R.color.third))
//                        TapTarget.forView(findViewById(R.id.visitingcards), "Categories", "Product Categories have been listed here !")
//                                .targetCircleColor(R.color.colorAccent)
//                                .titleTextColor(R.color.colorAccent)
//                                .titleTextSize(25)
//                                .descriptionTextSize(15)
//                                .descriptionTextColor(R.color.accent)
//                                .drawShadow(true)
//                                .cancelable(false)// Whether tapping outside the outer circle dismisses the view
//                                .tintTarget(true)
//                                .transparentTarget(true)
//                                .outerCircleColor(R.color.fourth))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                       // session.setFirstTime(false);
                        savePin("si");
                        Toasty.success(MainActivity.this, " Tu ya estas listo !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();

    }

    // PINSES GUARDO Para no pedir
    public void savePin(String message) {
        SharedPreferences prefe = getApplication().getSharedPreferences("premium", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString("pines", message);
        //  editor.putString("posici", posi);
        editor.commit();

    }


    public String returnPin(){

        SharedPreferences prefe = getApplication().getSharedPreferences("premium", getApplicationContext().MODE_PRIVATE);
        String d = prefe.getString("pines", "");
        //   Log.d("memory", d);
        //  Log.d("longi", String.valueOf(d.length()) );


        if (d.length()==0) {

            return "nulo";
        }
        else {
            return d;
        }
    }

    @Override
    public void onPositiveButtonClicked(String[] list, final int position) {
        filtrePosition = position;
        idFiltre = list[position];
        if(position == 0){
            settingProductGrid();
        }else if (position == 1){
            netwotkOK = NetworkHelper.hasNetworkAccess(this);
            if (netwotkOK) {

                String JSON_URL = Constants.GALLERY_GRID_URL+ "?search=/gallery";
                RequestQueue queue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseV", response);
                        Type listType = new TypeToken<List<Gallery>>() {
                        }.getType();
                        gallerytGridList = new Gson().fromJson(response, listType);
                        setingRecylerView();
//                        setingRecylerLis1();
                        searchListArray = new ArrayList<>();

                        for(Gallery pro:gallerytGridList) {
                            searchListArray.add(pro.getGallery());

                        }

                        final   SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) etSearch.findViewById(R.id.search_src_text);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(),  R.layout.textview_auto_complete,searchListArray );
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        searchAutoComplete.setAdapter(dataAdapter);

                        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                                searchAutoComplete.setText("");
                                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                intent.putExtra("seacrhedtext",queryString);
                                intent.putExtra("idFiltre",idFiltre);
                                intent.putExtra("position",position);
                                startActivity(intent);


                            }
                        });
                        // Below event is triggered when submit search query.
                        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                searchAutoComplete.setText("");
                                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                intent.putExtra("seacrhedtext",query);
                                intent.putExtra("idFiltre",idFiltre);
                                intent.putExtra("position",position);
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

            } else Toast.makeText(this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();

        }

    }

    /*@Override
    public void onNegativeButtonClicked() {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
    }*/

    public class MyTime extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    netwotkOK = NetworkHelper.hasNetworkAccess(getApplication());
                    if (netwotkOK) {
                        if (vpSliderMain.getCurrentItem() == 0) {
                            vpSliderMain.setCurrentItem(1);
                        } else if (vpSliderMain.getCurrentItem() == 1) {
                            vpSliderMain.setCurrentItem(2);
                        } else {
                            vpSliderMain.setCurrentItem(0);
                        }
                    }
                }
            });
        }
    }


    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
       if(extras!=null){
           String id_btn = extras.getString("primeravez");
        if(id_btn != null){
            DialogAlertActivity dialogAlertActivity = new DialogAlertActivity();
            dialogAlertActivity.show(getFragmentManager(), "PRODUCT_DETAIL_DIALOG");
        }
       }

    }


    private void settingProductGrid() {
        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {

            String JSON_URL = Constants.PRODUCT_GRID_URL;
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("responseV", response);
                    Type listType = new TypeToken<List<Product>>() {
                    }.getType();
                    productGridList = new Gson().fromJson(response, listType);
                    // setingRecylerView();
                    setingRecylerLis1();
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
                            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
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
                            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
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

        } else Toast.makeText(this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
    }

    private void settingTypes() {
        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {

            String JSON_URL = Constants.TYPES_LIST;
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("responseV", response);
                    Type listType = new TypeToken<List<Types>>() {
                    }.getType();
                    typeList = new Gson().fromJson(response, listType);
                    settingHorizontalTypeList();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.getCache().clear();
            queue.add(stringRequest);

        } else Toast.makeText(this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
    }



    private void settingSliderImagen() {
        netwotkOK = NetworkHelper.hasNetworkAccess(this);
        if (netwotkOK) {

            String JSON_URL = Constants.SLIDER_IMAGEN;
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("responseV", response);
                    Type listType = new TypeToken<List<Slider>>() {
                    }.getType();
                    sliderList = new Gson().fromJson(response, listType);
                    settingMainSlider();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.getCache().clear();
            queue.add(stringRequest);

        } else Toast.makeText(this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
    }

    private void settingMainSlider() {
        // START VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES
        sliderPopularProductList = new ArrayList<>();

        vpPopularProducts = findViewById(R.id.vp_popular_products);
        PagerAdapter sliderPopularProductAdapter = new SliderPopularProductAdapter(getSupportFragmentManager());
        vpPopularProducts.setAdapter(sliderPopularProductAdapter);
        // END VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES




        // START VIEWPAGER MAIN SLIDER IMAGES
        sliderMainList = new ArrayList<>();

        for(Slider sl: sliderList){
            SliderMain sliderMain1 = new SliderMain();
           sliderMain1.setId(sl.getImage_name());
           sliderMain1.setHeading(sl.getTitle());
           sliderMain1.setPreHeading(sl.getDescription());
            sliderMainList.add(sliderMain1);
        }
       // SliderMain sliderMain1 = new SliderMain("slider_1", "", "MEGA VENTA");
       // SliderMain sliderMain2 = new SliderMain("slider_2", "Los trajes más nuevos acaba de llegar", "LOS RECIÉN LLEGADOS");
       // SliderMain sliderMain3 = new SliderMain("slider_3", "Venta hasta el 30% esta semana", "COLECCION HOMBRE");


      //  sliderMainList.add(sliderMain2);
      //  sliderMainList.add(sliderMain3);

        vpSliderMain = findViewById(R.id.vp_slider_main);
        PagerAdapter sliderMainAdapter = new SliderMainAdapter(getSupportFragmentManager());
        vpSliderMain.setAdapter(sliderMainAdapter);
        // END OF VIEWPAGER SLIDER IMAGES

        /*START OF DOTS INDICATORS*/
        dotsIndicators = findViewById(R.id.layout_slider_main_dots);
        dots(0);

        vpSliderMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                dots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setingRecylerView() {

        ProductRecylerViewAdapter productRecylerViewAdapter = new ProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(productRecylerViewAdapter);
    }
    private void setingRecylerLis1() {

        ProductRecylerViewAdapter productRecylerViewAdapter = new ProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(productRecylerViewAdapter);
    }

    private void setingRecylerViewSearch() {

        SearchAdapter searchAdapter = new SearchAdapter(searchList, this);

//        search_bar.setHasFixedSize(false);
//        search_bar.setNestedScrollingEnabled(false);
        search_bar.setAdapter(searchAdapter);
        search_bar.bringToFront();
        searchAdapter.notifyDataSetChanged();
        search_bar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               String textAdd = searchList.get(position).getTitle();
              //  etSearch.setText(textAdd);
                String idEtex1="idEtex";
                search(null);
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("seacrhedtext",textAdd);
                intent.putExtra("idEtex",idEtex1);
                startActivity(intent);
               // etSearch.setText("");

            }
        });
    }

  /*  public void settingHorizontalTypeList() {

        TypesRecylerViewAdapter adapter = new TypesRecylerViewAdapter(typeList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //END OF HORIZOANTAL CATEGORY LIST
    }*/
  public void settingHorizontalTypeList() {

      mCardAdapter = new CardPagerAdapterS();
      for (Types type : typeList){
          mCardAdapter.addCardItemS(new Types(type.getId(),type.getTitle(),type.getImage_name()),this);
      }
      vptypes.setAdapter(mCardAdapter);

      imgen = (ImageView) findViewById(R.id.img_previous);
      imgen.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              vptypes.setCurrentItem(getItemofviewpager(-1), true);
          }
      });
      img2 = (ImageView) findViewById(R.id.img_next);
      img2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              vptypes.setCurrentItem(getItemofviewpager(+1), true);
          }
      });
  }

    private int getItemofviewpager(int i) {
        return vptypes.getCurrentItem() + i;
    }
  /*  public void settingHorizontalCategoryList() {

        CategoryRecylerViewAdapter adapter = new CategoryRecylerViewAdapter(categoryList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //END OF HORIZOANTAL CATEGORY LIST

    }*/

    private void dots(int current) {

        dotsIndicators.removeAllViews();
        for (int i = 0; i < sliderMainList.size(); i++) {
            TextView dot = new TextView(this);
            dot.setIncludeFontPadding(false);
            dot.setHeight((int) UtilityClass.convertDpToPixel(10, this));
            dot.setWidth((int) UtilityClass.convertDpToPixel(10, this));

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginAsDp = (int) UtilityClass.convertDpToPixel(4, this);
            params.setMargins(marginAsDp, marginAsDp, marginAsDp, marginAsDp);
            dot.setLayoutParams(params);

            dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_border_bg_1));

            if (i == current) {
                dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));
            }
            dotsIndicators.addView(dot);

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /*START OF MAIN SLIDER ADAPTER*/
    public class SliderMainAdapter extends FragmentStatePagerAdapter {

        public SliderMainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderMainItemFragment.newInstance(sliderMainList.get(position));
        }

        @Override
        public int getCount() {
            return sliderMainList.size();
        }

    }
    /*END OF MAIN SLIDER ADAPTER*/


    /*START OF POLULAR PRODUCT SLIDER ADAPTER*/
    public class SliderPopularProductAdapter extends FragmentStatePagerAdapter {


        public SliderPopularProductAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderProductItemFragment.newInstance(sliderPopularProductList.get(position));
        }

        @Override
        public float getPageWidth(int position) {
            return .55f;
        }

        @Override
        public int getCount() {
            return sliderPopularProductList.size();
        }

    }
    /*END OF POLULAR PRODUCT SLIDER ADAPTER*/


    public void settingToolBar() {

        /*START OF TOOLBAR */
        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toobarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toobarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        /*END OF TOOLBAR */
        /*START OF TOOLBAR search */

        btnSearchFiltre = findViewById(R.id.btn_filtre_menu);
        btnSearchFiltre.setVisibility(View.VISIBLE);
      //  btnClear = findViewById(R.id.btn_clear);
        btnList1 = findViewById(R.id.btn_filtre_list);
        etSearch = (SearchView) findViewById(R.id.et_seach_product);
        //String data[]={"Emmanuel", "Olayemi", "Henrry", "Mark", "Steve", "Ayomide", "David", "Anthony", "Adekola", "Adenuga"};

        // Para que se cliekte en cualquier parte
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setIconified(false);
            }
        });


     /*     etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
          public void afterTextChanged(Editable s) {
//                Toast.makeText(getApplicationContext(), "El texto es :" + s.toString(), Toast.LENGTH_LONG).show();
                try {
                    //!s.toString().equalsIgnoreCase("")
                    if(etSearch.getText().toString().trim().length() > 0){
                        String  temp=s.toString().replace(" ", "%20");
                        search(temp.toString());
                       // search(s.toString());
                    }else{
                        //searchList.clear();
                        search(null);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        });*/

      /*  etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(getApplicationContext(), "El texto es :" + s.toString(), Toast.LENGTH_LONG).show();
                try {
                    //!s.toString().equalsIgnoreCase("")
                    if(etSearch.getText().toString().trim().length() > 0){
                        String  temp=s.toString().replace(" ", "%20");
                        search(temp.toString());
                        // search(s.toString());
                    }else{
                        //searchList.clear();
                        search(null);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        });*/
    }

    private void search(String s){
        String JSON_URL = Constants.SEARCH;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL + "?search="+s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getApplicationContext(), "Data: " + response, Toast.LENGTH_LONG).show();;
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                searchList = new Gson().fromJson(response, listType);
                setingRecylerViewSearch();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }




    public void settingPopularProductSlider() {
        // START VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES
        sliderPopularProductList = new ArrayList<>();
        Product p = new Product();
        sliderPopularProductList.add(p);
        // END VIEWPAGER POPULAR PRODUCTS SLIDER IMAGES

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*START FOR NAVIGATION DRAWER*/
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        /*END FOR NAVIGATION DRAWER*/
        /*CLICK BTON SEARCH*/
     /*   btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seacrhed = etSearch.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "El texto es :" + seacrhed, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });*/

        return super.onOptionsItemSelected(item);
    }

    User loggedInUser;

    private void settingNavDrawer() {

        /*START OF NAVIGATION DRAWER */
        drawerLayout = findViewById(R.id.dl_navigation_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        loggedInUser = CustomSharedPrefs.getLoggedInUser(MainActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        //tvName.setText("Iniciar sesión / Regístrate aquí");
        ImageView ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);
        Picasso.get().load(R.drawable.profile_image).into(ivProfileImage);

        if (!CustomSharedPrefs.getProfileUrl(MainActivity.this).equals("")) {
            Picasso.get().load(CustomSharedPrefs.getProfileUrl(MainActivity.this)).into(ivProfileImage);
        }

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loggedInUser == null) {
                    Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                    intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                    startActivity(intentLogin);
                } else {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
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
                        Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_account:
                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intentLogin);
                        } else {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_sub_hombres:
                        String title_hombre="HOMBRE";
                        String idHombres="1";
                        Intent subHombres = new Intent(MainActivity.this, TypeActivity.class);
                        subHombres.putExtra("subTitleHombre",title_hombre);
                        subHombres.putExtra("subIdHombres",idHombres);
                        startActivity(subHombres);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_mujer:
                        String title_mujeres="MUJER";
                        String idMujeres="2";
                        Intent subMujeres = new Intent(MainActivity.this, TypeActivity.class);
                        subMujeres.putExtra("subTitleMujeres",title_mujeres);
                        subMujeres.putExtra("subIdMujeres",idMujeres);
                        startActivity(subMujeres);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_ninhos:
                        String title_ninhos="NIÑO";
                        String idNinhos="3";
                        Intent subNinhos = new Intent(MainActivity.this, TypeActivity.class);
                        subNinhos.putExtra("subTitleNinhos",title_ninhos);
                        subNinhos.putExtra("subIdNinhos",idNinhos);
                        startActivity(subNinhos);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_bebes:
                        String title_bebes="BEBE";
                        String idBebes="4";
                        Intent subBebes = new Intent(MainActivity.this, TypeActivity.class);
                        subBebes.putExtra("subTitleBebes",title_bebes);
                        subBebes.putExtra("subIdBebes",idBebes);
                        startActivity(subBebes);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_orders:

                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        } else {
                            Intent intentOrder = new Intent(MainActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                        break;

                    case R.id.nav_cart:
                        Intent intentCart = new Intent(MainActivity.this, CartActivity.class);
                        startActivity(intentCart);
                        break;

                    case R.id.nav_favourites:
                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        } else {
                            Intent intentFav = new Intent(MainActivity.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }
                        break;

                    case R.id.nav_logout:
                        if (loggedInUser != null) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Cerrar sesión")
                                    .setMessage("¿Quieres cerrar sesión?")
                                    .setIcon(R.drawable.ic_logout)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            CustomSharedPrefs.setProfileUrl(MainActivity.this, "");
                                            UtilityClass.signOutGoogle(mGoogleApiClient);
                                            UtilityClass.signOutFB();
                                            UtilityClass.signOutEmail(MainActivity.this);
                                            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intentLogin);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();

                        } else {
                            item.setTitle(getString(R.string.string_login));
                            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
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
