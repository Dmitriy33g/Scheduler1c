package ru.yourport.scheduler1c;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.DialogFragment;

public class MessageFragment extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Bundle args = this.getArguments();
        String message = args.getString("message");
        Boolean isPositive = args.getBoolean("isPositive");
        Boolean isNegative = args.getBoolean("isNegative");
        Boolean isNeutral = args.getBoolean("isNeutral");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);//R.string.dialog_fire_missiles
        if (isPositive) {
            String namePositive = args.getString("namePositive");
            namePositive = namePositive == null ? "ОК": namePositive;
            builder.setPositiveButton(namePositive, (DialogInterface.OnClickListener) getActivity());
        }
        if (isNegative) {
            String nameNegative = args.getString("nameNegative");
            nameNegative = nameNegative == null ? "Отмена": nameNegative;
            builder.setNegativeButton(nameNegative, (DialogInterface.OnClickListener) getActivity());
        }
        if (isNeutral) {
            String nameNeutral = args.getString("nameNeutral");
            nameNeutral = nameNeutral == null ? "Продолжить": nameNeutral;
            builder.setNeutralButton(nameNeutral, (DialogInterface.OnClickListener) getActivity());
        }
        //if (!(isPositive || isNegative || isNeutral))

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
            case Dialog.BUTTON_NEUTRAL:
                break;
        }
    }

    public void dismissDialog(final MessageFragment dialog, int daleyTime) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    dialog.dismiss();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }, daleyTime);
    }
}