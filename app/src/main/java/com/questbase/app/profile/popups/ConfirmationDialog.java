package com.questbase.app.profile.popups;

import android.content.Context;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.questbase.app.R;

public class ConfirmationDialog {
    private MaterialDialog confirmationDialog;

    public ConfirmationDialog(Context context) {
        confirmationDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.confirmation_popup, true)
                .build();
        confirmationDialog.show();
    }

    public void setCharityConfirmationText(String charityTarget, Context context) {
        ((TextView) confirmationDialog.getCustomView().findViewById(R.id.confirmation_text))
                .setText(String.format(context.getString(R.string.confirmation_charity_text),
                        charityTarget));
    }

    public void setPhoneConfirmationText(String phoneToOutput, Context context) {
        ((TextView) confirmationDialog.getCustomView().findViewById(R.id.confirmation_text))
                .setText(String.format(context.getString(R.string.confirmation_money_phone_text),
                        phoneToOutput));
    }
}
