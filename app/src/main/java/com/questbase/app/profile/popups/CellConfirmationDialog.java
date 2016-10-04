package com.questbase.app.profile.popups;

import android.content.Context;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.questbase.app.R;
import com.questbase.app.profile.PersonalCabPresenter;

public class CellConfirmationDialog {

    private final MaterialDialog cellConfirmationDialog;

    public CellConfirmationDialog(PersonalCabPresenter personalCabPresenter, Context context) {
        cellConfirmationDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.cell_confirmation_popup, true)
                .build();
        cellConfirmationDialog.show();

        cellConfirmationDialog.getCustomView().findViewById(R.id.decline_btn)
                .setOnClickListener(view -> cellConfirmationDialog.dismiss());
        cellConfirmationDialog.getCustomView().findViewById(R.id.confirm_btn)
                .setOnClickListener(view -> {
                            cellConfirmationDialog.dismiss();
                            personalCabPresenter.onApproveCellConfirmationDialog();
                        }
                );
    }

    public void showMessageWithUserPhone(String phone, String enteredMoney, Float moneyUAN, Context context) {
        TextView cellConfirmationText = ((TextView) cellConfirmationDialog.getCustomView()
                .findViewById(R.id.cell_confirmation_text));
        cellConfirmationText.setText(
                String.format(context.getString(R.string.pay_transaction_question),
                        Integer.valueOf(enteredMoney),
                        moneyUAN,
                        phone));
    }
}