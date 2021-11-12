package com.mishasho.tienda.moda.main;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.Types;
import com.mishasho.tienda.moda.type.TypeActivity;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;

import java.util.ArrayList;

public class TypesRecylerViewAdapter extends RecyclerView.Adapter<TypesRecylerViewAdapter.ViewHolder>{

    private ArrayList<Types> typesList;
    private Context context;

    public TypesRecylerViewAdapter(ArrayList<Types> typesList, Context context) {
        this.typesList = typesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_types, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Types types = typesList.get(position);

        //int imageId = context.getResources().getIdentifier(productCategory.getId(), "drawable", context.getPackageName());
        //holder.ivCategoryImage.setImageResource(imageId);

        String typeImg = Constants.CATEGORY_IMAGE_URL + types.getImage_name();
        Picasso.get().load(typeImg).into(holder.ivTypeImage);
        holder.tvTypeName.setText(types.getTitle());

    }

    @Override
    public int getItemCount() {
        return typesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView ivTypeImage;
        TextView tvTypeName;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivTypeImage = itemView.findViewById(R.id.iv_types_image);
            tvTypeName = itemView.findViewById(R.id.tv_types_name);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Types types = typesList.get(position);
            Intent intent = new Intent(context, TypeActivity.class);
            intent.putExtra(Constants.INTENT_TYPE_ID, UtilityClass.typeToJson(types));

            context.startActivity(intent);
        }
    }

}
