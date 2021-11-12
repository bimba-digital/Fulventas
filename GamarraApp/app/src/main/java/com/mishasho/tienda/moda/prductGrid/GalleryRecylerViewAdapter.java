package com.mishasho.tienda.moda.prductGrid;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.main.CustomProductInventory;
import com.mishasho.tienda.moda.model.Gallery;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.search.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryRecylerViewAdapter extends RecyclerView.Adapter<GalleryRecylerViewAdapter.ViewHolder> {

    private ArrayList<Gallery> galleryList;

    private ArrayList<User> userList;

    //private ArrayList<User> userList;

    public Context context;


    // SPONSORED PRODUCTS GRID
    ArrayList<CustomProductInventory> customProductInventories;

    public String variableDemo;

    private DataSource dataSource;

    private User user;

    public GalleryRecylerViewAdapter(ArrayList<Gallery> galleryList, Context context) {
        this.galleryList = galleryList;
        this.context = context;

        dataSource = new DataSource(context);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_gallery, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gallery gallery = galleryList.get(position);
        //User user = getInventory(product.getUser_id());
        String imageUrl = Constants.RECENT_GALLERY_IMAGE_URL + gallery.getImage_name();
        Picasso.get().load(imageUrl).into(holder.ivGallerytImage);
        holder.tv_card_name.setText("Tienda: "+ gallery.getStore() );
    }



    @Override
    public int getItemCount() {
        return galleryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView ivGallerytImage;
        public TextView tv_card_name;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            ivGallerytImage = itemView.findViewById(R.id.iv_cart_gallery_image);
            tv_card_name = itemView.findViewById(R.id.tv_card_name);


        }


        @Override
        public void onClick(View v) {

           int position = getAdapterPosition();
            Gallery gallery = galleryList.get(position);
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("id_store",gallery.getUser_id());
            intent.putExtra("name_store",gallery.getStore());
             context.startActivity(intent);
        }
    }


}


