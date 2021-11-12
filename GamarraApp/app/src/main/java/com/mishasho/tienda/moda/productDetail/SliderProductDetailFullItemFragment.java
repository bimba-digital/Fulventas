package com.mishasho.tienda.moda.productDetail;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mishasho.tienda.moda.model.Product;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * A simple {@link Fragment} subclass.
 */
public class SliderProductDetailFullItemFragment extends Fragment {


    public static final String IMAGE_KEY = "IMAGE_KEY";

    PhotoViewAttacher photoViewAttacher;

    public SliderProductDetailFullItemFragment() {
        // Required empty public constructor
    }


    public List<String> demo = new ArrayList<>();

    public static SliderProductDetailFullItemFragment newInstance(String imgName , Product mproduct) {

        Bundle args = new Bundle();
        args.putString(IMAGE_KEY, imgName);
        args.putParcelable("producto", mproduct);

        SliderProductDetailFullItemFragment fragment = new SliderProductDetailFullItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.item_slider_fragment_product_detail_full, container, false);

        Bundle args = getArguments();
        if(args == null) throw  new AssertionError();
        final String imgName = args.getString(IMAGE_KEY);
        if(imgName == null) throw  new AssertionError();
        final Product product = args.getParcelable("producto");

        final ImageView ivProductDetailImage = rootView.findViewById(R.id.iv_product_detail_image);
       // photoViewAttacher=  new PhotoViewAttacher(ivProductDetailImage);

        //Toast.makeText(getApplicationContext(), "Listado::  " +imgName, Toast.LENGTH_LONG).show();

        demo.add(imgName);
        ivProductDetailImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                Intent intent = new Intent(getContext(), SliderProductDetailFragment.class);
                intent.putExtra ("producto", product);
                getContext().startActivity(intent);

            }
        });

        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + imgName;
        Picasso.get().load(imageUrl).into(ivProductDetailImage);

        return rootView;
    }



}
