package com.example.hp_pc.weatheraap.Ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.hp_pc.weatheraap.R;

/**
 * Created by hp-pc on 5/24/2018.
 */

public class AndroidDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context=getActivity();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setTitle(R.string.error_titlt).setMessage(R.string.error_msg).setPositiveButton(R.string.error_button,null);
         return builder.create();
    }
}
