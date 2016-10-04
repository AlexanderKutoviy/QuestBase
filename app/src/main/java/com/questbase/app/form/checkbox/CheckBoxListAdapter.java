package com.questbase.app.form.checkbox;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.annimon.stream.Stream;
import com.febaisi.CustomTextView;
import com.google.gson.JsonObject;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;
import com.questbase.app.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CheckBoxListAdapter extends RecyclerView.Adapter<CheckBoxListAdapter.ViewHolder> {
    private static final String VALUE = "value";
    private final Set<Integer> checkedIndices = new HashSet<>();
    private final List<ItemCheck> listItemChecks;
    private List<ResponseListener> responseListeners = new ArrayList<>();
    private JsonObject clickedItem;
    private boolean isItemNotCheck = true;

    CheckBoxListAdapter(List<ItemCheck> listItemChecks) {
        this.listItemChecks = listItemChecks;
    }

    @Override
    public CheckBoxListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_check_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        ItemCheck record = listItemChecks.get(index);
        viewHolder.answerText.setText(record.getAnswer());
        viewHolder.checkBox.setOnClickListener(v -> {
            ToggleButton clickListener = (ToggleButton) v;
            if (clickListener.isChecked()) {
                checkedIndices.add(index);
            } else {
                checkedIndices.remove(index);
            }
            clickedItem = new JsonObject();
            clickedItem.add(VALUE, JsonUtils.toJsonArray(checkedIndices));
            this.isItemNotCheck = checkedIndices.size() == 0;
            onResponseClicked(responseListeners, clickedItem);
        });
    }

    @Override
    public int getItemCount() {
        return listItemChecks.size();
    }

    public boolean isItemCheck() {
        return this.isItemNotCheck;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ToggleButton checkBox;
        private CustomTextView answerText;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (ToggleButton) itemView.findViewById(R.id.custom_check_box);
            answerText = (CustomTextView) itemView.findViewById(R.id.answer_text);
        }
    }

    private void onResponseClicked(List<ResponseListener> responseListeners, JsonObject response) {
        Stream.of(responseListeners).forEach(listener -> listener.onResponse(response));
    }

    public void addResponseListener(ResponseListener responseListener) {
        responseListeners.add(responseListener);
    }

    public void removeResponseListener(ResponseListener responseListener) {
        responseListeners.remove(responseListener);
    }
}
