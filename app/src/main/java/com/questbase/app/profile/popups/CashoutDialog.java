package com.questbase.app.profile.popups;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Optional;
import com.questbase.app.R;
import com.questbase.app.profile.PersonalCabPresenter;

public class CashoutDialog {
    private static final int CHARITY_TARGET = 0;
    private static final int PHONE_TARGET = 1;
    public String enteredMoney;
    private Spinner transactionSpinner;
    private final PersonalCabPresenter personalCabPresenter;
    private final MaterialDialog performTransactionDialog;
    private final Resources resources;

    public CashoutDialog(PersonalCabPresenter personalCabPresenter, Context context) {
        this.personalCabPresenter = personalCabPresenter;
        resources = context.getResources();
        performTransactionDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.perform_transaction_layout, true)
                .build();
        performTransactionDialog.show();
        hideKeyBoardOnTouch(context);
        setupAmountOfOutMoneyEditText(context);
        setupSpinner(context);
        setupTransactionButtons();
        onTargetSelect();
    }

    private void hideKeyBoardOnTouch(Context context) {
        performTransactionDialog.getCustomView().setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard(context);
            }
            return false;
        });
    }

    private void setupAmountOfOutMoneyEditText(Context context) {
        EditText amountOfMoneyEditText = ((EditText) performTransactionDialog.getCustomView()
                .findViewById(R.id.enter_money));
        amountOfMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                enteredMoney = editable.toString();
                personalCabPresenter.onEnteredMoneyChanged(enteredMoney, (String) transactionSpinner.getSelectedItem());
            }
        });

        setupSpinner(context);
        performTransactionDialog.getCustomView()
                .findViewById(R.id.perform_transaction_button).setOnClickListener(v -> {
            personalCabPresenter.onApprovePerformTransactionSelected(enteredMoney,
                    transactionSpinner.getSelectedItem());
            performTransactionDialog.dismiss();
        });

        performTransactionDialog.getCustomView().findViewById(R.id.decline_transaction_button)
                .setOnClickListener(v -> performTransactionDialog.dismiss());
        performTransactionDialog.show();
        onTargetSelect();
    }

    private void setupSpinner(Context context) {
        transactionSpinner = (Spinner) performTransactionDialog.getCustomView()
                .findViewById(R.id.transaction_choice);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context,
                R.layout.simple_spinner_item, resources.getStringArray(R.array.spinner_items));
        transactionSpinner.setAdapter(spinnerAdapter);
    }

    private void setupTransactionButtons() {
        performTransactionDialog.getCustomView()
                .findViewById(R.id.perform_transaction_button).setOnClickListener(v -> {
            personalCabPresenter.onApprovePerformTransactionSelected(enteredMoney, transactionSpinner.getSelectedItem());
            performTransactionDialog.dismiss();
        });
        performTransactionDialog.getCustomView().findViewById(R.id.decline_transaction_button)
                .setOnClickListener(v -> performTransactionDialog.dismiss());
    }

    private void onTargetSelect() {
        CheckBox armyCheckBox = (CheckBox) performTransactionDialog.getCustomView()
                .findViewById(R.id.army_check_box);
        CheckBox petsCheckBox = (CheckBox) performTransactionDialog.getCustomView()
                .findViewById(R.id.pets_check_box);

        armyCheckBox.setTag(CharityTarget.ARMY);
        petsCheckBox.setTag(CharityTarget.PETS);

        ((Spinner) performTransactionDialog.getCustomView()
                .findViewById(R.id.transaction_choice)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            final TextView moreInformation = (TextView) performTransactionDialog.getCustomView()
                    .findViewById(R.id.more_info);
            final RelativeLayout charityAvatarFirst = (RelativeLayout) performTransactionDialog
                    .getCustomView().findViewById(R.id.army_avatar);
            final RelativeLayout charityAvatarSecond = (RelativeLayout) performTransactionDialog
                    .getCustomView().findViewById(R.id.pets_avatar);
            final EditText amountOfMoneyEditText = ((EditText) performTransactionDialog.getCustomView()
                    .findViewById(R.id.enter_money));

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard(view.getContext());
                personalCabPresenter.onTargetChange((String) transactionSpinner.getSelectedItem(), amountOfMoneyEditText.getText().toString());
                if (i == CHARITY_TARGET) {
                    onCharitySelected(moreInformation, charityAvatarFirst, charityAvatarSecond,
                            performTransactionDialog);
                } else if (i == PHONE_TARGET) {
                    onPhoneSelected(moreInformation, charityAvatarFirst, charityAvatarSecond,
                            performTransactionDialog);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        makeOneTargetSelect(armyCheckBox, petsCheckBox);
        setupOnCharityCheckedListeners(armyCheckBox, petsCheckBox);
    }

    private void makeOneTargetSelect(CheckBox armyCheckBox, CheckBox petsCheckBox) {
        armyCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                petsCheckBox.setChecked(false);
            }
        });
        petsCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                armyCheckBox.setChecked(false);
            }
        });
    }

    private void setupOnCharityCheckedListeners(CheckBox charityAvatarFirstCheckBox,
                                                CheckBox charityAvatarSecondCheckBox) {
        charityAvatarFirstCheckBox.setOnClickListener(this::onCharityCheckBoxClick);
        charityAvatarSecondCheckBox.setOnClickListener(this::onCharityCheckBoxClick);
    }

    private void onCharityCheckBoxClick(View clickedView) {
        CheckBox checkBox = (CheckBox) clickedView;
        Optional<CharityTarget> charityTarget;
        EditText amountOfMoneyEditText = ((EditText) performTransactionDialog.getCustomView()
                .findViewById(R.id.enter_money));
        hideKeyboard(clickedView.getContext());
        if (checkBox.isChecked()) {
            charityTarget = Optional.of((CharityTarget) checkBox.getTag());
        } else {
            charityTarget = Optional.empty();
        }
        personalCabPresenter.onCharityTargetSelected(charityTarget, amountOfMoneyEditText.getText().toString());
    }

    private RelativeLayout.LayoutParams createParamsForMoreInformation() {
        RelativeLayout.LayoutParams paramsForMoreInformation = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        paramsForMoreInformation.addRule(RelativeLayout.BELOW, R.id.transaction_choice);
        paramsForMoreInformation.topMargin = (int) resources.
                getDimension(R.dimen.transaction_more_info_top);
        return paramsForMoreInformation;
    }

    private RelativeLayout.LayoutParams createParamsForCharityAvatarFirst() {
        RelativeLayout.LayoutParams paramsForCharityAvatarFirst = new RelativeLayout.LayoutParams(
                (int) resources.getDimension(R.dimen.charity_avatars_size),
                (int) resources.getDimension(R.dimen.charity_avatars_size));
        paramsForCharityAvatarFirst.addRule(RelativeLayout.BELOW, R.id.more_info);
        paramsForCharityAvatarFirst.topMargin = (int) resources.
                getDimension(R.dimen.charity_avatars_top_margin);
        paramsForCharityAvatarFirst.leftMargin = (int) resources.
                getDimension(R.dimen.charity_army_left);
        return paramsForCharityAvatarFirst;
    }

    private RelativeLayout.LayoutParams createParamsForCharityAvatarSecond() {
        RelativeLayout.LayoutParams paramsForCharityAvatarSecond = new RelativeLayout.LayoutParams(
                (int) resources.getDimension(R.dimen.charity_avatars_size),
                (int) resources.getDimension(R.dimen.charity_avatars_size));
        paramsForCharityAvatarSecond.addRule(RelativeLayout.BELOW, R.id.more_info);
        paramsForCharityAvatarSecond.addRule(RelativeLayout.ALIGN_RIGHT, R.id.more_info);
        paramsForCharityAvatarSecond.topMargin = (int) resources.
                getDimension(R.dimen.charity_avatars_top_margin);
        paramsForCharityAvatarSecond.rightMargin = (int) resources.
                getDimension(R.dimen.charity_pets_right);
        return paramsForCharityAvatarSecond;
    }

    private void onCharitySelected(TextView moreInformation, RelativeLayout charityAvatarFirst,
                                   RelativeLayout charityAvatarSecond,
                                   MaterialDialog performTransactionDialog) {
        addViewFromParent(moreInformation, performTransactionDialog,
                createParamsForMoreInformation());
        addViewFromParent(charityAvatarFirst, performTransactionDialog,
                createParamsForCharityAvatarFirst());
        addViewFromParent(charityAvatarSecond, performTransactionDialog,
                createParamsForCharityAvatarSecond());

        RelativeLayout.LayoutParams paramsForTransactionBtns = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        paramsForTransactionBtns.addRule(RelativeLayout.BELOW, R.id.army_avatar);
        paramsForTransactionBtns.topMargin = (int) resources.
                getDimension(R.dimen.transaction_btns_top);
        performTransactionDialog.getCustomView().findViewById(R.id.transaction_btns)
                .setLayoutParams(paramsForTransactionBtns);
    }

    private void onPhoneSelected(TextView moreInformation, RelativeLayout charityAvatarFirst,
                                 RelativeLayout charityAvatarSecond,
                                 MaterialDialog performTransactionDialog) {
        removeViewFromParent(moreInformation);
        removeViewFromParent(charityAvatarFirst);
        removeViewFromParent(charityAvatarSecond);

        RelativeLayout.LayoutParams paramsForTransactionBtns = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        paramsForTransactionBtns.addRule(RelativeLayout.BELOW, R.id.transaction_choice);
        paramsForTransactionBtns.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        paramsForTransactionBtns.addRule(LinearLayout.HORIZONTAL);
        paramsForTransactionBtns.topMargin = (int) resources.
                getDimension(R.dimen.transaction_btns_phone_top);
        performTransactionDialog.getCustomView().findViewById(R.id.transaction_btns)
                .setLayoutParams(paramsForTransactionBtns);
    }

    private void addViewFromParent(View addView, MaterialDialog performTransactionDialog,
                                   RelativeLayout.LayoutParams params) {
        removeViewFromParent(addView);
        ((ViewGroup) performTransactionDialog.getCustomView()).addView(addView);
        addView.setLayoutParams(params);
    }

    private void removeViewFromParent(View removeView) {
        if (removeView.getParent() != null) {
            ((ViewGroup) removeView.getParent()).removeView(removeView);
        }
    }

    private void hideKeyboard(Context context) {
        EditText amountOfMoneyEditText = ((EditText) performTransactionDialog.getCustomView()
                .findViewById(R.id.enter_money));
        if (amountOfMoneyEditText != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(amountOfMoneyEditText.getWindowToken(), 0);
        }
    }

    public enum CharityTarget {
        ARMY, PETS
    }

    public void setMessageWithUserBalance(Float balance, Context context) {
        ((TextView) performTransactionDialog.getCustomView()
                .findViewById(R.id.perform_transaction_header))
                .setText(String.format(context.getString(R.string.perform_transaction_header_text),
                        balance));
    }

    public void setCanApprove(boolean canApprove) {
        CheckBox approveButton = (CheckBox) performTransactionDialog.getCustomView()
                .findViewById(R.id.perform_transaction_button);
        approveButton.setEnabled(canApprove);
    }
}
