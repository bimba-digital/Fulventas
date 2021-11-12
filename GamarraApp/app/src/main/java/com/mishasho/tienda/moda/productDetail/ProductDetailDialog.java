package com.mishasho.tienda.moda.productDetail;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.UtilityClass;
import com.mishasho.tienda.moda.model.AvailableProduct;
import com.mishasho.tienda.moda.main.CustomProductInventory;
import com.mishasho.tienda.moda.model.ProductColor;
import com.mishasho.tienda.moda.model.ProductSize;
import com.mishasho.tienda.moda.model.SelectedProduct;

import java.util.ArrayList;

public class ProductDetailDialog extends DialogFragment {

    /*START OF RADIOGROUP SIZE VARIABLE*/
    private boolean isCheckingSize = true;
    private int radioChekedSizeId = -1;
    /*END OF RADIOGROUP SIZE VARIABLE*/

    /*START OF RADIOGROUP COLOR VARIABLE*/
    private boolean isCheckingColor = true;
    private int radioChekedColorId = -1;

    /*END OF RADIOGROUP COLOR VARIABLE*/

    /*START OF QUANTITY PLUS/MINUS */
    LinearLayout btnDialogQtyPlus;
    LinearLayout btnDialogQtyMinus;
    Button btnDialogQty;

    LinearLayout layoutSize;
    LinearLayout layoutColor;
    /*END OF QUANTITY PLUS/MINUS */

    int productQty = 1;

    private Button btnProductDialogOk;
    private ProductDialogListener productDialogListener;

    Context context;

