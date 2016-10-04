package com.questbase.app.form.checkbox;

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

public class CheckBoxOptionsView extends RelativeLayout {
    private final static String TEXT = "text";
    private CheckBoxListAdapter checkAdapter;

    public boolean isItemCheck() {
        return checkAdapter.isItemCheck();
    }

    public void addResponseListener(ResponseListener responseListener) {
        checkAdapter.addResponseListener(responseListener);
    }

    public CheckBoxOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckBoxOptionsView(Context context, JsonArray answersText, String questionId) {
        super(context);
        RecyclerView questionRecycler = (RecyclerView) (LayoutInflater.from(context).inflate(
                R.layout.question_recycler, this, false));
        addView(questionRecycler);
        checkAdapter = new CheckBoxListAdapter(initAnswersText(answersText, questionId));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        questionRecycler.setAdapter(checkAdapter);
        questionRecycler.setLayoutManager(layoutManager);
        questionRecycler.setItemAnimator(itemAnimator);
    }

    private List<ItemCheck> initAnswersText(JsonArray answersArray, String questionId) {
        return Stream.of(answersArray).map(answer -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(questionId, answer);
            return new ItemCheck(jsonObject, ((JsonObject) answer).get(TEXT).getAsString());
        }).collect(Collectors.toList());
    }
}
