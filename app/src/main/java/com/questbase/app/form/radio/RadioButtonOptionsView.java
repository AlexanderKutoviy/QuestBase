package com.questbase.app.form.radio;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;

import java.util.List;

public class RadioButtonOptionsView extends RelativeLayout {
    private final static String TEXT = "text";
    private RadioButtonListAdapter radioAdapter;

    public RadioButtonOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioButtonOptionsView(Context context, JsonArray answersText, String questionId) {
        super(context);
        RecyclerView questionRecycler = (RecyclerView) (LayoutInflater.from(context).inflate(
                R.layout.question_recycler, this, false));
        addView(questionRecycler);
        radioAdapter = new RadioButtonListAdapter(questionId, initAnswersText(answersText, questionId));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        questionRecycler.setAdapter(radioAdapter);
        questionRecycler.setLayoutManager(layoutManager);
        questionRecycler.setItemAnimator(itemAnimator);
    }

    public boolean isItemCheck() {
        return radioAdapter.isItemCheck();
    }

    public void addResponseListener(ResponseListener responseListener) {
        radioAdapter.addResponseListener(responseListener);
    }

    private List<ItemRadio> initAnswersText(JsonArray answersArray, String questionId) {
        return Stream.of(answersArray).map(answer -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(questionId, ((JsonObject) answer).get(TEXT));
            return new ItemRadio(jsonObject, ((JsonObject) answer).get(TEXT).getAsString());
        }).collect(Collectors.toList());
    }
}
