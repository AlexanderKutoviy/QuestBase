package com.questbase.app.form.checkimage;

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

public class CheckImageView extends RelativeLayout {
    private final String IMG = "img";
    private final String ID = "id";
    private CheckImageListAdapter checkAdapter;

    public boolean isItemCheck() {
        return checkAdapter.isItemCheck();
    }

    public void addResponseListener(ResponseListener responseListener) {
        checkAdapter.addResponseListener(responseListener);
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckImageView(Context context, JsonArray answersText, long formId) {
        super(context);
        RecyclerView questionRecycler = (RecyclerView) (LayoutInflater.from(context).inflate(
                R.layout.question_recycler, this, false));
        addView(questionRecycler);
        checkAdapter = new CheckImageListAdapter(initAnswers(answersText), formId);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);

        questionRecycler.setAdapter(checkAdapter);
        questionRecycler.setLayoutManager(gridLayoutManager);
        questionRecycler.setItemAnimator(itemAnimator);
    }

    private List<CheckImageItem> initAnswers(JsonArray answersArray) {
        return Stream.of(answersArray)
                .map(answer -> new CheckImageItem(answer.getAsJsonObject().get(IMG).getAsString(), answer.getAsJsonObject().get(ID).getAsString()))
                .collect(Collectors.toList());
    }
}
