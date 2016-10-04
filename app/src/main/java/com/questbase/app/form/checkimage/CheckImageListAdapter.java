package com.questbase.app.form.checkimage;

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
import com.google.gson.JsonObject;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;
import com.questbase.app.utils.FileUtils;
import com.questbase.app.utils.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CheckImageListAdapter extends RecyclerView.Adapter<CheckImageListAdapter.ViewHolder> {
    private static final String NONE = "none";
    private Map<String, CheckBox> checkedItems = new HashMap<>();
    private final List<CheckImageItem> listItemChecks;
    private List<ResponseListener> responseListeners = new ArrayList<>();
    private JsonElement clickedItem;
    private boolean isItemNotCheck = true;
    private long formId;

    CheckImageListAdapter(List<CheckImageItem> listItemChecks, long formId) {
        this.listItemChecks = listItemChecks;
        this.formId = formId;
    }

    @Override
    public CheckImageListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_check_image_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        CheckImageItem record = listItemChecks.get(index);
        viewHolder.iconQuestion.setImageBitmap(getItemBitmap(viewHolder.checkBox.getContext(), formId, record.getImageName()));
        setUpItemState(viewHolder);
        makeJsonAnswer(viewHolder, record.getId());
    }

    @Override
    public int getItemCount() {
        return listItemChecks.size();
    }

    public boolean isItemCheck() {
        return this.isItemNotCheck;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconQuestion;
        private CheckBox checkBox;
        private CheckBox checkIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_answer);
            iconQuestion = (ImageView) itemView.findViewById(R.id.icon_image);
            checkIcon = (CheckBox) itemView.findViewById(R.id.check_icon);
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
        viewHolder.checkBox.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                viewHolder.checkIcon.setPressed(true);
                viewHolder.checkBox.setPressed(true);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (viewHolder.checkBox.isPressed() && viewHolder.checkBox.isChecked()) {
                    viewHolder.checkIcon.setPressed(false);
                    viewHolder.checkIcon.setChecked(false);
                } else if (viewHolder.checkIcon.isPressed()) {
                    viewHolder.checkIcon.setPressed(false);
                    viewHolder.checkIcon.setChecked(true);
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                viewHolder.checkIcon.setPressed(false);
                viewHolder.checkBox.setPressed(false);
                viewHolder.checkIcon.setChecked(false);
                viewHolder.checkBox.setChecked(false);
            }
            return false;
        });
    }

    private void makeJsonAnswer(ViewHolder viewHolder, String id) {
        viewHolder.checkBox.setOnClickListener(v -> {
            handleNoneAnswer((CheckBox) v, id, viewHolder);
            clickedItem = new JsonObject();
            clickedItem = JsonUtils.toJsonArray(checkedItems.keySet());
            this.isItemNotCheck = checkedItems.size() == 0;
            onResponseClicked(responseListeners, clickedItem);
        });
        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> viewHolder.checkIcon.setChecked(isChecked));
    }

    private void handleNoneAnswer(CheckBox clickedCheckBox, String id, ViewHolder viewHolder) {
        if (clickedCheckBox.isChecked()) {
            checkedItems.put(id, viewHolder.checkBox);
            if (id.equals(NONE)) {
                Stream.of(checkedItems.values()).forEach(checkBox -> {
                    checkBox.setPressed(false);
                    checkBox.setChecked(false);
                });
                checkedItems = new HashMap<>();
                checkedItems.put(id, viewHolder.checkBox);
                checkedItems.get(NONE).setPressed(true);
                checkedItems.get(NONE).setChecked(true);
            } else {
                if (checkedItems.containsKey(NONE)) {
                    checkedItems.get(NONE).setPressed(false);
                    checkedItems.get(NONE).setChecked(false);
                    checkedItems.remove(NONE);
                }
            }
        } else {
            checkedItems.remove(id);
        }
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
