package com.questbase.app.feed.feedcategories;

import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.questbase.app.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryType> listOfCategories;
    private List<OnCategorySelectedListener> listeners = new ArrayList<>();

    public CategoryAdapter(List<CategoryType> listOfCategories) {
        this.listOfCategories = listOfCategories;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(listOfCategories.get(i));
        CategoryType record = listOfCategories.get(i);
        viewHolder.categoryImg.setBackgroundResource(record.getIconId());
        viewHolder.categoryName.setText(record.getName());
        onChangeState(viewHolder.categoryImg, record.getPressedIconId(), record.getIconId());
    }

    @Override
    public int getItemCount() {
        return listOfCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImg;
        private TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.category_text);
            categoryImg = (ImageView) itemView.findViewById(R.id.category_icon);
        }

        public void bind(final CategoryType item) {
            categoryImg.setOnClickListener(v -> notifyCategorySelected(item));
            itemView.setOnClickListener(v -> notifyCategorySelected(item));
        }
    }

    public void addListener(OnCategorySelectedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(OnCategorySelectedListener listener) {
        listeners.remove(listener);
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(CategoryType categoryType);
    }

    private void notifyCategorySelected(CategoryType categoryType) {
        Stream.of(listeners)
                .forEach(listener -> listener.onCategorySelected(categoryType));
    }

    private void onChangeState(View item, int pressed, int enable) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                item.getResources().getDrawable(pressed));
        states.addState(new int[]{},
                item.getResources().getDrawable(enable));
        ((ImageView) item.findViewById(R.id.category_icon)).setImageDrawable(states);
    }
}
