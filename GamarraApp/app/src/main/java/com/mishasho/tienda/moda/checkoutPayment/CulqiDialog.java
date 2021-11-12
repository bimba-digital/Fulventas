package com.mishasho.tienda.moda.checkoutPayment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;


public class CulqiDialog extends AppCompatDialogFragment {

    public static final String value = "idValue";
    public  String mensaje;

    public static CulqiDialog newInstancia(String param1) {
        CulqiDialog fragment = new CulqiDialog();
        Bundle args = new Bundle();
        args.putString(value,param1);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mensaje = getArguments().getString(value);
        builder.setTitle("Información")
                .setMessage(mensaje)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });

        return builder.create();
    }
}