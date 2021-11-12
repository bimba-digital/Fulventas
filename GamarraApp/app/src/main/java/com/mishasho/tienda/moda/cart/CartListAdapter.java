package com.mishasho.tienda.moda.cart;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.database.DataSource;
import com.mishasho.tienda.moda.main.CustomProductInventory;
import com.mishasho.tienda.moda.model.SelectedProduct;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends ArrayAdapter<SelectedProduct> {

    List<SelectedProduct> selectedProductList;
    Context context;
    TextView tvTotalWriting, tvTotalWeight, tvTotalWeightEnvio;
    int cantidadTotal=0;
    DataSource dataSource;
    ArrayList<CustomProductInventory> customProductInventories;
    public Button btnDelete;
    int avilableQty;

    public CartListAdapter(@NonNull Context context, int resource, @NonNull List<SelectedProduct> objects,
                           TextView tvTotalWriting, TextView tvTotalWeight, TextView tvTotalWeightEnvio, int cantidadTotal, ArrayList<CustomProductInventory> customProductInventories) {
        super(context, resource, objects);
        selectedProductList = objects;
        this.context = context;
        this.tvTotalWriting = tvTotalWriting;
        this.tvTotalWeight = tvTotalWeight;
        this.tvTotalWeightEnvio = tvTotalWeightEnvio;
        this.cantidadTotal = cantidadTotal;
        this.customProductInventories = customProductInventories;
        dataSource = new DataSource(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        /*START CART ADAPTER*/
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_list_cart_product, parent, false);
          //  customViewHolder = new CustomViewHolder(convertView);

        }

        final SelectedProduct selectedProduct = selectedProductList.get(position);
        for (CustomProductInventory customProductInventory : customProductInventories) {
            if (customProductInventory.getInventory_id() == selectedProduct.getInvetory_id()) {
                avilableQty = customProductInventory.getAvailable_qty();
            }
        }

        ImageView ivCartImage = convertView.findViewById(R.id.iv_cart_product_image);
        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + selectedProduct.getImage_name();
        Picasso.get().load(imageUrl).into(ivCartImage);

        TextView tvCartProductHeading = convertView.findViewById(R.id.tv_cart_product_heading);
        tvCartProductHeading.setText(selectedProduct.getTitle());

        final TextView tvCartProductPrice = convertView.findViewById(R.id.tv_cart_product_price);

        NumberFormat nf = NumberFormat.getCurrencyInstance();
//        tvCartProductPrice.setText(Html.fromHtml("<b>Precio:</b> "  + String.valueOf(nf.format(selectedProduct.getPrice()))));
        tvCartProductPrice.setText(Html.fromHtml("<b>Precio:</b> S/"  + String.valueOf(selectedProduct.getPrice())));

        final Button btnQuantity = convertView.findViewById(R.id.btn_cart_product_quantity);
        btnQuantity.setText(String.valueOf(selectedProduct.getQunatity()));

        TextView tvCartProductSize = convertView.findViewById(R.id.tv_cart_product_size);
        tvCartProductSize.setText( Html.fromHtml("<b>Talla:</b> "  + selectedProduct.getProductSize().getSizeName()));

        TextView tvCartProductColor = convertView.findViewById(R.id.tv_cart_product_color);
        tvCartProductColor.setText(Html.fromHtml("<b>Color:</b> "   + selectedProduct.getProductColor().getColorName()));

        TextView tvCartProductWeigth = convertView.findViewById(R.id.tv_cart_product_peso);
        tvCartProductWeigth.setText(Html.fromHtml("<b>Peso:</b> "  + selectedProduct.getWeight()));

        TextView tvCartProductStore = convertView.findViewById(R.id.tv_cart_product_store);
        tvCartProductStore.setText(Html.fromHtml("<b>Tienda:</b> "  + selectedProduct.getStore()));

        /*START OF PLUS BTN CLICK LISTENER*/
        LinearLayout btnPlus = convertView.findViewById(R.id.btn_cart_product_plus_container);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addedQuantuity = selectedProduct.getQunatity() + 1;

                dataSource.updateQuantityCart(addedQuantuity, selectedProduct.getCart_id());
                selectedProduct.setQunatity(addedQuantuity);
                btnQuantity.setText(String.valueOf(addedQuantuity));

                CartActivity.totalPrice = CartActivity.totalPrice + selectedProduct.getPrice();
//                tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));

                CartActivity.totalPeso = CartActivity.totalPeso + Integer.parseInt(selectedProduct.getWeight());
                tvTotalWeight.setText(String.valueOf(CartActivity.totalPeso) + " gr");

                // comisioon
                CartActivity.totalPesoEnvio = CartActivity.getAdicionalEnvio(CartActivity.totalPeso);
                if (CartActivity.totalPesoEnvio == 0) {
                    tvTotalWeightEnvio.setText("S/ 15");
                    Toast.makeText(context, "Se excedió el Peso para el Envió para el envio por favor maximo 10 kilos.", Toast.LENGTH_LONG).show();
                    CartActivity.totalPesoEnvio = 15;
                }
                else {
                    tvTotalWeightEnvio.setText("S/ " + String.valueOf(CartActivity.totalPesoEnvio));
                }

                // Muestro el total de envio + comision
                CartActivity.totalPrice = CartActivity.totalPrice + CartActivity.totalPesoEnvio;

                tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));


                CartActivity.cantidadTotal=CartActivity.cantidadTotal + 1;
