package com.questbase.app.feed.feedforms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.questbase.app.R;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.form.FormController;
import com.questbase.app.domain.Form;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FormItemAdapter extends RecyclerView.Adapter<FormItemAdapter.ViewHolder> {
    private List<FormItem> listOfForms;
    private List<OnFormSelectedListener> listeners = new ArrayList<>();
    private final FormController formController;
    private final FilesController filesController;
    public BlockingQueue<View> viewBlockingQueue;

    public FormItemAdapter(List<FormItem> listOfForms, FilesController filesController,
                           FormController formController) {
        this.listOfForms = listOfForms;
        this.filesController = filesController;
        this.formController = formController;
        viewBlockingQueue = new ArrayBlockingQueue<>(3);
        new Thread(() -> {
            try {
                runThread();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public FormItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.form_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(listOfForms.get(i));
        FormItem record = listOfForms.get(i);
        viewHolder.formId = record.getFormId();
        viewHolder.formQuestion.setText(record.getTitle());
        viewHolder.formCategory.setBackgroundResource(record.getCategoryType().getPressedIconId());
    }

    @Override
    public int getItemCount() {
        return listOfForms.size();
    }

    public void updateForms(List<FormItem> list) {
        listOfForms = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView formImg;
        private ImageView formCategory;
        private TextView formQuestion;
        private long formId;

        public ViewHolder(View itemView) {
            super(itemView);
            formImg = (ImageView) itemView.findViewById(R.id.form_image);
            formCategory = (ImageView) itemView.findViewById(R.id.form_category);
            formQuestion = (TextView) itemView.findViewById(R.id.form_body);
        }

        public void bind(final FormItem formItem) {
            itemView.findViewById(R.id.form).setOnClickListener(v -> notifyFormSelected(formItem));
            itemView.setOnClickListener(v -> notifyFormSelected(formItem));
        }
    }

    public void addListener(OnFormSelectedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnFormSelectedListener listener) {
        listeners.remove(listener);
    }

    public interface OnFormSelectedListener {
        void onFormSelected(FormItem form);
    }

    private void notifyFormSelected(FormItem formItem) {
        Stream.of(listeners)
                .forEach(listener -> listener.onFormSelected(formItem));
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void runThread() throws InterruptedException {
        while (true) {
            View nextDisplayedView = viewBlockingQueue.take();
            ViewHolder viewHolder = (ViewHolder) nextDisplayedView.getTag();
            long initialFormId = viewHolder.formId;
            formController.getFormById(viewHolder.formId)
                    .ifPresent(form -> {
                        Bitmap bitmap = getFormIcon(form);
                        nextDisplayedView.post(() -> {
                            long currentFormId = viewHolder.formId;
                            if (currentFormId == initialFormId) {
                                viewHolder.formImg.setImageBitmap(bitmap);
                            }
                        });
                    });
        }
    }

    private Bitmap getFormIcon(Form form) {
        File formResourcesPath = new File(filesController.getBasePath(), String.valueOf(form.formId));
        File avatarPath = new File(formResourcesPath, Stream.of(form.resources)
                .filter(resource -> resource.resId.contains("avatar"))
                .findFirst().orElseThrow(() -> new NullPointerException("There is no avatar for form " + form.formId)).resId);
        return BitmapFactory.decodeFile(avatarPath.getAbsolutePath());
    }
}