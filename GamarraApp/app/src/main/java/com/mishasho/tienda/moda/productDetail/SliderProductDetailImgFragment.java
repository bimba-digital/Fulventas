package com.mishasho.tienda.moda.productDetail;


import android.graphics.Matrix;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * A simple {@link Fragment} subclass.
 */
public class SliderProductDetailImgFragment extends Fragment {

    public static final String IMAGE_KEY = "IMAGE_KEY";

    ScaleGestureDetector scaleGestureDetector;
    PhotoViewAttacher photoViewAttacher;
    Matrix matrix;

    ImageView imageView;

    public static SliderProductDetailImgFragment newInstance(String imgName) {

        Bundle args = new Bundle();
        args.putString(IMAGE_KEY, imgName);

        SliderProductDetailImgFragment fragment = new SliderProductDetailImgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.item_slider_fragment_product_detail_full_img, container, false);

        Bundle args = getArguments();
        if(args == null) throw  new AssertionError();
        final String imgName = args.getString(IMAGE_KEY);
        if(imgName == null) throw  new AssertionError();

        imageView = rootView.findViewById(R.id.iv_product_detail_image_img);
        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + imgName;
        Picasso.get().load(imageUrl).into(imageView);

        photoViewAttacher=  new PhotoViewAttacher(imageView);
        matrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        return rootView;
    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float SF = detector.getScaleFactor();
            SF = Math.max(0.1f, Math.min(SF, 0.5f));
            matrix.setScale(SF, SF);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }

}
