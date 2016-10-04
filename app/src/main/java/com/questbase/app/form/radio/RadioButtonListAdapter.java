package com.questbase.app.form.radio;

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
import java.util.List;

public class RadioButtonListAdapter extends RecyclerView.Adapter<RadioButtonListAdapter.ViewHolder> {
    private static final String VALUE = "value";
    private List<ItemRadio> listItemRadios;
    private static ToggleButton lastChecked = null;
    private int lastCheckedPos = 0;
    private List<ResponseListener> responseListeners = new ArrayList<>();
    private boolean isItemCheck = false;


    public RadioButtonListAdapter(String questionId, List<ItemRadio> listItemRadios) {
        this.listItemRadios = listItemRadios;
    }

    @Override
    public RadioButtonListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_radio_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ItemRadio record = listItemRadios.get(i);
        viewHolder.answerText.setText(record.getAnswer().get());
        viewHolder.radioButton.setChecked(listItemRadios.get(i).isCheck());
        viewHolder.radioButton.setTag(i);

        if (i == 0 && listItemRadios.get(0).isCheck() && viewHolder.radioButton.isChecked()) {
            lastChecked = viewHolder.radioButton;
            lastCheckedPos = 0;
        }

        viewHolder.radioButton.setOnClickListener(v -> {
            ToggleButton toggleButton = (ToggleButton) v;
            int clickedPos = (int) toggleButton.getTag();

            if (toggleButton.isChecked()) {
                if (lastChecked != null) {
                    lastChecked.setChecked(false);
                    listItemRadios.get(lastCheckedPos).setCheck(false);
                }

                lastChecked = toggleButton;
                lastCheckedPos = clickedPos;
                this.isItemCheck = toggleButton.isChecked();
            } else {
                lastChecked = null;
                this.isItemCheck = false;
            }
            listItemRadios.get(clickedPos).setCheck(toggleButton.isChecked());
            JsonObject clickedItem = new JsonObject();
            clickedItem.add(VALUE, JsonUtils.toJsonElement(String.valueOf(clickedPos)));
            onResponseClicked(responseListeners, clickedItem);
        });
    }

    @Override
    public int getItemCount() {
        return listItemRadios.size();
    }

    public boolean isItemCheck() {
        return this.isItemCheck;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ToggleButton radioButton;
        private CustomTextView answerText;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton = (ToggleButton) itemView.findViewById(R.id.toggle_radio);
            answerText = (CustomTextView) itemView.findViewById(R.id.answer_text);
        }
    }

    public void onResponseClicked(List<ResponseListener> responseListeners, JsonObject response) {
        Stream.of(responseListeners).forEach(listener -> listener.onResponse(response));
    }

    public void addResponseListener(ResponseListener responseListener) {
        responseListeners.add(responseListener);
    }

    public void removeResponseListener(ResponseListener responseListener) {
        responseListeners.remove(responseListener);
    }
}
