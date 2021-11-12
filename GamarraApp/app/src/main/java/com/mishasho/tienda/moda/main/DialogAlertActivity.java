package com.mishasho.tienda.moda.main;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mishasho.tienda.moda.R;

public class DialogAlertActivity extends DialogFragment {


    // private ProductDialogListener productDialogListener;

    Context context;
    public Button btnDialogOk;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (Context) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_alert, container, false);

        //categoryList = getArguments().getParcelableArrayList(CATEGORIA_ARRAYLIST);
        btnDialogOk = rootView.findViewById(R.id.btn_aceptar_ok);
        btnDialogOk.setOnClickListener(btnFiltreDialogCencelListener);

        return rootView;

    }



    /*START OF DIALOG BUTTON CANCEL LISTENER*/
    private Button.OnClickListener btnFiltreDialogCencelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                dismiss();
        }
    };







}

