package com.fit.app.fitit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Maccas on 18/05/2018.
 */

public class AddFoodDialog extends AppCompatDialogFragment {
    public EditText editName;
    public EditText editCalories;

    public DialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog, null);

        //set title, negative button and positive button name
        builder.setView(view).setTitle("Add Item")
        //set negative button listener
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        //set positive button listener
        .setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String fText1 = editName.getText().toString();
                String fText2 = editCalories.getText().toString();

                listener.applyTexts(fText1, fText2);
            }
        });

        //Assign IDs
        editName = view.findViewById(R.id.editName);
        editCalories = view.findViewById(R.id.editCal);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            //listener = (DialogListener) context; //activity version
            listener = (DialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener{
        void applyTexts(String text1, String text2);
    }
}
