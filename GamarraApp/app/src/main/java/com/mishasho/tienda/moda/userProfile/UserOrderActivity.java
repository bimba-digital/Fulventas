package com.mishasho.tienda.moda.userProfile;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.model.Ordered;
import com.mishasho.tienda.moda.model.OrderedProduct;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.userLogin.LoginActivity;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserOrderActivity extends ActivityBaseCartIcon {


    RelativeLayout pbarContainer;

    /*START OF MENU VERIABLE*/
    androidx.appcompat.widget.Toolbar toolbar;
    ArrayList<Ordered> orderedList;
    ArrayList<OrderedProduct> orderedProductList;
    User loggedInUser;

    RecyclerView rvOrderHistory;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UtilityClass.cartEmptyRedirect(UserOrderActivity.this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        UtilityClass.cartEmptyRedirect(UserOrderActivity.this);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        pbarContainer = findViewById(R.id.pbar_container);
        pbarContainer.setVisibility(View.VISIBLE);

        /*START OF TOOLBAR*/
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(getString(R.string.title_orders));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR*/

        rvOrderHistory = findViewById(R.id.rv_order_history);

        /*START OF ORDEDRED PRODUCTS */

        loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
        if (loggedInUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {

            String JSON_URL = Constants.ORDERED_BY_CUSTOMER + "?user_id=" + loggedInUser.getUser_id();
            Log.d("JSON_URLJSON_URL", JSON_URL);
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Type listType = new TypeToken<List<Ordered>>() { }.getType();
                    orderedList = new Gson().fromJson(response, listType);
                    StringBuilder stringBuilder = new StringBuilder();
                    int counter = 0;
                    for (Ordered o: orderedList) {
                        counter++;
                        stringBuilder.append(o.getOrder_id());
                        if(counter == orderedList.size()) continue;
                        stringBuilder.append(",");
                    }
                    getOrderedProductsByOrderId(stringBuilder.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.getCache().clear();
            queue.add(stringRequest);
        }
    }


    private void getOrderedProductsByOrderId(String ordersId){
        String JSON_URL = Constants.ORDERED_PRODUCT_BY_ORDERID + "?order_id=" + ordersId;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listType = new TypeToken<List<OrderedProduct>>() { }.getType();
                orderedProductList = new Gson().fromJson(response, listType);

                for (Ordered o: orderedList) {
                    ArrayList<OrderedProduct> orderedProductsOfOrder = new ArrayList<>();
                    for (OrderedProduct op: orderedProductList) {
                        if(op.getOrder_id() == o.getOrder_id()){
                            orderedProductsOfOrder.add(op);
                        }
                    }
                    o.setOrderedProduct(orderedProductsOfOrder);
                }

                setttingRVOrderHistory();
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

    private void setttingRVOrderHistory(){
        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(orderedList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvOrderHistory.setLayoutManager(mLayoutManager);
        rvOrderHistory.setAdapter(orderHistoryAdapter);
    }
}

