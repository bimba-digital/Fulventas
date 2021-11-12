package com.mishasho.tienda.moda.searching;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mishasho.tienda.moda.R;

import com.mishasho.tienda.moda.model.Product;

import java.util.ArrayList;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private ArrayList<Product> resul;
    private Context context;

    //agregado
    private OnItemClickListener mListener;

    /*agregado */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /*agregado */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SearchAdapter(ArrayList<Product> result, Context contex){
        this.resul = result;
        this.context = contex;
//        Toast.makeText(context, "Result: " + result.size(), Toast.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_search, parent, false,mListener));
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_search, parent, false);
        ViewHolder evh = new ViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = resul.get(i);
        String title = product.getTitle();

        viewHolder.tv_result.setText(title);


       // viewHolder.
    }

    @Override
    public int getItemCount() {
        return resul.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardResult;
        private TextView tv_result;


        public ViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            cardResult = (CardView) itemView.findViewById(R.id.cardResult);
            tv_result = (TextView) itemView.findViewById(R.id.tv_result);
            /*agregado */
           // itemView.setOnClickListener(new View
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
