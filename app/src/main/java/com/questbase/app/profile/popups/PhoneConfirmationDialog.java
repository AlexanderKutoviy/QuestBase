package com.questbase.app.profile.popups;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.questbase.app.R;
import com.questbase.app.profile.PersonalCabPresenter;

public class PhoneConfirmationDialog {
    private final MaterialDialog phoneToOutMoneyDialog;
    private final PersonalCabPresenter personalCabPresenter;

    public PhoneConfirmationDialog(PersonalCabPresenter personalCabPresenter, Context context,
                                   Float money) {
        this.personalCabPresenter = personalCabPresenter;
        phoneToOutMoneyDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.phone_confirmation_popup, true)
                .build();
        phoneToOutMoneyDialog.show();
        ((TextView) phoneToOutMoneyDialog.getCustomView().findViewById(R.id.cell_confirmation_text))
                .setText(String.format(context.getString(R.string.confirmation_phone_text),
                        money));
        phoneToOutMoneyDialog.getCustomView().findViewById(R.id.decline_btn)
                .setOnClickListener(view ->
                        phoneToOutMoneyDialog.dismiss()
                );
        setListenerOnPhoneChange();
        phoneToOutMoneyDialog.getCustomView().findViewById(R.id.confirm_btn).setEnabled(false);
        phoneToOutMoneyDialog.getCustomView().findViewById(R.id.confirm_btn)
                .setOnClickListener(view -> {
                            String phoneToOutput = ((EditText) phoneToOutMoneyDialog
                                    .getCustomView().findViewById(R.id.enter_money))
                                    .getText().toString();
                            phoneToOutMoneyDialog.dismiss();
                            ConfirmationDialog confirmationDialog = new ConfirmationDialog(context);
                            confirmationDialog.setPhoneConfirmationText(phoneToOutput, context);
                        }
                );
        hideKeyBoardOnTOuch(context);
    }

    private void hideKeyBoardOnTOuch(Context context) {
        phoneToOutMoneyDialog.getCustomView().setOnTouchListener((view, motionEvent) -> {
            EditText phone = ((EditText) phoneToOutMoneyDialog.getCustomView()
                    .findViewById(R.id.enter_money));
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && phone != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(phone.getWindowToken(), 0);
            }
            return false;
        });
    }

    private void setListenerOnPhoneChange() {
        MaskedEditText phone = ((MaskedEditText) phoneToOutMoneyDialog.getCustomView().findViewById(R.id.enter_money));
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                personalCabPresenter.onPhoneChange(editable.toString());
            }
        });
    }

    public void canApprovePhone(boolean canApprove) {
        phoneToOutMoneyDialog.getCustomView().findViewById(R.id.confirm_btn).setEnabled(canApprove);
    }
}
