package com.mishasho.tienda.moda.userProfile;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.model.OrderedProduct;

import java.util.ArrayList;

public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.ViewHolder> {

    private ArrayList<OrderedProduct> orderedProductList;
    private Context context;

    public OrderedProductAdapter(ArrayList<OrderedProduct> orderedProductList, Context context) {
        this.orderedProductList = orderedProductList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordered_product, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderedProduct orderedProduct = orderedProductList.get(position);
        holder.tvOPProductTitle.setText(orderedProduct.getProduct_title());

        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + orderedProduct.getProduct_image();
        Picasso.get().load(imageUrl).into(holder.ivOPProductImage);

        String opColor = "", opSize = "";
        if(orderedProduct.getProduct_color() == null) opColor = "N/A";
        else opColor = orderedProduct.getProduct_color();
        if(orderedProduct.getProduct_size() == null) opSize = "N/A";
        else opSize = orderedProduct.getProduct_size();

        double totalPrice = orderedProduct.getOrdered_quantity() * orderedProduct.getProduct_price();

        holder.tvOPProductInfo.setText("Color : " + opColor + ", Talla : "+ opSize);
        holder.tvOPProductPrice.setText("Precio : " + orderedProduct.getOrdered_quantity() + " X " +
                CustomSharedPrefs.getCurrency(context) + orderedProduct.getProduct_price() + " = " + CustomSharedPrefs.getCurrency(context) + totalPrice);

    }

    @Override
    public int getItemCount() {
        return orderedProductList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvOPProductTitle, tvOPProductInfo, tvOPProductPrice;
        public ImageView ivOPProductImage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvOPProductTitle = itemView.findViewById(R.id.tv_op_product_title);
            ivOPProductImage = itemView.findViewById(R.id.iv_op_product_image);
            tvOPProductInfo = itemView.findViewById(R.id.tv_op_product_info);
            tvOPProductPrice = itemView.findViewById(R.id.tv_op_product_price);


        }
    }
}


