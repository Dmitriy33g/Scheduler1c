package ru.yourport.scheduler1c;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MessageFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private final String LOG_TAG = "myLogs";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Bundle args = this.getArguments();
        int layout = args.getInt("layout", 0);
        String title = args.getString("title");
        String message = args.getString("message");
        String namePositive = args.getString("namePositive");
        String nameNegative = args.getString("nameNegative");
        String nameNeutral = args.getString("nameNeutral");
        Boolean isPositive = namePositive != null;
        Boolean isNegative = nameNegative != null;
        Boolean isNeutral = nameNeutral != null;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);//R.string.dialog_fire_missiles
        if (layout != 0) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(layout, null);
            //view.findViewById(R.id.btnYes).setOnClickListener((View.OnClickListener) getActivity());
            builder.setView(view);
        }

        if (isPositive || !(isNegative && isNeutral)) {
            builder.setPositiveButton(namePositive, (DialogInterface.OnClickListener) getActivity());
        }
        if (isNegative) {
            builder.setNegativeButton(nameNegative, (DialogInterface.OnClickListener) getActivity());
        }
        if (isNeutral) {
            builder.setNeutralButton(nameNeutral, (DialogInterface.OnClickListener) getActivity());
        }
        //if (!(isPositive || isNegative || isNeutral))

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "DialogInterface:onDismiss");
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "DialogInterface:onCancel");
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