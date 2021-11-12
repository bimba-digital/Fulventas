package com.mishasho.tienda.moda.main;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.app.Dialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.mishasho.tienda.moda.R;

public class DialogAlertActivityFiltre extends DialogFragment {

    int position = 0; //default selected position

    public interface SingleChoiceListener {
        void onPositiveButtonClicked(String[] list, int position);
    }

    SingleChoiceListener mListener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SingleChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + " SingleChoiceListener must implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] list = getActivity().getResources().getStringArray(R.array.choice_items);
        if(getArguments() != null){
            position= getArguments().getInt("filtre");
        }
        builder.setTitle(getString(R.string.search_config_full))
                .setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position = i;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onPositiveButtonClicked(list, position);
                    }
                });
             /*   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onNegativeButtonClicked();
                    }
                });*/

        return builder.create();
    }









}

