package com.mishasho.tienda.moda.type;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.model.TypeCategory;
import com.mishasho.tienda.moda.model.Types;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.UtilityClass;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeActivity extends ActivityBaseCartIcon
        implements View.OnClickListener {

    // SPONSORED PRODUCTS GRID



    /*START OF MENU VERIABLE*/
    Toolbar toolbar;
    Button btnAddCart;
    /*END OF MENU VERIABLE*/

    /*ENCABEZADO */
    TextView type_nombre;
    ArrayList<TypeCategory> typeCategoriaList;
    RecyclerView recyclerViewTypeCategoria;
    private Types typesItem;
    String hombre_title,mujer_title,ninho_title,bebe_title;
    String hombre_id,mujer_id,ninho_id,bebe_id;

    private boolean networkOK;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_categoria);

        TextView txt1 = findViewById(R.id.list_categoria);
        txt1.setText(Html.fromHtml("<u>"+ this.getString(R.string.type_list_categoria)+ "</u> "  ));
        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_types_categoria));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/
        typesItem =UtilityClass.jsonToTypes(getIntent().getStringExtra(Constants.INTENT_TYPE_ID));
        type_nombre = findViewById(R.id.id_type_nombre);

            String dado = getIntent().getStringExtra(Constants.INTENT_TYPE_ID);
        Log.d("XD", dado.toString());

        //type_nombre.setText(typesItem.getTitle());
        //getTypeCategory();
        recyclerViewTypeCategoria = findViewById(R.id.rv_type_categoria_grid);
        typeCategoriaList = new ArrayList<>();


        recibirDatos();


    }

    private void recibirDatos() {
        Bundle extras = getIntent().getExtras();
        hombre_title = extras.getString("subTitleHombre");
        hombre_id = extras.getString("subIdHombres");

        mujer_title = extras.getString("subTitleMujeres");
        mujer_id = extras.getString("subIdMujeres");

        ninho_title = extras.getString("subTitleNinhos");
        ninho_id = extras.getString("subIdNinhos");

        bebe_title = extras.getString("subTitleBebes");
        bebe_id = extras.getString("subIdBebes");


        if(hombre_title != null){
            getTypeCategory(hombre_id);
           // type_nombre.setText(hombre_title);
            type_nombre.setText(Html.fromHtml("<u>"+ hombre_title+ "</u> "  ));
        }else  if(mujer_title != null){
            getTypeCategory(mujer_id);
            //type_nombre.setText(mujer_title);
            type_nombre.setText(Html.fromHtml("<u>"+ mujer_title+ "</u> "  ));
        }else  if(ninho_title != null){
            //type_nombre.setText(ninho_title);
            type_nombre.setText(Html.fromHtml("<u>"+ ninho_title+ "</u> "  ));
            getTypeCategory(ninho_id);
        }else  if(bebe_title != null){
            //type_nombre.setText(bebe_title);
            type_nombre.setText(Html.fromHtml("<u>"+ bebe_title+ "</u> "  ));
            getTypeCategory(bebe_id);
        }else {
            //type_nombre.setText(typesItem.getTitle());
            type_nombre.setText(Html.fromHtml("<u>"+ typesItem.getTitle()+ "</u> "  ));
            getTypeCategory("");
        }

    }

    @Override
    public void onClick(View v) {
       //switch (v.getId()) {
           // case R.id.btn_product_detail_add_cart:


       // }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // settingNavDrawer();
        invalidateOptionsMenu();
    }

    private void getTypeCategory(String id){

        //recoger los datos de la webservices
        //String JSON_URL = Constants.TYPE_CATEGORIA +"?types=" +typesItem.getId();
        String JSON_URL;
        if(id !=""){
            JSON_URL = Constants.TYPE_CATEGORIA +"?types=" + id;
        }else {

            JSON_URL = Constants.TYPE_CATEGORIA +"?types=" + typesItem.getId();
        }

        Log.d("UnoX", JSON_URL.toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<TypeCategory>>() {
                }.getType();
                typeCategoriaList = new Gson().fromJson(response, listType);
                settingRecylerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }


    private void settingRecylerView(){
        TypesCategoriaRecylerViewAdapter typesCategoriaRecylerViewAdapter = new TypesCategoriaRecylerViewAdapter(typeCategoriaList, this);

        recyclerViewTypeCategoria.setHasFixedSize(false);
        recyclerViewTypeCategoria.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerViewTypeCategoria.setNestedScrollingEnabled(false);
        recyclerViewTypeCategoria.setAdapter(typesCategoriaRecylerViewAdapter);
    }

}
