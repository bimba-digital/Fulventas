package com.mishasho.tienda.moda.productDetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class DialogInventory extends AppCompatDialogFragment {

    int position = 0; //default selected position


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(getArguments() != null){
            position= getArguments().getInt("filtre");
        }
        builder.setTitle("Información")
                //.setMessage("Solo hay en Stock " + position)
                .setMessage("NO HAY STOCK")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}