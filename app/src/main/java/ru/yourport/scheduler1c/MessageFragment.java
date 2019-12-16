package ru.yourport.scheduler1c;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class MessageFragment extends DialogFragment implements DialogInterface.OnClickListener {
    final String LOG_TAG = "myLogs";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //Log.d("myLogs", "savedInstanceState=" + savedInstanceState);
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
            builder.setCancelable(false);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                //i = R.string.yes;
                break;
            case Dialog.BUTTON_NEGATIVE:
                //i = R.string.no;
                break;
            case Dialog.BUTTON_NEUTRAL:
                //i = R.string.maybe;
                break;
        }
        if (i > 0)
            Log.d(LOG_TAG, "Dialog: " + getResources().getString(i));
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
