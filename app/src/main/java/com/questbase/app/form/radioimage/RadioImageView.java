package com.questbase.app.form.radioimage;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.JsonArray;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;

import java.util.List;

public class RadioImageView extends RelativeLayout {
    private static final String IMG = "img";
    private RadioImageListAdapter radioAdapter;

    public boolean isItemCheck() {
        return radioAdapter.isItemCheck();
    }

    public void addResponseListener(ResponseListener responseListener) {
        radioAdapter.addResponseListener(responseListener);
    }

    public RadioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioImageView(Context context, JsonArray answersText, long formId) {
        super(context);
        RecyclerView questionRecycler = (RecyclerView) (LayoutInflater.from(context).inflate(
                R.layout.question_recycler, this, false));
        addView(questionRecycler);
        radioAdapter = new RadioImageListAdapter(initAnswers(answersText), formId);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        questionRecycler.setAdapter(radioAdapter);
        questionRecycler.setLayoutManager(gridLayoutManager);
        questionRecycler.setItemAnimator(itemAnimator);
    }

    private List<RadioImageItem> initAnswers(JsonArray answersArray) {
        return Stream.of(answersArray)
                .map(answer -> new RadioImageItem(answer.getAsJsonObject().get(IMG).getAsString()))
                .collect(Collectors.toList());
    }
}
