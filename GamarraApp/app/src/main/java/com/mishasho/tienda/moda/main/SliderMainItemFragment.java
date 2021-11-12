package com.mishasho.tienda.moda.main;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.model.SliderMain;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class SliderMainItemFragment extends Fragment {


    public static final String SLIDER_MAIN_KEY = "SLIDER_MAIN_KEY";

    public SliderMainItemFragment() {
        // Required empty public constructor
    }

    public static SliderMainItemFragment newInstance(SliderMain sliderMain) {

        Bundle args = new Bundle();
        args.putParcelable(SLIDER_MAIN_KEY, sliderMain);

        SliderMainItemFragment fragment = new SliderMainItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.item_slider_fragment_main, container, false);

        Bundle args = getArguments();
        if(args == null) throw new AssertionError();
        SliderMain sliderMain = args.getParcelable(SLIDER_MAIN_KEY);
        if(sliderMain == null) throw new AssertionError();

       // int imageResource = getActivity().getResources()
         //       .getIdentifier(sliderMain.getId(), "drawable", getActivity().getPackageName());


        ImageView ivSliderImage = rootView.findViewById(R.id.iv_slider_image);
        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + sliderMain.getId();
        Picasso.get().load(imageUrl).into(ivSliderImage);

      //  ivSliderImage.setImageResource(imageResource);

        TextView tvSliderHeading = rootView.findViewById(R.id.tv_slider_heading);
        tvSliderHeading.setText(sliderMain.getHeading());

        TextView tvSliderPreHeading = rootView.findViewById(R.id.tv_slider_preheading);
        tvSliderPreHeading.setText(sliderMain.getPreHeading());

        return rootView;
    }

}
