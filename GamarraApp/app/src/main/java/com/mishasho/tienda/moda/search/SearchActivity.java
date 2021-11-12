package com.mishasho.tienda.moda.search;

import android.content.Context;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.model.Gallery;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.prductGrid.GalleryRecylerViewAdapter;
import com.mishasho.tienda.moda.prductGrid.ProductRecylerViewAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends ActivityBaseCartIcon implements DialogSearchFragment  {

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;
    /*END OF MENU VERIABLE*/
    Context context;
    /*START OF NAVIGATION DRAWER*/
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;
    /*END OF NAVIGATION DRAWER*/
    public ArrayList<Product> productGridList;
    public ArrayList<String> searchListArray;
    public ArrayList<Gallery> gallerytGridList;
    //Toolbar toolbar;
    ImageButton btnSearchFiltre, btnList1;
    SearchView etSearch;
    TextView tvNoProducts, title_list;
    RecyclerView recyclerViewProduct, search_bar;
    public ArrayList<Product> searchList;
    ArrayList<Product> productList;
    ArrayList<Gallery> galleryList;
    RecyclerView rvSerachProductGrid;
    RecyclerView rvSerachGalleryGrid;
    RelativeLayout pbarContainer;

    String  tempRecibido;
    String mayorMe,menorMa;
    String etBuscar;
    private String JSON_URL;

    private boolean filtreList = true;


   /* public SearchActivity(String mayorMe,String menorMa, Context context) {
        this.mayorMe = mayorMe;
        this.menorMa=menorMa;
        this.context = context;
    }*/

    //COMENTADO
   @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        pbarContainer = findViewById(R.id.pbar_container);
        pbarContainer.setVisibility(View.GONE);
        tvNoProducts = findViewById(R.id.tv_no_products);


        settingToolBar();

        rvSerachProductGrid = findViewById(R.id.rv_serach_product_grid);
        rvSerachGalleryGrid = findViewById(R.id.rv_serach_gallery_grid);

        title_list =  findViewById(R.id.id_lista_title);


        btnList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filtreList){
                    settingProductsRV1();
                    filtreList = false;
                }else if(!filtreList)
                {
                    settingProductsRV();
                    filtreList = true;
                }

            }
        });
        /* agregado*/
       // mayorPrecio();

        btnSearchFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open_dialog(v);
                SearchFiltreActivity searchFiltreActivity = SearchFiltreActivity.newInstance();
                searchFiltreActivity.show(getFragmentManager(), "PRODUCT_DETAIL_DIALOG");

            }
        });
        recibirDatos();
        //makeSearcrh(temp,mayorMe);
        settingProductGrid();
        setingFiltre();

    }


    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        String id_btn = extras.getString("idbtn");
        String id_etex = extras.getString("idEtex");
        String idFiltre = extras.getString("idFiltre");
        int posFiltre = extras.getInt("position");

        String id_store = extras.getString("id_store");
        String name_store = extras.getString("name_store");

        /*mayor Precio*/
        String idMP = extras.getString("idmayorPrecio");
        String mayMenor = extras.getString("mayorPrecio");

       // Toast.makeText(this, mayMenor+ " "+idMP, Toast.LENGTH_LONG).show();
        if(id_btn !=null ||id_etex != null ){
            String d1 = extras.getString("seacrhedtext");
            title_list.setText(Html.fromHtml("<u>"+ "Listado de "+d1+ "</u> "  ));
              tempRecibido=d1.replace(" ", "%20");
            makeSearcrh(tempRecibido,null,null,0,0,null,null);
        }else if(idFiltre !=null){
            String filtre = extras.getString("seacrhedtext");
            title_list.setText(Html.fromHtml("<u>"+ "Galeria "+filtre+  "</u> "  ));
            tempRecibido=filtre.replace(" ", "%20");
            makeFiltre(tempRecibido,posFiltre);
        }else if(id_store !=null){
            title_list.setText(Html.fromHtml("<u>"+ "Tienda "+name_store+  "</u> "  ));
            makeFiltre_store(id_store);

        }

    }


   /* private void mayorPrecio(){
        Bundle extras = getIntent().getExtras();

       if(idMP.equals(null)){

           Toast.makeText(this, "Ver Search: "+ idMP, Toast.LENGTH_LONG).show();
       }

    }*/
 /*  private void search(String s){
       String JSON_URL = Constants.SEARCH;
       RequestQueue queue = Volley.newRequestQueue(this);

       StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL + "?search="+s, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

          //     Toast.makeText(getApplicationContext(), "Data: " + response, Toast.LENGTH_LONG).show();;
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
   }*/


   /* private void setingRecylerViewSearch() {

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
                etSearch.setText(textAdd);
                settingProductsRV();
               // etSearch.setText("");

            }
        });
    }*/

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

                    for (Product pro : productGridList) {
                        searchListArray.add(pro.getTitle());

                    }

                    final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) etSearch.findViewById(R.id.search_src_text);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(), R.layout.textview_auto_complete, searchListArray);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    searchAutoComplete.setAdapter(dataAdapter);

                    searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                            String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                            // searchAutoComplete.setText("");

                            etBuscar = queryString.toString().replace(" ", "%20");
                            makeSearcrh(etBuscar, null, null, 0, 0, null, null);


                        }


                    });
                    // Below event is triggered when submit search query.
                    etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            //  searchAutoComplete.setText("");
                            etBuscar = query.toString().replace(" ", "%20");
                            makeSearcrh(etBuscar, null, null, 0, 0, null, null);
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

    public void setingFiltre() {

                String JSON_URL = Constants.GALLERY_GRID_URL+ "?search=/gallery";
                RequestQueue queue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseV", response);
                        Type listType = new TypeToken<List<Gallery>>() {
                        }.getType();
                        gallerytGridList = new Gson().fromJson(response, listType);

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
                                etBuscar = queryString.toString().replace(" ", "%20");
                                makeFiltre(etBuscar,0);


                            }
                        });
                        // Below event is triggered when submit search query.
                        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                etBuscar = query.toString().replace(" ", "%20");
                                makeFiltre(etBuscar, 0);
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


    public void makeSearcrh(String seacrhedtext, String mayorM,String talla,int num1,int num2,String menorMa, String relevantes) {
        pbarContainer.setVisibility(View.VISIBLE);
        String JSON_URL;
          if(mayorM !=null){
              JSON_URL = Constants.SEARCH + "?search=" + seacrhedtext+"/"+mayorM;
          }else  if(talla!=null){
              JSON_URL = Constants.SEARCH + "?search=" + seacrhedtext+"/"+"tallas"+"/" +talla;
          }else if(num1>0 && num2>0){
              JSON_URL = Constants.SEARCH + "?search=" + seacrhedtext+"/"+"rangoprecio"+"/" +num1+"/"+num2;
          }else if(menorMa!=null){
              JSON_URL = Constants.SEARCH + "?search=" + seacrhedtext+"/"+menorMa;
          }else if(relevantes!=null){
              JSON_URL = Constants.SEARCH + "?search=" + seacrhedtext+"/"+relevantes;
          }
          else {
              JSON_URL = Constants.SEARCH + "?search=" + seacrhedtext;
          }
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                productList = new Gson().fromJson(response, listType);
                if(productList.size() > 0){

                    settingProductsRV();
                    tvNoProducts.setVisibility(View.GONE);
                    rvSerachProductGrid.setVisibility(View.VISIBLE);
                    rvSerachGalleryGrid.setVisibility(View.GONE);
                }else{
                    tvNoProducts.setVisibility(View.VISIBLE);
                    rvSerachProductGrid.setVisibility(View.GONE);
                    rvSerachGalleryGrid.setVisibility(View.GONE);
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

    }


    public void makeFiltre(String seacrhedtext, int position) {
        pbarContainer.setVisibility(View.VISIBLE);
        JSON_URL = Constants.SEARCH_STORE_GALLERY + "?search=" + seacrhedtext;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listType = new TypeToken<List<Gallery>>() {
                }.getType();
                galleryList = new Gson().fromJson(response, listType);
                if(galleryList.size() > 0){
                    settingGalleryRV();
                    tvNoProducts.setVisibility(View.GONE);
                    rvSerachGalleryGrid.setVisibility(View.VISIBLE);
                }else{
                    tvNoProducts.setVisibility(View.VISIBLE);
                    rvSerachProductGrid.setVisibility(View.GONE);
                    rvSerachGalleryGrid.setVisibility(View.GONE);
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
    }

    public void makeFiltre_store(String id_store) {
        pbarContainer.setVisibility(View.VISIBLE);
        JSON_URL = Constants.SEARCH_STORE + "?search=" + id_store + "/store";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                productList = new Gson().fromJson(response, listType);
                if(productList.size() > 0){

                    settingProductsRV();
                    tvNoProducts.setVisibility(View.GONE);
                    rvSerachProductGrid.setVisibility(View.VISIBLE);
                    rvSerachGalleryGrid.setVisibility(View.GONE);
                }else{
                    tvNoProducts.setVisibility(View.VISIBLE);
                    rvSerachProductGrid.setVisibility(View.GONE);
                    rvSerachGalleryGrid.setVisibility(View.GONE);
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
    }

    private void settingProductsRV1() {
        ProductRecylerViewAdapter productRecylerViewAdapter =
                new ProductRecylerViewAdapter(productList, this);

        rvSerachProductGrid.setHasFixedSize(false);
        rvSerachProductGrid.setLayoutManager(new GridLayoutManager(this, 1));
        rvSerachProductGrid.setNestedScrollingEnabled(false);
        rvSerachProductGrid.setAdapter(productRecylerViewAdapter);
    }



    private void settingProductsRV() {
        ProductRecylerViewAdapter productRecylerViewAdapter =
                new ProductRecylerViewAdapter(productList, this);

        rvSerachProductGrid.setHasFixedSize(false);
        rvSerachProductGrid.setLayoutManager(new GridLayoutManager(this, 2));
        rvSerachProductGrid.setNestedScrollingEnabled(false);
        rvSerachProductGrid.setAdapter(productRecylerViewAdapter);
    }

    private void settingGalleryRV() {
        GalleryRecylerViewAdapter galleryRecylerViewAdapter =
                new GalleryRecylerViewAdapter(galleryList, this);

        rvSerachGalleryGrid.setHasFixedSize(false);
        rvSerachGalleryGrid.setLayoutManager(new GridLayoutManager(this, 1));
        rvSerachGalleryGrid.setNestedScrollingEnabled(true);
        rvSerachGalleryGrid.setAdapter(galleryRecylerViewAdapter);
    }





    public void settingToolBar() {

        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toolbarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);

      //  btnClear = findViewById(R.id.btn_clear);
        etSearch = findViewById(R.id.et_seach_product);
        btnSearchFiltre = findViewById(R.id.btn_filtre);
        btnList1 = findViewById(R.id.btn_filtre_list);
        btnSearchFiltre.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

        /*START OF TOOLBAR */

       // toolbar = findViewById(R.id.toolbar);
       //setSupportActionBar(toolbar);
        //comentado
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR */

       /* etSearch.addTextChangedListener(new TextWatcher() {
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

                        etBuscar=s.toString().replace(" ", "%20");
                            makeSearcrh(etBuscar,null,null,0,0,null,null);


                    }else{
                        //searchList.clear();
                        //search(null);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        });*/


    }

    @Override
    public void getMayorMenor(String selected) {
       // temp = selected;
        if(etBuscar!=null){
            makeSearcrh(etBuscar,selected,null,0,0,null,null);
        }else {
            makeSearcrh(tempRecibido,selected,null,0,0,null,null);
        }
    }

    @Override
    public void getMenorMayor(String menorMa) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,null,0,0,menorMa,null);
        }else {
            makeSearcrh(tempRecibido,null,null,0,0,menorMa,null);
        }
    }

    @Override
    public void getRelevantes(String relevante) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,null,0,0,null,relevante);
        }else {
            makeSearcrh(tempRecibido,null,null,0,0,null,relevante);
        }
    }

    @Override
    public void getTallas( String select) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,select,0,0,null,null);
        }else {
            makeSearcrh(tempRecibido,null,select,0,0,null,null);
        }

    }

    @Override
    public void getRango(int num1, int num2) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,null,num1,num2,null,null);
        }else {
            makeSearcrh(tempRecibido,null,null,num1,num2,null,null);
        }
    }

}
