package com.questbase.app.form.radioimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.google.gson.JsonElement;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;
import com.questbase.app.utils.FileUtils;
import com.questbase.app.utils.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class RadioImageListAdapter extends RecyclerView.Adapter<RadioImageListAdapter.ViewHolder> {
    private final List<RadioImageItem> listItemRadios;
    private List<ResponseListener> responseListeners = new ArrayList<>();
    private CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;
    private boolean isItemCheck = true;
    private long formId;

    RadioImageListAdapter(List<RadioImageItem> listItemChecks, long formId) {
        this.listItemRadios = listItemChecks;
        this.formId = formId;
    }

    @Override
    public RadioImageListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.radio_image_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        RadioImageItem record = listItemRadios.get(index);
        viewHolder.iconQuestion.setImageBitmap(getItemBitmap(viewHolder.radioButton.getContext(), formId, record.getImageName()));
        setUpItemState(viewHolder);
        makeJsonAnswer(viewHolder, index, record.getImageName());
    }

    @Override
    public int getItemCount() {
        return listItemRadios.size();
    }

    public boolean isItemCheck() {
        return this.isItemCheck;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconQuestion;
        private CheckBox radioButton;
        private CheckBox radioIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton = (CheckBox) itemView.findViewById(R.id.radio_answer);
            iconQuestion = (ImageView) itemView.findViewById(R.id.radio_image);
            radioIcon = (CheckBox) itemView.findViewById(R.id.radio_icon);
        }
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

    private void setUpItemState(ViewHolder viewHolder) {
        viewHolder.radioButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                viewHolder.radioIcon.setPressed(true);
                viewHolder.radioButton.setPressed(true);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (viewHolder.radioIcon.isPressed()) {
                    viewHolder.radioIcon.setPressed(false);
                    viewHolder.radioIcon.setChecked(true);
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                viewHolder.radioIcon.setPressed(false);
                viewHolder.radioIcon.setChecked(false);
            }

            return false;
        });
        viewHolder.radioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            viewHolder.radioIcon.setPressed(b);
            viewHolder.radioIcon.setChecked(b);
        });
    }

    private void makeJsonAnswer(ViewHolder viewHolder, int index, String id) {
        viewHolder.radioButton.setChecked(listItemRadios.get(index).isCheck());
        viewHolder.radioButton.setTag(index);
        if (index == 0 && listItemRadios.get(0).isCheck() && viewHolder.radioButton.isChecked()) {
            lastChecked = viewHolder.radioButton;
            lastCheckedPos = 0;
        }

        viewHolder.radioButton.setOnClickListener(v -> {
            CheckBox radioBtn = (CheckBox) v;
            int clickedPos = (Integer) radioBtn.getTag();

            if (radioBtn.isChecked()) {
                if (lastChecked != null) {
                    lastChecked.setChecked(false);
                    listItemRadios.get(lastCheckedPos).setCheck(false);
                }

                lastChecked = radioBtn;
                lastCheckedPos = clickedPos;
                this.isItemCheck = radioBtn.isChecked();
            } else {
                lastChecked = null;
                this.isItemCheck = false;
            }
            listItemRadios.get(clickedPos).setCheck(radioBtn.isChecked());
            JsonElement clickedItem = JsonUtils.toJsonElement(id);
            onResponseClicked(responseListeners, clickedItem);
        });
    }

    private Bitmap getItemBitmap(Context context, long formId, String imageName) {
        final String JPG = ".jpg";
        final String PNG = ".png";
        File image = new File(FileUtils.getFormsParentDirectory(context), String.valueOf(formId));
        String end = "";
        if (!(imageName.contains(PNG) || imageName.contains(JPG))) {
            end = JPG;
        }
        File path = new File(image, imageName + end);
        return BitmapFactory.decodeFile(path.getAbsolutePath());
    }
}