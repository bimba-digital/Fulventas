package com.mishasho.tienda.moda.adapter;

/**
 * Created by KottlandPro TET on 3/3/2018.
 */


import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.Types;
import com.mishasho.tienda.moda.type.TypeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// public class CardPagerAdapterS {


public class CardPagerAdapterS extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<Types> typesList;

    private Context context;
    private float mBaseElevation;
    public Button b1;

    public CardPagerAdapterS() {
        typesList = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItemS(Types item,Context context) {
        mViews.add(null);
        typesList.add(item);
        this.context = context;
    }



    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return typesList.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
              return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_recylerview_types, container, false);
        container.addView(view);
        bind(typesList.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Types types = typesList.get(position);
                Intent intent = new Intent(context, TypeActivity.class);
                intent.putExtra(Constants.INTENT_TYPE_ID, UtilityClass.typeToJson(types));
                context.startActivity(intent);

            }
        });
       /* if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }*/

       // cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Types item, View view)  {
        TextView contentTextView = (TextView) view.findViewById(R.id.tv_types_name);
        ImageView ivTypeImage = (ImageView) view.findViewById(R.id.iv_types_image);
        String typeImg = Constants.CATEGORY_IMAGE_URL + item.getImage_name();
        Picasso.get().load(typeImg).into(ivTypeImage);
        contentTextView.setText(item.getTitle());



    }

}

