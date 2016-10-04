package com.questbase.app.form.bulkradio;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.JsonElement;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;
import com.questbase.app.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkRadioListAdapter extends RecyclerView.Adapter<BulkRadioListAdapter.ViewHolder> {

    private static final String ID = "id";
    private List<BulkRadioItem> listItemChecks;
    private Map<Integer, Integer> checkedAnswers = new HashMap<>();
    private List<ResponseListener> responseListeners = new ArrayList<>();
    private boolean isItemCheck = false;

    public BulkRadioListAdapter(List<BulkRadioItem> listItemChecks) {
        this.listItemChecks = listItemChecks;
    }

    @Override
    public BulkRadioListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bulk_radio_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BulkRadioItem record = listItemChecks.get(position);
        setCheckedFalse(holder);
        if (record.getChoice() != null) {
            checkItem(holder, record.getChoice());
        }
        holder.answerText.setText(record.getAnswerText());
        Stream.of(holder.responses)
                .forEach(response -> response.getValue()
                        .setOnClickListener(v -> onClickPerform(holder, record, (CheckBox) v)));
    }

    private void checkItem(ViewHolder holder, Response choice) {
        holder.responses.get(choice).setChecked(true);
    }

    private void onClickPerform(ViewHolder viewHolder, BulkRadioItem record,
                                CheckBox clickedButton) {
        setCheckedFalse(viewHolder);
        clickedButton.setChecked(true);
        record.setChoice((Response) clickedButton.getTag());
        JsonElement clickedItem = generateResponseObject(record, clickedButton);
        onResponseClicked(responseListeners, clickedItem);
    }

    private void setCheckedFalse(ViewHolder viewHolder) {
        Stream.of(viewHolder.responses).forEach(response -> response.getValue().setChecked(false));
    }

    private JsonElement generateResponseObject(BulkRadioItem record, CheckBox clickedButton) {
        if (clickedButton.isChecked()) {
            checkedAnswers.put(record.getAnswerJson().get(ID).getAsInt(), record.getChoice().ordinal());
        }
        this.isItemCheck = checkedAnswers.size() == listItemChecks.size();
        return JsonUtils.toJsonArray(checkedAnswers.values());
    }

    @Override
    public int getItemCount() {
        return listItemChecks.size();
    }

    private void onResponseClicked(List<ResponseListener> responseListeners, JsonElement response) {
        Stream.of(responseListeners).forEach(listener -> listener.onResponse(response));
    }

    public void addResponseListener(ResponseListener responseListener) {
        responseListeners.add(responseListener);
    }

    public void removeResponseListener(ResponseListener responseListener) {
        responseListeners.remove(responseListener);
    }

    public boolean isItemCheck() {
        return isItemCheck;
    }

    public enum Response {
        ONE(R.id.one), TWO(R.id.two), THREE(R.id.three), FOUR(R.id.four), FIVE(R.id.five);

        int id;

        Response(int id) {
            this.id = id;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView answerText;
        private Map<Response, CheckBox> responses;

        public ViewHolder(View itemView) {
            super(itemView);
            answerText = (TextView) itemView.findViewById(R.id.bulk_radio_answer);
            responses = Stream.of(Response.values())
                    .collect(Collectors.toMap(response -> response,
                            response -> (CheckBox) itemView.findViewById(response.id)));
            Stream.of(Response.values())
                    .forEach(btn -> responses.get(btn).setTag(btn));
        }
    }
}