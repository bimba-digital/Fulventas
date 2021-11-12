package com.mishasho.tienda.moda.userProfile;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.main.MainActivity;
import com.mishasho.tienda.moda.model.Product;

import java.util.ArrayList;

public class UserFavActivity extends ActivityBaseCartIcon {

    /*START OF MENU VERIABLE*/
    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;
    /*END OF MENU VERIABLE*/

    ArrayList<Product> productGridList;
    RecyclerView recyclerViewProduct;

    private boolean networkOK;

    private DataSource dataSource;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fav);

        /*START OF TOOLBAR */
        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbarLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserFavActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setTitle("");
        toolbarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);
        toobarTitle.setText(this.getString(R.string.app_name));

        /*CHECKING THE TOOLBAR TITLE FROM USER FAVOURITES*/
        String toolbarTitle = getIntent().getStringExtra(UtilityClass.TOOLBAR_TITLE);
        if (toolbarTitle != null) {

            toobarTitle.setText(toolbarTitle);
            toolbarLogo.setVisibility(View.GONE);
            toobarTitle.setVisibility(View.VISIBLE);

        }
        /*CHECKING THE TOOLBAR TITLE FROM USER FAVOURITES*/

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*END OF TOOLBAR */

        recyclerViewProduct = findViewById(R.id.rv_user_fav_product_grid);
        dataSource = new DataSource(this);
        productGridList = (ArrayList<Product>) dataSource.getAllFavProducts();

        Log.d("productGridList", new Gson().toJson(productGridList).toString());


        settingRecylerView();

    }

    private void settingRecylerView() {
        FavProductRecylerViewAdapter favProductRecylerViewAdapter = new FavProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(favProductRecylerViewAdapter);
    }


}
