package com.questbase.app.flowui.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.questbase.app.R;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.screens.RecoveryScreen;
import com.questbase.app.flowui.screens.RegisterByMailScreen;

import flow.Flow;

public class AuthorizationMailView extends RelativeLayout implements RouterOwner {

    EditText email;
    EditText password;
    TextView errorMsg;

    public AuthorizationMailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        email = (EditText) findViewById(R.id.email_input);
        password = (EditText) findViewById(R.id.password);
        errorMsg = (TextView) findViewById(R.id.error_msg);

        findViewById(R.id.recover_ref).setOnClickListener(
                v -> Flow.get(AuthorizationMailView.this).set(new RecoveryScreen()));

        findViewById(R.id.sign_in_btn).setOnClickListener(
                v -> {
                    if (!validateMail() || !validatePassword()) {
                        displayError();
                    }
                });

        findViewById(R.id.sign_up_btn).setOnClickListener(
                v -> Flow.get(AuthorizationMailView.this).set(new RegisterByMailScreen()));
    }

    private void displayError() {
        email.getBackground().setColorFilter(getResources().getColor(R.color.logo_sign_in_btn), PorterDuff.Mode.SRC_ATOP);
        password.getBackground().setColorFilter(getResources().getColor(R.color.logo_sign_in_btn), PorterDuff.Mode.SRC_ATOP);
        errorMsg.setTextColor(getResources().getColor(R.color.logo_sign_in_btn));
        errorMsg.setText(getResources().getString(R.string.error_authorization_msg));
    }

    private boolean validateMail() {
        return !email.getText().toString().isEmpty();
    }

    private boolean validatePassword() {
        return !password.getText().toString().isEmpty();
    }

    @Override
    public void injectRouter(Router router) {
    }
}
