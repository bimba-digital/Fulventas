package com.mishasho.tienda.moda.productDetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.Util.CustomSharedPrefs;
import com.mishasho.tienda.moda.Util.NetworkHelper;
import com.mishasho.tienda.moda.Util.RequestPackage;
import com.mishasho.tienda.moda.baseActivity.ActivityBaseCartIcon;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.main.MainActivity;
import com.mishasho.tienda.moda.main.CustomProductInventory;
import com.mishasho.tienda.moda.model.Politics;
import com.mishasho.tienda.moda.model.ProductColor;
import com.mishasho.tienda.moda.model.ProductImages;
import com.mishasho.tienda.moda.model.ProductSize;
import com.mishasho.tienda.moda.model.SelectedProduct;
import com.mishasho.tienda.moda.model.User;
import com.mishasho.tienda.moda.prductGrid.ProductRecylerViewAdapter;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.cart.CartActivity;
import com.mishasho.tienda.moda.model.Product;
import com.mishasho.tienda.moda.search.SearchActivity;
import com.mishasho.tienda.moda.services.ProductImagesService;
import com.mishasho.tienda.moda.type.TypeActivity;
import com.mishasho.tienda.moda.userLogin.LoginActivity;
import com.mishasho.tienda.moda.userProfile.ProfileActivity;
import com.mishasho.tienda.moda.userProfile.UserFavActivity;
import com.mishasho.tienda.moda.userProfile.UserOrderActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends ActivityBaseCartIcon
        implements View.OnClickListener, OnLikeListener {

    public SelectedProduct mSelectedProduct;

    public ArrayList<ProductImages> productDetailSliderImgList;
    public ArrayList<String> productDetailSliderImgNameList;

    private int productSlideCount;
    private LinearLayout dotsIndicators;
    private TextView tvProductDetailHeading;


    private ImageView imageViewFotoTalla;

    // SPONSORED PRODUCTS GRID
    ArrayList<CustomProductInventory> customProductInventories;
    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;

    ArrayList<CustomProductInventory> customProductInventoryList;
    ArrayList<Product> productGridList;
    ArrayList<Politics> politicsList;
    RecyclerView recyclerViewProduct;
    public ArrayList<String> searchListArray;
    /*START OF MENU VERIABLE*/
    Button btnAddCart;
    /*END OF MENU VERIABLE*/

    // START OF DESCRIPTION AREA
    int firstDescLineCount;
    int firstDescLineHeight;
    int firstDescVisibleLineCount = 4;

    // END OF DESCRIPTION AREA
    TextView tvNoProducts, title_list;
    LikeButton btnPDetailFavourite;

    private DataSource dataSource;
    private Product mproduct;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;

    // Share button for Social media
    private ImageView imgCompartir;

    private boolean networkOK;
    ImageButton btnSearchFiltre, btnList1;
    SearchView etSearch;
    /*add carrito directo */
    /*START OF QUANTITY PLUS/MINUS */
    LinearLayout btnDialogQtyPlus;
    LinearLayout btnDialogQtyMinus;
    Button btnDialogQty;
    int productQty = 1;
    //spinner_color
    private Spinner spinner_talla ;
    public ArrayList<String> tallas;
    public ArrayList<String> colores;

    private String item_talla;
    private String item_color;

    ArrayList<ProductSize> productSizes;
    ArrayList<ProductColor> productColors;

    CustomProductInventory customProductoItem;

    ArrayList<CustomProductInventory> inventoryList;

    public int countTalla;
    public int countColor;
    /*end*/
    String productid;
    int available_qty_response;
    TextView tvPDetailDescription, tvPreviousPrice, tvPrice, tv_product_detail_descripcion,tvpAdditional,tvpPolitic;

    private List<SelectedProduct> selectedProductList;
      public ArrayList<String> listIdTemp;
   // String[] nombres = {"Marta","Ana","Luis","Rodrigo","Virginia"};
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        settingToolBar();
        title_list =  findViewById(R.id.id_lista_title);
        title_list.setText(Html.fromHtml("<u>"+ this.getString(R.string.title_product_detail)+ "</u> "  ));

        dataSource = new DataSource(this);
        mproduct = UtilityClass.jsonToProduct(getIntent().getStringExtra(Constants.PRODUCT_DETAIL_INTENT));

        tvProductDetailHeading = findViewById(R.id.tv_product_detail_heading);
        tvProductDetailHeading.setText(mproduct.getTitle());

        Log.d("TresX", mproduct.getFotos_talla().toString());

        imageViewFotoTalla = (ImageView)findViewById(R.id.rn_foto_medida);


        // Funcionalidad compartir
        imgCompartir = (ImageView) findViewById(R.id.btn_share);

        imgCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoCompartir = "Para ver más productos de "+ mproduct.getTitle() + " en intent://#Intent;scheme=com.mishasho.tienda.moda;package=com.mishasho.tienda.moda;end  \n \n\n. Si no tienes instalada la App Gamarrita descarga aqui https://bit.ly/gamarrita ";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textoCompartir);
                sendIntent.setType("text/html");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        // START OF FAVOURITE BUTTON
        btnPDetailFavourite = findViewById(R.id.btn_p_detail_favourite);
        btnPDetailFavourite.setOnLikeListener(this);

        String pId = String.valueOf(mproduct.getId());

        String[] favProductIds = CustomSharedPrefs.getFavProductsInPref(this, dataSource);
        for (int i = 0; i < favProductIds.length; i++) {
            if (favProductIds[i].equals(pId)) {
                btnPDetailFavourite.setLiked(true);
            }
        }
        settingProductGrid();
        // END OF FAVOURITE BUTTON

        tvPDetailDescription = findViewById(R.id.tv_p_detail_description);
        tvPreviousPrice = findViewById(R.id.tv_product_detail_pre_price);
        tvPrice = findViewById(R.id.tv_product_detail_price);
        tv_product_detail_descripcion = findViewById(R.id.tv_product_detail_descripcion);

        tvPDetailDescription.setText(mproduct.getDescription());
        tvPreviousPrice.setText(UtilityClass.getNumberFormat(this, mproduct.getPrevious_price()));
        tvPrice.setText(UtilityClass.getNumberFormat(this, mproduct.getPrice()));
        tv_product_detail_descripcion.setText(mproduct.getDescription());




        /*START INTENT SERVICE*/
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
        /*END INTENT SERVICE*/

        /*START OF DIALOG*/
     //   Button btnProductDetailSize = findViewById(R.id.btn_product_detail_size);
      //  Button btnProductDetailColor = findViewById(R.id.btn_product_detail_color);
       // Button btnProductDetailQuantity = findViewById(R.id.btn_product_detail_quantity);

       // btnProductDetailSize.setOnClickListener(this);
       // btnProductDetailColor.setOnClickListener(this);
       // btnProductDetailQuantity.setOnClickListener(this);
        /*END OF DIALOG*/


        /*START OF ADD TO CART*/
        btnAddCart = findViewById(R.id.btn_product_detail_add_cart);
        btnAddCart.setOnClickListener(this);
        /*END OF ADD TO CART*/


        // START OF PRODUCT DESCRIPTION
      //  btnDescReadMore = findViewById(R.id.btn_p_detail_desc_read_more);
        //btnDescReadMore.setOnClickListener(this);
        pDetailDesc = findViewById(R.id.p_detail_desc);
        //STAR BTN ADDITIONAL
        btnDescAdditional = findViewById(R.id.btn_p_detail_desc_additional);
        btnDescAdditional.setOnClickListener(this);
        pDetailDesAdditional = findViewById(R.id.p_additional_desc);
        //STAR BTN POLITIC
        btnDescPolitic = findViewById(R.id.btn_p_detail_desc_politic);
        btnDescPolitic.setOnClickListener(this);
        pDetailDesPolitic = findViewById(R.id.p_politic_desc);



        //  START OF FIRST VISIBLE ON ACTIVE PRODUCT DESCRIPTION

        final TextView tvpDetailsDesc = findViewById(R.id.tv_p_detail_description);
        ((TextView) tvpDetailsDesc).post(new Runnable() {

            @Override
            public void run() {

                firstDescLineCount = ((TextView) tvpDetailsDesc).getLineCount();
                firstDescLineHeight = ((TextView) tvpDetailsDesc).getLineHeight();

                ViewGroup.LayoutParams params = pDetailDesc.getLayoutParams();
                params.height = firstDescLineHeight * firstDescVisibleLineCount;
                pDetailDesc.setLayoutParams(params);

            }

        });
        //  END OF FIRST VISIBLE ON ACTIVE PRODUCT DESCRIPTION

        // Politica
