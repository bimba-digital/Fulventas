package com.mishasho.tienda.moda.type;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mishasho.tienda.moda.model.TypeCategory;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.prductGrid.ProductGridActivity;
import com.mishasho.tienda.moda.R;

import java.util.ArrayList;

public class TypesCategoriaRecylerViewAdapter extends RecyclerView.Adapter<TypesCategoriaRecylerViewAdapter.ViewHolder>{

    private ArrayList<TypeCategory> typeCategoryList;
    private Context context;

    public TypesCategoriaRecylerViewAdapter(ArrayList<TypeCategory> typeCategoryList, Context context) {
        this.typeCategoryList = typeCategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_type_category, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TypeCategory typeCategory = typeCategoryList.get(position);
        holder.tvCategoryName.setText(typeCategory.getTitle() + " (" + typeCategory.getCount() + ")");
//        if((position % 2) == 0)
//            holder.tvCategoryName.setBackgroundColor(ContextCompat.getColor(context, R.color.lightgray) );
//        else
//            holder.tvCategoryName.setBackgroundColor(ContextCompat.getColor(context, R.color.rnColorUno) );


    }

    @Override
    public int getItemCount() {
        return typeCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{


        TextView tvCategoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvCategoryName = itemView.findViewById(R.id.tv_type_categoria_name);
        }
        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            TypeCategory typeCategory = typeCategoryList.get(position);
            String idCategory = String.valueOf(typeCategory.getId());
            String nombreCategoria = String.valueOf(typeCategory.getTitle());
            if (typeCategory.getCount() > 0) {

                Intent intent = new Intent(context, ProductGridActivity.class);
                intent.putExtra(Constants.INTENT_CATEGORY_ID, idCategory);
                intent.putExtra(Constants.INTENT_CATEGORY_NAME, nombreCategoria);
                context.startActivity(intent);
            } else {
                Toast.makeText(context,"NO existe productos en esa categoría", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
