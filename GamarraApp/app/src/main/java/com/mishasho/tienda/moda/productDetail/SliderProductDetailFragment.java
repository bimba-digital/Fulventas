package com.mishasho.tienda.moda.productDetail;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.Util.RequestPackage;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.model.ProductImages;
import com.mishasho.tienda.moda.services.ProductImagesService;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class SliderProductDetailFragment extends AppCompatActivity {


    androidx.appcompat.widget.Toolbar toolbar;


    // public ArrayList<String> recuperamos_variable_string;



    public ArrayList<ProductImages> productDetailSliderImgList;
    public ArrayList<String> productDetailSliderImgNameList;

    private int productSlideCount;

    private LinearLayout dotsIndicators;

    private boolean networkOK;

    private Product mproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_img);

        //dataSource = new DataSource(this);

        // UtilityClass.buttonScaleIconRight(this, findViewById(R.id.btn_cart_checkout), R.drawable.ic_arrow_right_white, .8, 1.1);

        /*START OF TOOLBAR*/

        TextView toobarTitle = findViewById(R.id.toolbar_title_img);
        toolbar = findViewById(R.id.toolbar_img);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_cart_img));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { onBackPressed();
            }
        });


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mproduct = this.getIntent().getParcelableExtra("producto");
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(productImageBroadcastReceiver, new IntentFilter(ProductImagesService.PRODUCT_IMAGES_MESSAGE));


        networkOK = NetworkHelper.hasNetworkAccess(this);
        if (networkOK) {

            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(Constants.PRODUCT_IMAGES_URL);
            requestPackage.setParam("product_id", String.valueOf(mproduct.getId()));

            Intent intentProductImage = new Intent(this, ProductImagesService.class);
            intentProductImage.putExtra(ProductImagesService.PRODUCT_IMAGES_REQUEST_PACKAGE, requestPackage);
            startService(intentProductImage);

        } else {
            Toast.makeText(this, "No se pudo conectar a internet.", Toast.LENGTH_SHORT).show();
        }

        dotsIndicators = findViewById(R.id.layout_product_Detail_dots_img);
    }


    BroadcastReceiver productImageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ProductImages[] productImages = (ProductImages[]) intent.getParcelableArrayExtra(ProductImagesService.PRODUCT_IMAGES_PAYLOAD);
            productDetailSliderImgList = new ArrayList<>(Arrays.asList(productImages));


            productDetailSliderImgNameList = new ArrayList<>();
            for (ProductImages pImg : productDetailSliderImgList) {
                productDetailSliderImgNameList.add(pImg.getImage_name());

            }

            settingProductImageSlider();
            dots(0);
        }
    };

    private void settingProductImageSlider() {

        productSlideCount = productDetailSliderImgNameList.size();

        ViewPager vpProductDetailsImageSlider = findViewById(R.id.vp_slider_main_img);
        PagerAdapter pagerAdapter = new SliderMainAdapter(getSupportFragmentManager());
        vpProductDetailsImageSlider.setAdapter(pagerAdapter);

        vpProductDetailsImageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /*START OF MAIN SLIDER ADAPTER*/
    public class SliderMainAdapter extends FragmentStatePagerAdapter {

        public SliderMainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderProductDetailImgFragment.newInstance(productDetailSliderImgNameList.get(position));
        }

        @Override
        public int getCount() {
            return productDetailSliderImgList.size();
        }

    }

    private void dots(int current) {

        dotsIndicators.removeAllViews();
        for (int i = 0; i < productSlideCount; i++) {
            TextView dot = new TextView(this);
            dot.setIncludeFontPadding(false);
            dot.setHeight((int) UtilityClass.convertDpToPixel(10, SliderProductDetailFragment.this));
            dot.setWidth((int) UtilityClass.convertDpToPixel(10, SliderProductDetailFragment.this));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginAsDp = (int) UtilityClass.convertDpToPixel(4, SliderProductDetailFragment.this);
            params.setMargins(marginAsDp, marginAsDp, marginAsDp, marginAsDp);
            dot.setLayoutParams(params);

            dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_border_bg_1));

            if (i == current) {
                dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));
            }

            dotsIndicators.addView(dot);

        }
    }

}