//++               if (addedQuantuity > avilableQty) {
//                    Toast.makeText(context, "La cantidad excede el inventario. stock " + avilableQty, Toast.LENGTH_SHORT).show();
//                } else {
//
//
//                }


            }
        });
        /*END OF PLUS BTN CLICK LISTENER*/


        /*START OF MINUS BTN CLICK LISTENER*/
        LinearLayout btnMinus = convertView.findViewById(R.id.btn_cart_product_minus_conatiner);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int removedQuantuity = selectedProduct.getQunatity() - 1;

                if (removedQuantuity > 0) {

                    dataSource.updateQuantityCart(removedQuantuity, selectedProduct.getCart_id());
                    selectedProduct.setQunatity(removedQuantuity);
                    btnQuantity.setText(String.valueOf(removedQuantuity));

                    CartActivity.totalPrice = CartActivity.totalPrice - selectedProduct.getPrice();

//                    tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));
                    CartActivity.cantidadTotal=CartActivity.cantidadTotal - 1;

                    // Disminue el peso total
                    CartActivity.totalPeso = CartActivity.totalPeso - Integer.parseInt(selectedProduct.getWeight());
                    tvTotalWeight.setText(String.valueOf(CartActivity.totalPeso) + " gr");

                    // comisioon
                    CartActivity.totalPesoEnvio = CartActivity.getAdicionalEnvio(CartActivity.totalPeso);
                    if (CartActivity.totalPesoEnvio == 0) {
                        tvTotalWeightEnvio.setText("S/ 15");
                        Toast.makeText(context, "Se excedió el Peso para el Envió para el envio por favor maximo 10 kilos.", Toast.LENGTH_LONG).show();
                        CartActivity.totalPesoEnvio = 15;
                    }
                    else {
                        tvTotalWeightEnvio.setText("S/ " + String.valueOf(CartActivity.totalPesoEnvio));
                    }

                    // Muestro el total de envio + comision
                    CartActivity.totalPrice = CartActivity.totalPrice + CartActivity.totalPesoEnvio;

                    tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));


                }
//++               if (removedQuantuity > avilableQty) {
//                    Toast.makeText(context, "La cantidad excede el inventario.", Toast.LENGTH_SHORT).show();
//                } else {
//
//                }

            }
        });
        /*END OF PLUS BTN CLICK LISTENER*/


        /*END OF CART ADAPTER*/


//        customViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
      //  customViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, customViewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));
      /*  customViewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onClose(SwipeLayout layout) {
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
            }
        });*/

        btnDelete = (Button) convertView.findViewById(R.id.btndelete);
        btnDelete.setOnClickListener(onDeleteListener(position));
        return convertView;

    }


    private View.OnClickListener onDeleteListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectedProduct sp = selectedProductList.get(position);

                dataSource.removeCartProductById(sp.getCart_id());

                double currentprice = sp.getQunatity() * sp.getPrice();
                CartActivity.totalPrice = CartActivity.totalPrice - currentprice;
                tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));

                CartActivity.cantidadTotal=CartActivity.cantidadTotal - sp.getQunatity();

                selectedProductList.remove(position);
                notifyDataSetChanged();

            }
        };

    }

   /* public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }*/


}
