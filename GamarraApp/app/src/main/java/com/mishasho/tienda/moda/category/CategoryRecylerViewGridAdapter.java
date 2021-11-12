package com.mishasho.tienda.moda.category;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.prductGrid.ProductGridActivity;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.model.ProductCategory;

import java.util.ArrayList;

public class CategoryRecylerViewGridAdapter extends RecyclerView.Adapter<CategoryRecylerViewGridAdapter.ViewHolder>{

    private ArrayList<ProductCategory> categoryList;
    private Context context;

    public CategoryRecylerViewGridAdapter(ArrayList<ProductCategory> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_grid_category, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductCategory productCategory = categoryList.get(position);

        String categoryImageUrl = Constants.CATEGORY_IMAGE_URL + productCategory.getImage_name();

        Picasso.get().load(categoryImageUrl).into(holder.ivCategoryImage);
        holder.tvCategoryName.setText(productCategory.getTitle());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView ivCategoryImage;
        TextView tvCategoryName;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivCategoryImage = itemView.findViewById(R.id.iv_category_image);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }
        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            ProductCategory productCategory = categoryList.get(position);

            Intent intent = new Intent(context, ProductGridActivity.class);
            intent.putExtra(Constants.INTENT_CATEGORY_ID, productCategory.getId());

            context.startActivity(intent);
        }

    }

}
