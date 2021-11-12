package com.mishasho.tienda.moda.cart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class CartDialog extends AppCompatDialogFragment {




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Información")
                .setMessage("El carrito de compras admite como máximo 12 productos... Gracias")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}