package com.mishasho.tienda.moda.userProfile;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.productDetail.ProductDetailActivity;
import com.mishasho.tienda.moda.userLogin.LoginActivity;

import java.util.ArrayList;

public class FavProductRecylerViewAdapter extends RecyclerView.Adapter<FavProductRecylerViewAdapter.ViewHolder> {

    private ArrayList<Product> productList;
    private Context context;
    private Product product;
    private DataSource dataSource;

    public FavProductRecylerViewAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        dataSource = new DataSource(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_product, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       product = productList.get(position);


        String[] favProductIds = CustomSharedPrefs.getFavProductsInPref(context, dataSource);
        for (int i = 0; i < favProductIds.length; i++) {
            if(favProductIds[i].equals(product.getId())){
                holder.btnFavourite.setLiked(true);
            }
        }

        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + product.getImage_name();
        Picasso.get().load(imageUrl).into(holder.ivProductImage);

        holder.tvProductHeading.setText(product.getTitle());
        holder.tvProductPrice.setText(UtilityClass.getNumberFormat(context, Double.valueOf(product.getPrice())));
      //  holder.tvProductStore.setText("Tienda: " +  product.getStore());
        holder.tvProductGallery.setText("Galeria: "+ product.getGallery());
       // holder.tvProductStock.setText("Stock: " + product.getQty());
        holder.tvProductMarca.setText("Marca: "+ product.getBrand() );

        if (product.getPrevious_price() != 0) {
            holder.tvGridProductPrevPrice.setText(UtilityClass.getNumberFormat(context, product.getPrevious_price()));
        } else {
            holder.tvGridProductPrevPrice.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, OnLikeListener{

        public ImageView ivProductImage;
        public TextView tvProductHeading;
        public TextView tvProductPrice;
        public TextView tvGridProductPrevPrice;

        public  LikeButton btnFavourite;

        public TextView tvProductGallery;
        public TextView tvProductStore;

        public TextView tvProductStock;
        public TextView tvProductMarca;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivProductImage = itemView.findViewById(R.id.iv_grid_product_image);
            tvProductHeading = itemView.findViewById(R.id.tv_grid_product_heading);
            tvProductPrice = itemView.findViewById(R.id.tv_grid_product_price);
            tvGridProductPrevPrice = itemView.findViewById(R.id.tv_grid_product_Previous_price);

            tvProductGallery = itemView.findViewById(R.id.lblGaleria);
            tvProductStore = itemView.findViewById(R.id.lblTienda);

            tvProductStock = itemView.findViewById(R.id.lblstock);
            tvProductMarca = itemView.findViewById(R.id.lblmarca);

            btnFavourite = itemView.findViewById(R.id.btn_favourite);

            btnFavourite.setOnLikeListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Product product = productList.get(position);
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getTitle());
            intent.putExtra("price", product.getPrice());
            intent.putExtra(Constants.PRODUCT_DETAIL_INTENT, UtilityClass.productToJson(product));
            context.startActivity(intent);

        }

        @Override
        public void liked(LikeButton likeButton) {

            if(CustomSharedPrefs.getLoggedInUser(context) != null) {

                int position = getAdapterPosition();
                Product currentProduct = productList.get(position);
                currentProduct.setUser_id(CustomSharedPrefs.getLoggedInUserId(context));
                dataSource.addFavProduct(currentProduct);
                CustomSharedPrefs.setFavProductsInPref(context, dataSource);

            }else{

                Intent intentLogin = new Intent(context, LoginActivity.class);
                context.startActivity(intentLogin);
                likeButton.setLiked(false);
                CustomSharedPrefs.setFavProductsInPref(context, dataSource);

            }
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            int position = getAdapterPosition();
            Product currentProduct = productList.get(position);
            dataSource.removeFavProductById(currentProduct.getId());
        }

    }

}


