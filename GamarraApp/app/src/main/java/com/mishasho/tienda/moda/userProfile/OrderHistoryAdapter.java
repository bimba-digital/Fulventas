package com.mishasho.tienda.moda.userProfile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.model.Ordered;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private ArrayList<Ordered> orderedList;
    private Context context;

    public OrderHistoryAdapter(ArrayList<Ordered> orderedList, Context context) {
        this.orderedList = orderedList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Ordered ordered = orderedList.get(position);
        holder.tvOrderId.setText("Orden ID : " + ordered.getGenerated_order_id());
        holder.tvOrderMethod.setText("Método de pedido:" +  ordered.getOrder_method());
        holder.tvOrderStaus.setText("Estado del pedido: " + ordered.getOrder_status());
        holder.tvOrderTime.setText("Tiempo de la orden : " + ordered.getOrder_time());
        holder.tvOrderPrice.setText("Total Precio : " + CustomSharedPrefs.getCurrency(context) + ordered.getOrder_price());

        OrderedProductAdapter orderedProductAdapter = new OrderedProductAdapter(ordered.getOrderedProduct(), context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context.getApplicationContext());
        holder.rvOrderedProduct.setLayoutManager(mLayoutManager);
        holder.rvOrderedProduct.setAdapter(orderedProductAdapter);
    }

    @Override
    public int getItemCount() {
        return orderedList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvOrderId, tvOrderMethod, tvOrderStaus, tvOrderTime, tvOrderPrice;
        RecyclerView rvOrderedProduct;

        public ViewHolder(View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderMethod = itemView.findViewById(R.id.tv_order_method);
            tvOrderStaus = itemView.findViewById(R.id.tv_order_status);
            tvOrderTime = itemView.findViewById(R.id.tv_order_time);
            rvOrderedProduct = itemView.findViewById(R.id.rv_ordered_product);
            tvOrderPrice = itemView.findViewById(R.id.tv_order_price);

        }
    }
}


