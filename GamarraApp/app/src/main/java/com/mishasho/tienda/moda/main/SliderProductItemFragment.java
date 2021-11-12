package com.mishasho.tienda.moda.main;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.productDetail.ProductDetailActivity;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.userLogin.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SliderProductItemFragment extends Fragment{


    public static final String PRODUCT_KEY = "product_key";

    private LikeButton btnSliderFavourite;
    private Product product;
    private DataSource dataSource;


    public SliderProductItemFragment() {
        // Required empty public constructor
    }

    public static SliderProductItemFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putParcelable(PRODUCT_KEY, product);
        SliderProductItemFragment fragment = new SliderProductItemFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataSource = new DataSource(getContext());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.item_slider_fragment_product, container, false);

        Bundle args = getArguments();
        if (args == null) throw new AssertionError();
        product = args.getParcelable(PRODUCT_KEY);
        if (product == null) throw new AssertionError();

        TextView tvSliderProductHeading = rootView.findViewById(R.id.tv_slider_product_heading);
        tvSliderProductHeading.setText(product.getTitle());

        TextView tvSliderProductPrevPrice = rootView.findViewById(R.id.tv_slider_product_Previous_price);
        if (product.getPrevious_price() != 0) {
            tvSliderProductPrevPrice.setText(UtilityClass.getNumberFormat(getContext(), product.getPrevious_price()));
        } else {
            tvSliderProductPrevPrice.setVisibility(View.INVISIBLE);
        }

        TextView tvSliderProductPrice = rootView.findViewById(R.id.tv_slider_product_price);
        tvSliderProductPrice.setText(UtilityClass.getNumberFormat(getContext(), Double.valueOf(product.getPrice())));


        String[] favProductIds = CustomSharedPrefs.getFavProductsInPref(getContext(), dataSource);
        for (int i = 0; i < favProductIds.length; i++) {
            if(favProductIds[i].equals(product.getId())){
                btnSliderFavourite.setLiked(true);
            }
        }

        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + product.getImage_name();
        Picasso.get().load(imageUrl).into((ImageView) rootView.findViewById(R.id.iv_slider_product_image));


        btnSliderFavourite = rootView.findViewById(R.id.btn_slider_favourite);
        btnSliderFavourite.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {

                if(CustomSharedPrefs.getLoggedInUser(getContext()) != null) {

                    Product currentProduct = product;
                    currentProduct.setUser_id(CustomSharedPrefs.getLoggedInUserId(getContext()));
                    dataSource.addFavProduct(currentProduct);
                    CustomSharedPrefs.setFavProductsInPref(getContext(), dataSource);

                }else{

                    Intent intentLogin = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(intentLogin);
                    likeButton.setLiked(false);
                    CustomSharedPrefs.setFavProductsInPref(getContext(), dataSource);

                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Product currentProduct = product;
                dataSource.removeFavProductById(currentProduct.getId());
            }

        });

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("name", product.getTitle());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("descripcion", product.getDescription());
               // intent.putExtra("store", product.getStore());
               // intent.putExtra("gallery", product.getGallery());
                startActivity(intent);

            }
        });

        return rootView;

    }



}
