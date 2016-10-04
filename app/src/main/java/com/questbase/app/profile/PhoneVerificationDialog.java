package com.questbase.app.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.questbase.app.R;

public class PhoneVerificationDialog extends Activity implements View.OnClickListener {

    private EditText etNum;
    public static final String DATA = "phoneNum";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification_dialog);
        etNum = (EditText) findViewById(R.id.edit_number);
        Button btnOk = (Button) findViewById(R.id.verificate_button);
        btnOk.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(DATA, etNum.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
