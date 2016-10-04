package com.questbase.app.form.bulkradio;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.JsonArray;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;

import java.util.List;

public class BulkRadioOptionsView extends RelativeLayout {
    private static final String TEXT = "text";
    private static final String LEGEND = "legend";
    private static final int NEXT_LEGEND_ITEM = 3;
    private BulkRadioListAdapter bulkRadioListAdapter;

    public BulkRadioOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BulkRadioOptionsView(Context context, JsonArray answersText, JsonArray legendTitles) {
        super(context);
        RelativeLayout bulkRadioLayout = (RelativeLayout) (LayoutInflater.from(context).inflate(
                R.layout.bulk_radio_container, this, false));
        RecyclerView questionRecycler = (RecyclerView) bulkRadioLayout.findViewById(R.id.bulk_recycler);
        addView(bulkRadioLayout);
        setupLegendParams(legendTitles);
        bulkRadioListAdapter = new BulkRadioListAdapter(initAnswersText(answersText));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        questionRecycler.setAdapter(bulkRadioListAdapter);
        questionRecycler.setLayoutManager(layoutManager);
        questionRecycler.setItemAnimator(itemAnimator);
    }

    public void addResponseListener(ResponseListener responseListener) {
        bulkRadioListAdapter.addResponseListener(responseListener);
    }

    public boolean isItemCheck() {
        return bulkRadioListAdapter.isItemCheck();
    }

    private List<BulkRadioItem> initAnswersText(JsonArray answersArray) {
        return Stream.of(answersArray).map(answer -> new BulkRadioItem(answer.getAsJsonObject()))
                .collect(Collectors.toList());
    }

    private void setupLegendParams(JsonArray legendTitles) {
        final int[] index = {0};
        Stream.of(legendTitles).filter(item -> item.getAsJsonObject().get(LEGEND) != null).forEach(item -> {
                    ((TextView) findViewById(LegendTitles.values()[index[0]].id)).
                            setText((item.getAsJsonObject().get(TEXT)) + " - "
                                    + item.getAsJsonObject().get(LEGEND).getAsString());
                    index[0] = index[0] + NEXT_LEGEND_ITEM;
                }
        );
    }

    enum LegendTitles {
        FIRST(R.id.first_item),
        SECOND(R.id.second_item),
        THIRD(R.id.third_item),
        FOUR(R.id.four_item),
        FIVE(R.id.five_item);

        int id;

        LegendTitles(int id) {
            this.id = id;
        }
    }
}
