package com.questbase.app.form.bulkimage;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.JsonArray;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;

import java.util.List;

public class BulkRadioImageOptionsView extends RelativeLayout {
    private static final String IMG = "img";
    private static final int COUNT_ANSWERS_ON_SREEN = 2;
    private BulkRadioImageAdapter bulkRadioImageAdapter;

    public BulkRadioImageOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BulkRadioImageOptionsView(Context context, JsonArray answersText, long formId) {
        super(context);
        LinearLayout bulkImagesLayout = (LinearLayout) (LayoutInflater.from(context).inflate(
                R.layout.question_container, this, false));
        RecyclerView questionRecycler = (RecyclerView) bulkImagesLayout.findViewById(R.id.bulk_image_recycler);
        addView(bulkImagesLayout);
        bulkRadioImageAdapter = new BulkRadioImageAdapter(initAnswers(answersText), formId);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, COUNT_ANSWERS_ON_SREEN);

        questionRecycler.setAdapter(bulkRadioImageAdapter);
        questionRecycler.setLayoutManager(gridLayoutManager);
        questionRecycler.setItemAnimator(itemAnimator);
    }

    public void addResponseListener(ResponseListener responseListener) {
        bulkRadioImageAdapter.addResponseListener(responseListener);
    }

    public boolean isItemCheck() {
        return bulkRadioImageAdapter.isItemCheck();
    }

    private List<BulkRadioImageItem> initAnswers(JsonArray answersArray) {
        return Stream.of(answersArray)
                .map(answer -> new BulkRadioImageItem(answer.getAsJsonObject(), answer.getAsJsonObject().get(IMG).getAsString()))
                .collect(Collectors.toList());
    }
}