//        View viewBtnPolitic = findViewById(R.id.btn_p_detail_desc_politic);
//        viewBtnPolitic.performClick();

        //STAR ADDITIONAL
        tvpAdditional = findViewById(R.id.tv_p_additional);

        final String ruta_full = Constants.RECENT_PRODUCT_TALLA_FOTO + "" +mproduct.getFotos_talla();

        Picasso.get().load(Constants.RECENT_PRODUCT_TALLA_FOTO + "" +mproduct.getFotos_talla()).into(imageViewFotoTalla);

        imageViewFotoTalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, FotoFullActivity.class);
                intent.putExtra("ruta_full", ruta_full);
                startActivity(intent);
            }
        });

//+++        ((TextView) tvpAdditional).post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                firstDescLineCount = ((TextView) tvpAdditional).getLineCount();
//                firstDescLineHeight = ((TextView) tvpAdditional).getLineHeight();
//
//                ViewGroup.LayoutParams params = pDetailDesAdditional.getLayoutParams();
//                params.height = 0 ;
//                pDetailDesAdditional.setLayoutParams(params);
//
//            }
//
//        });


        //STAR POLITIC
        tvpPolitic = findViewById(R.id.tv_p_politic);
        politicProd();




//++        ((TextView) tvpPolitic).post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                firstDescLineCount = ((TextView) tvpPolitic).getLineCount();
//                firstDescLineHeight = ((TextView) tvpPolitic).getLineHeight();
//
//                ViewGroup.LayoutParams params = pDetailDesPolitic.getLayoutParams();
//                params.height = 0 ;
//                pDetailDesPolitic.setLayoutParams(params);
//
//            }
//
//        });


        // START OF SIZE/COLOR/QTY ICON RIGHT RESIZING
       // UtilityClass.buttonScaleIconRight(this, btnProductDetailSize, R.drawable.ic_arrow_down_black, .3, 1);
       // UtilityClass.buttonScaleIconRight(this, btnProductDetailColor, R.drawable.ic_arrow_down_black, .3, 1);
       // UtilityClass.buttonScaleIconRight(this, btnProductDetailQuantity, R.drawable.ic_arrow_down_black, .3, 1);
      //  UtilityClass.buttonScaleIconRight(this, btnDescReadMore, R.drawable.ic_arrow_down_primary, 1, 1);
        UtilityClass.buttonScaleIconRight(this, btnDescAdditional, R.drawable.ic_arrow_down_primary, 1, 1);
        UtilityClass.buttonScaleIconRight(this, btnDescPolitic, R.drawable.ic_arrow_down_primary, 1, 1);
        // END OF SIZE/COLOR/QTY ICON RIGHT RESIZING

        // START DOTS INDICATORS
        dotsIndicators = findViewById(R.id.layout_product_Detail_dots);
        // END OF DOTS INDICATORS




        // Descripcion

        //START RECYLERVIEW PRODUCT GRID

        productGridList = new ArrayList<>();

        recyclerViewProduct = findViewById(R.id.rv_p_detail_product_grid);
        ProductRecylerViewAdapter productRecylerViewAdapter = new ProductRecylerViewAdapter(productGridList, this);

        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);

        recyclerViewProduct.setAdapter(productRecylerViewAdapter);
        //END FOR RECYLER PRODUCT VIEW


        /*STAR ADD CART DIRECCTO*/
        /*START OF QUANTITY PLUS/MINUS*/
        btnDialogQtyPlus = findViewById(R.id.btn_dialog_cart_product_plus_conatiner);
        btnDialogQtyMinus = findViewById(R.id.btn_dialog_cart_product_minus_container);
        btnDialogQty = findViewById(R.id.btn_dialog_cart_product_quantity);
        btnDialogQty.setText(String.valueOf(productQty));
        /*START OF PLUS BTN CLICK LISTENER*/
        btnDialogQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productQty++;
                btnDialogQty.setText(String.valueOf(productQty));

            }
        });
        /*END OF PLUS BTN CLICK LISTENER*/
        /*START OF MINUS BTN CLICK LISTENER*/
        btnDialogQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQty > 1) {
                    productQty--;
                    btnDialogQty.setText(String.valueOf(productQty));
                }
            }
        });
        /*END OF QUANTITY PLUS/MINUS*/
        /*END*/
        /*STAR SPINER */
        getInventory();

        // ADD POLIIC Y ADDIT
      //  // ADD POLIIC Y ADDIT

        spinner_talla = findViewById(R.id.sp1_talla);
        //spinner_color = findViewById(R.id.sp2_color);
        spinner_talla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Tallas y Colores")){
                    item_talla = parent.getItemAtPosition(position).toString();
                    if(position == 0){

                    }
                }else {
                    item_talla = parent.getItemAtPosition(position).toString();
                    countTalla = position - 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  spinner_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Colores")){
                    item_color = parent.getItemAtPosition(position).toString();
                }else {
                    item_color = parent.getItemAtPosition(position).toString();
                    countColor = position - 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

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

        ViewPager vpProductDetailsImageSlider = findViewById(R.id.vp_product_detail_image_slider);
        PagerAdapter pagerAdapter = new SliderProductDeatilAdapter(getSupportFragmentManager());
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


    private void dots(int current) {

        dotsIndicators.removeAllViews();
        for (int i = 0; i < productSlideCount; i++) {
            TextView dot = new TextView(this);
            dot.setIncludeFontPadding(false);
            dot.setHeight((int) UtilityClass.convertDpToPixel(10, ProductDetailActivity.this));
            dot.setWidth((int) UtilityClass.convertDpToPixel(10, ProductDetailActivity.this));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginAsDp = (int) UtilityClass.convertDpToPixel(4, ProductDetailActivity.this);
            params.setMargins(marginAsDp, marginAsDp, marginAsDp, marginAsDp);
            dot.setLayoutParams(params);

            dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_border_bg_1));

            if (i == current) {
                dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));
            }

            dotsIndicators.addView(dot);

        }
    }

    public void settingToolBar() {

        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbarLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toolbarLogo.setVisibility(View.VISIBLE);
        toobarTitle.setVisibility(View.GONE);

        //  btnClear = findViewById(R.id.btn_clear);
        etSearch = findViewById(R.id.et_seach_product);
        btnSearchFiltre = findViewById(R.id.btn_filtre);
        btnList1 = findViewById(R.id.btn_filtre_list);
        btnSearchFiltre.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        /*START OF TOOLBAR */

        // toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //comentado
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*END OF TOOLBAR */



    }

    private static boolean arrayShearch(ArrayList<String> listIdTemp, String searchString) {
        return Arrays.asList(listIdTemp)
                .contains(searchString);
    }

    Button btnDescAdditional,btnDescPolitic; //btnDescReadMore
    LinearLayout pDetailDesc,pDetailDesAdditional,pDetailDesPolitic;
    boolean pDetailDescExpanded = false;
    public   Map<String, SelectedProduct > listID = new HashMap<String, SelectedProduct>();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product_detail_add_cart:

                if (item_talla.equals("Tallas y Colores")) {
                    Toast.makeText(getApplicationContext(), "Selecionar Talla y Color", Toast.LENGTH_LONG).show();
                   // getInventory();


                } else {
                    CustomProductInventory tempIventory = inventoryList.get(countTalla);
                    if(productQty <= tempIventory.getAvailable_qty() ){
                      // Toast.makeText(getApplicationContext(), " " +tempIventory.getAvailable_qty(), Toast.LENGTH_LONG).show();
                       selectedProductList = dataSource.getAllCartProducts();
                        listID =  new HashMap<String, SelectedProduct>();
                        for(SelectedProduct proTemp : selectedProductList){
                          //  listIdTemp.add( String.valueOf(proTemp.getInvetory_id()));
                            listID.put(String.valueOf(proTemp.getInvetory_id()),proTemp);
                        }

                        if( listID.containsKey(String.valueOf(tempIventory.getInventory_id()))){

                            SelectedProduct  ee = listID.get(String.valueOf(tempIventory.getInventory_id()));
                            if(productQty +ee.getQunatity() <= tempIventory.getAvailable_qty()){
                                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                                dataSource.updateQuantityCart(ee.getQunatity() + productQty, ee.getCart_id());
                                startActivity(intent);
                            }else { DialogInventory exampleDialog = new DialogInventory();
                                Bundle bund = new Bundle();
                                bund.putInt("filtre",tempIventory.getAvailable_qty());
                                exampleDialog.setArguments(bund);
                                exampleDialog.show(getSupportFragmentManager(), "example dialog");}

                        }else
                        {
                            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                            SelectedProduct selectedProduct = new SelectedProduct();
                            ProductSize selectedSize = productSizes.get(countTalla);
                            ProductColor selectedColor = productColors.get(countTalla);
                            selectedProduct.setInvetory_id(tempIventory.getInventory_id());
                            selectedProduct.setQunatity(productQty);
                            if (selectedSize != null) selectedProduct.setProductSize(selectedSize);
                            if (selectedColor != null) selectedProduct.setProductColor(selectedColor);
                            selectedProduct.setProduct(mproduct);
                            selectedProduct.setId(mproduct.getId());
                            dataSource.addCartProduct(selectedProduct);
                            startActivity(intent);
                        }

                      /*  for(int i = 0; i < listID.size(); ++i)
                        {
                            Toast.makeText(getApplicationContext(),  listID.get(i) +"", Toast.LENGTH_LONG).show();
                        }*/

               /*         if(proTemp.getInvetory_id() !=tempIventory.getInventory_id() ){
                            Toast.makeText(getApplicationContext(), " entra :. "+    proTemp.getCart_id(), Toast.LENGTH_LONG).show();
                            addTemp = true;
                        }else if(proTemp.getInvetory_id() ==tempIventory.getInventory_id()){

                            Toast.makeText(getApplicationContext(), " no entraa", Toast.LENGTH_LONG).show();
                        }*/



                    }else{
                        DialogInventory exampleDialog = new DialogInventory();
                        Bundle bund = new Bundle();
                        bund.putInt("filtre",tempIventory.getAvailable_qty());
                        exampleDialog.setArguments(bund);
                        exampleDialog.show(getSupportFragmentManager(), "example dialog");
                    }

                  //  mSelectedProduct.setProduct(mproduct);
                 //   mSelectedProduct.setId(mproduct.getId());
                 /*   if(mSelectedProduct.getProductColor() == null){
                        ProductColor productColor = new ProductColor();
                        productColor.setColor_id(0);
                        productColor.setColorName(item_color);
                        mSelectedProduct.setProductColor(productColor);
                    }
                    if(mSelectedProduct.getProductSize() == null){
                        ProductSize productSize = new ProductSize();
                        productSize.setSize_id(0);
                        productSize.setSizeName(item_talla);
                        mSelectedProduct.setProductSize(productSize);
                    }
                    Toast.makeText(getApplicationContext(), "talla"+
                            mSelectedProduct.getQunatity() + " , " +
                            mSelectedProduct.getInvetory_id() + ", " + mSelectedProduct.getProductColor().getColorName() +
                            ", " + mSelectedProduct.getProductSize().getSizeName(), Toast.LENGTH_LONG).show();
                    Log.d("mSelectedProduct", mSelectedProduct.getQunatity() + " , " +
                            mSelectedProduct.getInvetory_id() + ", " + mSelectedProduct.getProductColor().getColorName() +
                            ", " + mSelectedProduct.getProductSize().getSizeName()
                    );*/


                }
                break;
           /* case R.id.btn_product_detail_size:

                getInventory();
                break;
            case R.id.btn_product_detail_color:
                getInventory();
                break;*/
           /* case R.id.btn_product_detail_quantity:
                getInventory();
                break;*/

          /*  case R.id.btn_p_detail_desc_read_more:

                if (!pDetailDescExpanded) {
                    pDetailDescExpanded = true;
                    ViewGroup.LayoutParams params = pDetailDesc.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    pDetailDesc.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescReadMore, R.drawable.ic_arrow_up_primary, 1, 1);
                    btnDescReadMore.setText(getResources().getString(R.string.read_less));
                } else {
                    pDetailDescExpanded = false;
                    ViewGroup.LayoutParams params = pDetailDesc.getLayoutParams();
                    params.height = firstDescLineHeight * firstDescVisibleLineCount;
                    pDetailDesc.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescReadMore, R.drawable.ic_arrow_down_primary, 1, 1);
                    btnDescReadMore.setText(getResources().getString(R.string.read_more));
                }
                break;*/

            case R.id.btn_p_detail_desc_additional:

                if (!pDetailDescExpanded) {
                    pDetailDescExpanded = true;
                    ViewGroup.LayoutParams params = pDetailDesAdditional.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    pDetailDesAdditional.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescAdditional, R.drawable.ic_arrow_up_primary, 1, 1);
                    //btnDesc.setText(getResources().getString(R.string.read_less));
                } else {
                    pDetailDescExpanded = false;
                    ViewGroup.LayoutParams params = pDetailDesAdditional.getLayoutParams();
                    params.height = 0;
                    pDetailDesAdditional.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescAdditional, R.drawable.ic_arrow_down_primary, 1, 1);
                    //btnDescReadMore.setText(getResources().getString(R.string.read_more));
                }
                break;
            case R.id.btn_p_detail_desc_politic:

                if (!pDetailDescExpanded) {
                    pDetailDescExpanded = true;
                    ViewGroup.LayoutParams params = pDetailDesPolitic.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    pDetailDesPolitic.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescPolitic, R.drawable.ic_arrow_up_primary, 1, 1);
                    //btnDesc.setText(getResources().getString(R.string.read_less));
                } else {
                    pDetailDescExpanded = false;
                    ViewGroup.LayoutParams params = pDetailDesPolitic.getLayoutParams();
                    params.height = 0;
                    pDetailDesPolitic.setLayoutParams(params);
                    UtilityClass.buttonScaleIconRight(this, btnDescPolitic, R.drawable.ic_arrow_down_primary, 1, 1);
                    //btnDescReadMore.setText(getResources().getString(R.string.read_more));
                }
                break;
        }
    }

    private void getInventories(int inventory_ids){
        String JSON_URL = Constants.INVENTORY_BY_ID + "?inventory_ids=" + inventory_ids;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<CustomProductInventory>>() {}.getType();
                customProductInventoryList = new Gson().fromJson(response, listType);

                for(CustomProductInventory iven : customProductInventoryList){
                    available_qty_response = iven.getAvailable_qty();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.getCache().clear();
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // mSelectedProduct = null;
       // invalidateOptionsMenu();
        settingNavDrawer();
    }

    private void getInventory(){

        //recoger los datos de la webservices

        String JSON_URL = Constants.PRODUCT_INVENTORY + "?product_ids=" + mproduct.getId();

        Log.d("ChangeX", "?product_ids="+ mproduct.getId().toString());

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Type listType = new TypeToken<List<CustomProductInventory>>() {}.getType();
                customProductInventories = new Gson().fromJson(response, listType);
                tallas = new ArrayList<>();
                colores = new ArrayList<>();
                productColors = new ArrayList<>();
                productSizes = new ArrayList<>();
                inventoryList = new ArrayList<>();
                for (CustomProductInventory pImg : customProductInventories) {
                    customProductoItem=pImg;
                    inventoryList.add(pImg);
                    ProductColor productColor = new ProductColor();
                    productColor.setColor_id(pImg.getColor_id());
                    productColor.setColorName(pImg.getColor_name());
                    productColor.setColorCode(pImg.getColor_code());
                    productColors.add(productColor);

                    ProductSize productSize = new ProductSize();
                    productSize.setSize_id(pImg.getSize_id());
                    productSize.setSizeName(pImg.getSize_name());
                    productSizes.add(productSize);
                    colores.add(pImg.getColor_name());
                    tallas.add(pImg.getSize_name() + " - " +pImg.getColor_name());
                    // ====== RN - Change FOto Ftalla
//+++                    tvpAdditional.setText(Html.fromHtml("Peso del producto: "+customProductoItem.getWeight() +"<br>" + "Largo del producto: "+customProductoItem.getLongs()+"<br>" +
//                            "Largo de manga: "+customProductoItem.getLong_sleeve()+"<br>" + "Ancho de espalda: "+customProductoItem.getBack_width()+"<br>" + "Contorno de pecho: "+customProductoItem.getBreast_contour()
//                            +"<br>" + "Cintura: "+customProductoItem.getWaist()  +"<br>" + "Cadera: "+customProductoItem.getHip()
//                    ));

                    //++

                   // tvpPolitic.setText(customProductoItem.getPolitics());
                }


                tallas.add(0,"Tallas y Colores");
                //colores.add(0,"Colores");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(), R.layout.textview_spinner, tallas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_talla.setAdapter(adapter);
               /* ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplication(), R.layout.textview_spinner, colores);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
               // spinner_color.setAdapter(adapter2);

              // AvailableProduct availableProduct = new AvailableProduct();
              // availableProduct.setQuantity(23);

                //abrir modal
              //  ProductDetailDialog productDetailDialog = ProductDetailDialog.newInstance(availableProduct, customProductInventories);
              //  productDetailDialog.show(getFragmentManager(), "PRODUCT_DETAIL_DIALOG");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.getCache().clear();
        queue.add(stringRequest);
    }


    private void politicProd() {
            String JSON_URL = Constants.POLITICS;
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("responseV", response);
                    Type listType = new TypeToken<List<Politics>>() {
                    }.getType();
                    politicsList = new Gson().fromJson(response, listType);

                    for(Politics pro:politicsList) {
                        tvpPolitic.setText(pro.getPoliticas());

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.getCache().clear();
            queue.add(stringRequest);
    }


    public void liked(LikeButton likeButton) {

        if (CustomSharedPrefs.getLoggedInUser(this) != null) {

            dataSource.addFavProduct(mproduct);
            CustomSharedPrefs.setFavProductsInPref(this, dataSource);

        } else {

            Intent intentLogin = new Intent(this, LoginActivity.class);
            this.startActivity(intentLogin);
            likeButton.setLiked(false);
            CustomSharedPrefs.setFavProductsInPref(this, dataSource);

        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        dataSource.removeFavProductById(mproduct.getId());
    }

  /*  @Override
    public void onProductEntryComplete(SelectedProduct selectedProduct) {
        mSelectedProduct = selectedProduct;
    }*/


    /*START OF SLIDER IMAGE VIEWPAGER*/
    public class SliderProductDeatilAdapter extends FragmentStatePagerAdapter {

        public SliderProductDeatilAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SliderProductDetailFullItemFragment.newInstance(productDetailSliderImgNameList.get(position),mproduct);
        }

        @Override
        public int getCount() {
            return productDetailSliderImgList.size();
        }

    }
    /*END OF SLIDER IMAGE VIEWPAGER*/


    private void settingProductGrid() {

            String JSON_URL = Constants.PRODUCT_GRID_URL;
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("responseV", response);
                    Type listType = new TypeToken<List<Product>>() {
                    }.getType();
                    productGridList = new Gson().fromJson(response, listType);
                    searchListArray = new ArrayList<>();
                    for(Product pro:productGridList) {
                        searchListArray.add(pro.getTitle());

                    }

                    final   SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) etSearch.findViewById(R.id.search_src_text);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(),  R.layout.textview_auto_complete, searchListArray);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    searchAutoComplete.setAdapter(dataAdapter);

                    searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                            String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                            searchAutoComplete.setText("");
                            String idEtex1="idEtex";
                            Intent intent = new Intent(ProductDetailActivity.this, SearchActivity.class);
                            intent.putExtra("seacrhedtext",queryString);
                            intent.putExtra("idEtex",idEtex1);

                            startActivity(intent);


                        }
                    });
                    // Below event is triggered when submit search query.
                    etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            searchAutoComplete.setText("");
                            String idEtex1="idEtex";
                            Intent intent = new Intent(ProductDetailActivity.this, SearchActivity.class);
                            intent.putExtra("seacrhedtext",query);
                            intent.putExtra("idEtex",idEtex1);
                            startActivity(intent);

                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            return false;
                        }
                    });




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.getCache().clear();
            queue.add(stringRequest);

    }


    User loggedInUser;

    private void settingNavDrawer() {

        /*START OF NAVIGATION DRAWER */
        drawerLayout = findViewById(R.id.dl_navigation_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        loggedInUser = CustomSharedPrefs.getLoggedInUser(ProductDetailActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        tvName.setText("Iniciar sesión");
        ImageView ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);
        Picasso.get().load(R.drawable.profile_image).into(ivProfileImage);

        if (!CustomSharedPrefs.getProfileUrl(ProductDetailActivity.this).equals("")) {
            Picasso.get().load(CustomSharedPrefs.getProfileUrl(ProductDetailActivity.this)).into(ivProfileImage);
        }

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loggedInUser == null) {
                    Intent intentLogin = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                    startActivity(intentLogin);
                } else {
                    Intent intent = new Intent(ProductDetailActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        if (loggedInUser != null) tvName.setText(loggedInUser.getFirst_name());

        nvMainNav = findViewById(R.id.nv_main_nav);

        MenuItem loggedOut = nvMainNav.getMenu().findItem(R.id.nav_logout);
        if (loggedInUser == null) loggedOut.setTitle(getString(R.string.string_login));
        else loggedOut.setTitle(getString(R.string.string_logout));

        nvMainNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intentMain = new Intent(ProductDetailActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_account:
                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(ProductDetailActivity.this, LoginActivity.class);
                            startActivity(intentLogin);
                        } else {
                            Intent intent = new Intent(ProductDetailActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.nav_sub_hombres:
                        String title_hombre="Hombre";
                        String idHombres="1";
                        Intent subHombres = new Intent(ProductDetailActivity.this, TypeActivity.class);
                        subHombres.putExtra("subTitleHombre",title_hombre);
                        subHombres.putExtra("subIdHombres",idHombres);
                        startActivity(subHombres);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_mujer:
                        String title_mujeres="Mujer";
                        String idMujeres="2";
                        Intent subMujeres = new Intent(ProductDetailActivity.this, TypeActivity.class);
                        subMujeres.putExtra("subTitleMujeres",title_mujeres);
                        subMujeres.putExtra("subIdMujeres",idMujeres);
                        startActivity(subMujeres);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_ninhos:
                        String title_ninhos="Niño";
                        String idNinhos="3";
                        Intent subNinhos = new Intent(ProductDetailActivity.this, TypeActivity.class);
                        subNinhos.putExtra("subTitleNinhos",title_ninhos);
                        subNinhos.putExtra("subIdNinhos",idNinhos);
                        startActivity(subNinhos);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sub_bebes:
                        String title_bebes="Bebe";
                        String idBebes="4";
                        Intent subBebes = new Intent(ProductDetailActivity.this, TypeActivity.class);
                        subBebes.putExtra("subTitleBebes",title_bebes);
                        subBebes.putExtra("subIdBebes",idBebes);
                        startActivity(subBebes);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_orders:

                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(ProductDetailActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        } else {
                            Intent intentOrder = new Intent(ProductDetailActivity.this, UserOrderActivity.class);
                            startActivity(intentOrder);
                        }
                        break;

                    case R.id.nav_cart:
                        Intent intentCart = new Intent(ProductDetailActivity.this, CartActivity.class);
                        startActivity(intentCart);
                        break;

                    case R.id.nav_favourites:
                        if (loggedInUser == null) {
                            Intent intentLogin = new Intent(ProductDetailActivity.this, LoginActivity.class);
                            intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                            startActivity(intentLogin);
                        } else {
                            Intent intentFav = new Intent(ProductDetailActivity.this, UserFavActivity.class);
                            startActivity(intentFav);
                        }
                        break;

                    case R.id.nav_logout:
                        if (loggedInUser != null) {
                            new AlertDialog.Builder(ProductDetailActivity.this)
                                    .setTitle("Cerrar sesión")
                                    .setMessage("¿Quieres cerrar sesión?")
                                    .setIcon(R.drawable.ic_logout)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            CustomSharedPrefs.setProfileUrl(ProductDetailActivity.this, "");
                                            UtilityClass.signOutGoogle(mGoogleApiClient);
                                            UtilityClass.signOutFB();
                                            UtilityClass.signOutEmail(ProductDetailActivity.this);
                                            Intent intentLogin = new Intent(ProductDetailActivity.this, LoginActivity.class);
                                            startActivity(intentLogin);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();

                        } else {
                            item.setTitle(getString(R.string.string_login));
                            Intent intentLogin = new Intent(ProductDetailActivity.this, LoginActivity.class);
                            startActivity(intentLogin);
                        }
                        break;
                }

                return false;
            }
        });
        /*END OF NAVIGATION DRAWER */

    }

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();

        // Change RN - For Default desplegable (Descripcion / Politica)

        // Politica
        View viewBtnDescrip = findViewById(R.id.btn_p_detail_desc_additional);
        viewBtnDescrip.performClick();




    }
}
