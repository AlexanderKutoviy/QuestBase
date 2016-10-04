package com.questbase.app.form.bulkimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.gson.JsonElement;
import com.questbase.app.R;
import com.questbase.app.form.ResponseListener;
import com.questbase.app.utils.FileUtils;
import com.questbase.app.utils.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BulkRadioImageAdapter extends RecyclerView.Adapter<BulkRadioImageAdapter.ViewHolder> {
    private static final String ID = "id";
    private List<BulkRadioImageItem> listItemChecks;
    private List<Response> responses = new ArrayList<>();
    private List<ResponseListener> responseListeners = new ArrayList<>();
    private Map<Integer, Integer> checkedAnswers = new HashMap<>();
    private boolean isItemCheck = false;
    private long formId;

    BulkRadioImageAdapter(List<BulkRadioImageItem> listItemChecks, long formId) {
        this.listItemChecks = listItemChecks;
        this.formId = formId;
    }

    @Override
    public BulkRadioImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bulk_radio_image_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        BulkRadioImageItem record = listItemChecks.get(i);
        setCheckedFalse(viewHolder);
        checkItem(viewHolder, record.getChoice());
        viewHolder.answerText.setText(record.getAnswerText());
        viewHolder.questionImg.setImageBitmap(getItemBitmap(viewHolder.questionImg.getContext(), formId, record.getImageName()));
        viewHolder.greenButton.setOnClickListener(v -> onClickPerform(viewHolder, record, (CheckBox) v));
        viewHolder.yellowButton.setOnClickListener(v -> onClickPerform(viewHolder, record, (CheckBox) v));
        viewHolder.redButton.setOnClickListener(v -> onClickPerform(viewHolder, record, (CheckBox) v));
    }

    public boolean isItemCheck() {
        return this.isItemCheck;
    }

    @Override
    public int getItemCount() {
        return listItemChecks.size();
    }

    public List<Response> getResponses() {
        return Collections.unmodifiableList(responses);
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

    private void checkItem(ViewHolder viewHolder, Response response) {
        if (response == Response.GREEN) {
            viewHolder.greenButton.setChecked(true);
        } else if (response == Response.YELLOW) {
            viewHolder.yellowButton.setChecked(true);
        } else if (response == Response.RED) {
            viewHolder.redButton.setChecked(true);
        }
    }

    private void onClickPerform(ViewHolder viewHolder, BulkRadioImageItem record,
                                CheckBox clickedButton) {
        setCheckedFalse(viewHolder);
        clickedButton.setChecked(true);
        responses.add((Response) clickedButton.getTag());
        record.setChoice((Response) clickedButton.getTag());
        JsonElement clickedItem = generateResponseObject(record, clickedButton);
        onResponseClicked(responseListeners, clickedItem);
    }

    private JsonElement generateResponseObject(BulkRadioImageItem record, CheckBox clickedButton) {
        if (clickedButton.isChecked()) {
            checkedAnswers.put(record.getAnswerJson().get(ID).getAsInt(), record.getChoice().id);
        }
        this.isItemCheck = checkedAnswers.values().size() == listItemChecks.size();
        return JsonUtils.toJsonArray(checkedAnswers.values());
    }

    private void setCheckedFalse(ViewHolder viewHolder) {
        viewHolder.yellowButton.setChecked(false);
        viewHolder.redButton.setChecked(false);
        viewHolder.greenButton.setChecked(false);
    }

    enum Response {
        GREEN(2), YELLOW(1), RED(0);
        int id;

        Response(int id) {
            this.id = id;
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView questionImg;
        private CheckBox redButton;
        private CheckBox yellowButton;
        private CheckBox greenButton;
        private TextView answerText;

        public ViewHolder(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.bulk_image_item);
            linearLayout.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            redButton = (CheckBox) itemView.findViewById(R.id.answer_red);
            redButton.setTag(Response.RED);
            yellowButton = (CheckBox) itemView.findViewById(R.id.answer_orrange);
            yellowButton.setTag(Response.YELLOW);
            greenButton = (CheckBox) itemView.findViewById(R.id.answer_green);
            greenButton.setTag(Response.GREEN);
            questionImg = (ImageView) itemView.findViewById(R.id.question_image);
            answerText = (TextView) itemView.findViewById(R.id.answer_text);
        }
    }
}