package com.mishasho.tienda.moda.prductGrid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import android.widget.TextView;
import android.widget.Toast;

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
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.Util.RequestPackage;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.search.DialogSearchFragment;
import com.mishasho.tienda.moda.search.SearchFiltreActivity;
import com.mishasho.tienda.moda.services.ProductGridGetService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductGridActivity extends ActivityBaseCartIcon implements DialogSearchFragment {

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;
    /*END OF MENU VERIABLE*/

    ArrayList<Product> productGridList;
    RecyclerView recyclerViewProduct;
    public ArrayList<String> searchListArray;
    ImageButton btnSearchFiltre, btnList1;
    SearchView etSearch;
    TextView tvNoProducts, title_list;
    private boolean networkOK;
    private boolean filtreList = true;
    String etBuscar;
    String  tempRecibido;
    String categoryId;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_grid);

        settingToolBar();

        /*CHECKING THE TOOLBAR TITLE FROM USER FAVOURITES*/

        title_list =  findViewById(R.id.id_lista_title);
        tvNoProducts = findViewById(R.id.tv_no_products);
        /*END OF TOOLBAR */

        recyclerViewProduct = findViewById(R.id.rv_product_grid);

         categoryId = getIntent().getStringExtra(Constants.INTENT_CATEGORY_ID);
        String categoryName = getIntent().getStringExtra(Constants.INTENT_CATEGORY_NAME);
        title_list.setText(Html.fromHtml("<u>"+ "Listado de "+categoryName+ "</u> "  ));

        Log.d("DosX", "Dos");


        /*START INTENT SERVICE*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(productBroadcastReceiver, new IntentFilter(ProductGridGetService.MY_MESSAGE_GET));

        networkOK = NetworkHelper.hasNetworkAccess(this);
        if (networkOK) {

            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(Constants.PRODUCT_GRID_URL);
            Log.d("DosXX", categoryId.toString());

            requestPackage.setParam("category", categoryId);

            Intent intentProduct = new Intent(this, ProductGridGetService.class);
            intentProduct.putExtra(ProductGridGetService.PRODUCT_REQUEST_PACKAGE, requestPackage);
            startService(intentProduct);


        } else {
            Toast.makeText(this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
        }
        /*END INTENT SERVICE*/

        btnList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filtreList){
                    settingRecylerView();
                    filtreList = false;
                }else if(!filtreList)
                {
                    settingRecylerView1();
                    filtreList = true;
                }

            }
        });
        btnSearchFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open_dialog(v);
                SearchFiltreActivity searchFiltreActivity = SearchFiltreActivity.newInstance();
                searchFiltreActivity.show(getFragmentManager(), "PRODUCT_DETAIL_DIALOG");

            }
        });
    }

    BroadcastReceiver productBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Product[] products = (Product[]) intent.getParcelableArrayExtra(ProductGridGetService.MY_PAYLOAD_GET);
            productGridList = new ArrayList<>(Arrays.asList(products));
            searchListArray = new ArrayList<>();
            if(productGridList.size() > 0){
                settingRecylerView1();
                tvNoProducts.setVisibility(View.GONE);
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

                        tempRecibido=queryString.replace(" ", "%20");
                        makeSearcrh(tempRecibido,null,null,0,0,null,null,categoryId);


                    }


                });
                // Below event is triggered when submit search query.
                etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //  searchAutoComplete.setText("");
                        tempRecibido=query.replace(" ", "%20");
                        makeSearcrh(tempRecibido,null,null,0,0,null,null,categoryId);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

            }else{
                tvNoProducts.setVisibility(View.VISIBLE);

            }




        }
    };

    private void settingRecylerView() {
        ProductRecylerViewAdapter productRecylerViewAdapter =
                new ProductRecylerViewAdapter(productGridList, this);
        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(productRecylerViewAdapter);
    }

    private void settingRecylerView1() {
        ProductRecylerViewAdapter productRecylerViewAdapter =
                new ProductRecylerViewAdapter(productGridList, this);
        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(productRecylerViewAdapter);
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




    }


    public void makeSearcrh(String seacrhedtext, String mayorM,String talla,int num1,int num2,String menorMa, String relevantes,String idCategoria) {

        String JSON_URL;
        if(mayorM !=null){
            JSON_URL = Constants.SEARCH + "?search=" + idCategoria+"-cat/"+mayorM;
        }else  if(talla!=null){
            JSON_URL = Constants.SEARCH + "?search=" +idCategoria+"-cat/"+"tallas"+"/" +talla;
        }else if(num1>0 && num2>0){
            JSON_URL = Constants.SEARCH + "?search=" +idCategoria+"-cat/"+"rangoprecio"+"/" +num1+"/"+num2;
        }else if(menorMa!=null){
            JSON_URL = Constants.SEARCH + "?search=" +idCategoria+"-cat/"+menorMa;
        }else if(relevantes!=null){
            JSON_URL = Constants.SEARCH + "?search=" +idCategoria+"-cat/"+relevantes;
        }
        else {
            JSON_URL = Constants.SEARCH + "?search="+idCategoria+"-cat/" + seacrhedtext;
        }
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                productGridList = new Gson().fromJson(response, listType);
                if(productGridList.size() > 0){
                    settingRecylerView1();
                    tvNoProducts.setVisibility(View.GONE);
                }else{
                    tvNoProducts.setVisibility(View.VISIBLE);

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

    @Override
    public void getMayorMenor(String selected) {
        // temp = selected;
        if(etBuscar!=null){
            makeSearcrh(etBuscar,selected,null,0,0,null,null,categoryId);
        }else {
            makeSearcrh(tempRecibido,selected,null,0,0,null,null,categoryId);
        }
    }

    @Override
    public void getMenorMayor(String menorMa) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,null,0,0,menorMa,null,categoryId);
        }else {
            makeSearcrh(tempRecibido,null,null,0,0,menorMa,null,categoryId);
        }
    }

    @Override
    public void getRelevantes(String relevante) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,null,0,0,null,relevante,categoryId);
        }else {
            makeSearcrh(tempRecibido,null,null,0,0,null,relevante,categoryId);
        }
    }

    @Override
    public void getTallas( String select) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,select,0,0,null,null,categoryId);
        }else {
            makeSearcrh(tempRecibido,null,select,0,0,null,null,categoryId);
        }

    }

    @Override
    public void getRango(int num1, int num2) {
        if(etBuscar!=null){
            makeSearcrh(etBuscar,null,null,num1,num2,null,null,categoryId);
        }else {
            makeSearcrh(tempRecibido,null,null,num1,num2,null,null,categoryId);
        }
    }

}
