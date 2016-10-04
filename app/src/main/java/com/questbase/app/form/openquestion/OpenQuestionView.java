package com.questbase.app.form.openquestion;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.annimon.stream.Stream;
import com.google.gson.JsonObject;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;
import com.questbase.app.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class OpenQuestionView extends RelativeLayout {
    private static final String PUBLISH_RESPONSE = "publish";
    private static final String TEXT_VALUE = "value";
    private List<ResponseListener> responseListeners = new ArrayList<>();
    private JsonObject answer = new JsonObject();
    private EditText answerText;

    public OpenQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OpenQuestionView(Context context) {
        super(context);
        answer.add(TEXT_VALUE, JsonUtils.toJsonElement(""));
        answer.add(PUBLISH_RESPONSE, JsonUtils.toJsonElement(String.valueOf(true)));

        RelativeLayout openQuestionLayout = (RelativeLayout) (LayoutInflater.from(getContext()).inflate(
                R.layout.open_question, this, false));
        answerText = (EditText) openQuestionLayout.findViewById(R.id.open_answer);
        setOnChangeStateCheckBoxListener(openQuestionLayout);
        answerText.setEnabled(true);
        setupOnChangeListener(answerText);
        addView(openQuestionLayout);
    }

    private void setOnChangeStateCheckBoxListener(RelativeLayout openQuestionLayout) {
        CheckBox checkPublish = (CheckBox) openQuestionLayout.findViewById(R.id.check_publicate);
        checkPublish.setOnCheckedChangeListener((compoundButton, buttonState) -> {
            if (!answer.get(TEXT_VALUE).isJsonNull()) {
                onResponseClicked(responseListeners, generateResponseObject(answer.get(TEXT_VALUE).getAsString(),
                        checkPublish.isChecked()));
            }
        });
    }

    private JsonObject generateResponseObject(String contentDescription, Boolean checked) {
        answer.add(TEXT_VALUE, JsonUtils.toJsonElement(contentDescription));
        answer.add(PUBLISH_RESPONSE, JsonUtils.toJsonElement(String.valueOf(checked)));
        return answer;
    }

    public void onResponseClicked(List<ResponseListener> responseListeners, JsonObject jsonAnswer) {
        Stream.of(responseListeners).forEach(listener -> listener.onResponse(jsonAnswer));
    }

    public void addResponseListener(ResponseListener responseListener) {
        responseListeners.add(responseListener);
    }

    public void removeResponseListener(ResponseListener responseListener) {
        responseListeners.remove(responseListener);
    }

    public boolean answered() {
        return !answer.get(TEXT_VALUE).getAsString().isEmpty();
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(answerText.getWindowToken(), 0);
    }

    private void setupOnChangeListener(EditText answerText) {
        answerText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                onResponseClicked(responseListeners, generateResponseObject(editable.toString(),
                        answer.get(PUBLISH_RESPONSE).getAsBoolean()));
            }
        });
    }
}