    boolean hasColor = true;
    boolean hasSize = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (Context) activity;
        productDialogListener = (ProductDialogListener) activity;
    }

    public static final String AVAILABLE_PRODUCTS = "availabeProducts";
    public static final String INVENTORY_ARRAYLIST = "inventoryArrayList";

    public static ProductDetailDialog newInstance(AvailableProduct availableProduct, ArrayList<CustomProductInventory> customProductInventories) {

        Bundle args = new Bundle();
        args.putParcelable(AVAILABLE_PRODUCTS, availableProduct);
        args.putParcelableArrayList(INVENTORY_ARRAYLIST, customProductInventories);
        ProductDetailDialog fragment = new ProductDetailDialog();
        fragment.setArguments(args);
        return fragment;

    }

    RadioGroup sizeRadioGroup1;
    RadioGroup sizeRadioGroup2;
    RadioGroup colorRadioGroup1;
    RadioGroup colorRadioGroup2;

    ArrayList<CustomProductInventory> customProductInventories;
    ArrayList<ProductSize> productSizes;
    ArrayList<ProductColor> productColors;
    LinearLayout layoutSizeWrapper;
    LinearLayout layoutColorWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.product_detail_enty_dialog, container, false);

        customProductInventories = getArguments().getParcelableArrayList(INVENTORY_ARRAYLIST);
        productSizes = new ArrayList<>();
        productColors = new ArrayList<>();

        for (CustomProductInventory customProductInventory : customProductInventories) {

            ProductColor productColor = new ProductColor();
            ProductSize productSize = new ProductSize();

            productColor.setColor_id(customProductInventory.getColor_id());
            productColor.setColorName(customProductInventory.getColor_name());
            productColor.setColorCode(customProductInventory.getColor_code());
            productColors.add(productColor);

            productSize.setSize_id(customProductInventory.getSize_id());
            productSize.setSizeName(customProductInventory.getSize_name());
            productSizes.add(productSize);

        }

        /*LAYOUT COLOR AND SIZE*/
        layoutColor = rootView.findViewById(R.id.layout_color);
        layoutSize = rootView.findViewById(R.id.layout_size);

        for (ProductColor color : productColors) {
            if (color.getColorName().toLowerCase().equals("null")) {
                layoutColor.setVisibility(View.GONE);
                hasColor = false;
                break;
            }
        }
        for (ProductSize size : productSizes) {
            if (size.getSizeName().toLowerCase().equals("null")) {
                layoutSize.setVisibility(View.GONE);
                hasSize = false;
                break;
            }
        }

        layoutSizeWrapper = rootView.findViewById(R.id.layout_size_wrapper);
        layoutColorWrapper = rootView.findViewById(R.id.layout_color_wrapper);

        if (hasColor) settingColorRadio(4);
        if (hasSize) settingSizeRadio(4);

        /*START OF OK BUTTON*/
        btnProductDialogOk = rootView.findViewById(R.id.btn_product_detail_dialog_cancel);
        btnProductDialogOk.setOnClickListener(btnProductDialogCencelListener);
        /*END OF OK BUTTON*/

        /*START OF OK BUTTON*/
        btnProductDialogOk = rootView.findViewById(R.id.btn_product_detail_dialog_ok);
        btnProductDialogOk.setOnClickListener(btnProductDialogOkListener);
        /*END OF OK BUTTON*/

        /*START OF QUANTITY PLUS/MINUS*/
        btnDialogQtyPlus = rootView.findViewById(R.id.btn_dialog_cart_product_plus_conatiner);
        btnDialogQtyMinus = rootView.findViewById(R.id.btn_dialog_cart_product_minus_container);
        btnDialogQty = rootView.findViewById(R.id.btn_dialog_cart_product_quantity);

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

        return rootView;

    }

    private void settingColorRadio(int colorRowCount) {

        colorRadioGroup1 = new RadioGroup(context);
        colorRadioGroup1.setOrientation(LinearLayout.HORIZONTAL);

        colorRadioGroup2 = new RadioGroup(context);
        colorRadioGroup2.setOrientation(LinearLayout.HORIZONTAL);

        colorRadioGroup1.setOnCheckedChangeListener(rGroupCheckedColorListener1);
        colorRadioGroup2.setOnCheckedChangeListener(rGroupCheckedColorListener2);

        if (productColors.size() <= colorRowCount) {

            for (int i = 0; i < productColors.size(); i++) {

                RadioButton rb = settingColorRadioBtn(productColors.get(i), i);
                colorRadioGroup1.addView(rb);

            }

            layoutColorWrapper.addView(colorRadioGroup1);

        } else if (productColors.size() <= (colorRowCount * 2)) {

            for (int i = 0; i < colorRowCount; i++) {

                RadioButton rb = settingColorRadioBtn(productColors.get(i), i);
                colorRadioGroup1.addView(rb);

            }
            layoutColorWrapper.addView(colorRadioGroup1);

            for (int i = colorRowCount; i < productColors.size(); i++) {

                RadioButton rb = settingColorRadioBtn(productColors.get(i), i);
                colorRadioGroup2.addView(rb);

            }
            layoutColorWrapper.addView(colorRadioGroup2);
        }
    }

    public RadioButton settingColorRadioBtn(ProductColor productColor, int id) {
        RadioButton rdbtn = new RadioButton(context);
        rdbtn.setId(id);
        rdbtn.setButtonDrawable(R.drawable.radio_button_color_clicked);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);

        params.setMargins(0, 0, 16, 10);
        rdbtn.setLayoutParams(params);

        int radioSize = UtilityClass.dpToPx(getResources().getDimension(R.dimen.radio_color_height), context);
        rdbtn.setHeight(radioSize);
        rdbtn.setWidth(radioSize);

        rdbtn.setBackgroundResource(R.drawable.radio_color_bg);
        GradientDrawable drawable = (GradientDrawable) rdbtn.getBackground();
        drawable.setColor(Color.parseColor(productColor.getColorCode()));
        return rdbtn;

    }

    private void settingSizeRadio(int sizeRowCount) {

        sizeRadioGroup1 = new RadioGroup(context);
        sizeRadioGroup1.setOrientation(LinearLayout.HORIZONTAL);

        sizeRadioGroup2 = new RadioGroup(context);
        sizeRadioGroup2.setOrientation(LinearLayout.HORIZONTAL);

        sizeRadioGroup1.setOnCheckedChangeListener(rGroupCheckedSizeListener1);
        sizeRadioGroup2.setOnCheckedChangeListener(rGroupCheckedSizeListener2);

        if (productSizes.size() <= sizeRowCount) {

            for (int i = 0; i < productSizes.size(); i++) {

                RadioButton rb = settingSizeRadioBtn(productSizes.get(i), i);
                sizeRadioGroup1.addView(rb);

            }

            layoutSizeWrapper.addView(sizeRadioGroup1);

        } else if (productSizes.size() <= (sizeRowCount * 2)) {

            for (int i = 0; i < sizeRowCount; i++) {

                RadioButton rb = settingSizeRadioBtn(productSizes.get(i), i);
                sizeRadioGroup1.addView(rb);

            }
            layoutSizeWrapper.addView(sizeRadioGroup1);

            for (int i = sizeRowCount; i < productSizes.size(); i++) {

                RadioButton rb = settingSizeRadioBtn(productSizes.get(i), i);
                sizeRadioGroup2.addView(rb);

            }
            layoutSizeWrapper.addView(sizeRadioGroup2);
        }
    }


    public RadioButton settingSizeRadioBtn(ProductSize productSize, int id) {
        RadioButton rdbtn = new RadioButton(context);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
        params.setMargins(0, 0, 20, 10);
        rdbtn.setLayoutParams(params);

        rdbtn.setText(productSize.getSizeName());
        rdbtn.setId(id);

        Drawable img = context.getResources().getDrawable(R.drawable.radio_button_size_clicked);
        img.setBounds(0, 0, 0, 0);
        rdbtn.setCompoundDrawables(img, null, null, null);
        return rdbtn;

    }

    /*START OF DIALOG BUTTON CANCEL LISTENER*/
    private Button.OnClickListener btnProductDialogCencelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            dismiss();

        }
    };
    /*END OF DIALOG BUTTON CANCEL LISTENER*/
    String message = "";
    boolean showToast = false;

    private boolean passingSelectedProduct(CustomProductInventory customProductInventory,
                                           ProductColor selectedColor, ProductSize selectedSize) {

        if (productQty <= customProductInventory.getAvailable_qty()) {

            SelectedProduct selectedProduct = new SelectedProduct();
            selectedProduct.setInvetory_id(customProductInventory.getInventory_id());
            selectedProduct.setQunatity(productQty);
            if (selectedSize != null) selectedProduct.setProductSize(selectedSize);
            if (selectedColor != null) selectedProduct.setProductColor(selectedColor);
            productDialogListener.onProductEntryComplete(selectedProduct);
           // message = "agregado a carrito."+ "talla :"+ selectedProduct.getProductSize().getSizeName()
            dismiss();
            showToast = false;
            return true;

        } else {
            message = "Su cantidad supera el inventario.";
            showToast = true;
        }

        return false;
    }

    /*START OF DIALOG BUTTON OK LISTENER*/
    private Button.OnClickListener btnProductDialogOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (!hasSize && !hasColor) {

                for (CustomProductInventory customProductInventory : customProductInventories) {
                        if (passingSelectedProduct(customProductInventory, null, null)) break;
                }


            } else if (hasSize && !hasColor) {

                if (radioChekedSizeId == -1) {
                    showToast = true;
                    message = "Por favor, seleccionar un tamaño.";
                } else {
                    ProductSize selectedSize = productSizes.get(radioChekedSizeId);

                    for (CustomProductInventory customProductInventory : customProductInventories) {

                        if (customProductInventory.getSize_id() == selectedSize.getSize_id()) {

                            if (passingSelectedProduct(customProductInventory, null, selectedSize))
                                break;

                        }
                    }
                }

            } else if (!hasSize && hasColor) {

                if (radioChekedColorId == -1) {
                    showToast = true;
                    message = "Por favor elija un color.";
                } else {
                    ProductColor selectedColor = productColors.get(radioChekedColorId);
                    for (CustomProductInventory customProductInventory : customProductInventories) {

                        if (customProductInventory.getColor_id() == selectedColor.getColor_id()) {

                            if (passingSelectedProduct(customProductInventory, selectedColor, null))
                                break;
                        }
                    }
                }

            } else if (hasSize && hasColor) {

                if ((radioChekedSizeId > -1) && (radioChekedColorId > -1)) {

                    ProductSize selectedSize = productSizes.get(radioChekedSizeId);
                    ProductColor selectedColor = productColors.get(radioChekedColorId);

                    for (CustomProductInventory customProductInventory : customProductInventories) {

                        if ((customProductInventory.getColor_id() == selectedColor.getColor_id())
                                && (customProductInventory.getSize_id() == selectedSize.getSize_id())) {

                            if (passingSelectedProduct(customProductInventory, selectedColor, selectedSize))
                                break;

                        } else {
                            message = selectedColor.getColorName() + " no está disponible en el tamaño " + selectedSize.getSizeName() + ".";
                            showToast = true;
                        }
                    }

                } else {
                    showToast = true;
                    if (radioChekedColorId == -1) message = "Por favor elija un color.";
                    else if (radioChekedSizeId == -1) message = "Por favor, seleccionar un tamaño.";

                }
            }

            if (showToast) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        }
    };
    /*END OF DIALOG BUTTON OK LISTENER*/


    /*START OF RADIO GROUP SIZE LISTENET*/
    private RadioGroup.OnCheckedChangeListener rGroupCheckedSizeListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingSize) {
                isCheckingSize = false;
                sizeRadioGroup2.clearCheck();
                radioChekedSizeId = checkedId;

                Toast.makeText(context, "id : "+radioChekedSizeId , Toast.LENGTH_LONG).show();
            }
            isCheckingSize = true;
        }
    };
    private RadioGroup.OnCheckedChangeListener rGroupCheckedSizeListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingSize) {
                isCheckingSize = false;
                sizeRadioGroup1.clearCheck();
                radioChekedSizeId = checkedId;
            }
            isCheckingSize = true;
        }
    };

    private RadioGroup.OnCheckedChangeListener rGroupCheckedColorListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingColor) {
                isCheckingColor = false;
                colorRadioGroup2.clearCheck();
                radioChekedColorId = checkedId;
            }
            isCheckingColor = true;
        }
    };
    private RadioGroup.OnCheckedChangeListener rGroupCheckedColorListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingColor) {
                isCheckingColor = false;
                colorRadioGroup1.clearCheck();
                radioChekedColorId = checkedId;
            }
            isCheckingColor = true;
        }
    };
    /*END OF RADIO GROUP SIZE LISTENET*/


    public interface ProductDialogListener {
        void onProductEntryComplete(SelectedProduct selectedProduct);
    }

}
