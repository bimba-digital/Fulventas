package com.mishasho.tienda.moda.productDetail;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class FotoFullActivity extends ActivityBaseCartIcon {

    private TouchImageView imageViewFotoTalla;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_foto);


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

        String ruta = getIntent().getStringExtra("ruta_full");

        imageViewFotoTalla = (TouchImageView)findViewById(R.id.rn_foto_medida_new);

        Picasso.get().load(ruta).into(imageViewFotoTalla);

    }
}
