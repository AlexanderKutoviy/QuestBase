package com.questbase.app.personalresult;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.questbase.app.R;
import com.questbase.app.net.entity.PortfolioDto;

import java.util.List;

public class PortfolioRecyclerAdapter extends RecyclerView.Adapter<PortfolioRecyclerAdapter.PortfolioItemsHolder> {

    private final PersonalResultPresenter personalResultPresenter;
    private List<PortfolioDto> portfolioDtos;

    public class PortfolioItemsHolder extends RecyclerView.ViewHolder {
        public ImageView projectAvatar;

        public PortfolioItemsHolder(View view) {
            super(view);
            projectAvatar = (ImageView) view.findViewById(R.id.project_avatar);
        }
    }

    public PortfolioRecyclerAdapter(List<PortfolioDto> list, PersonalResultPresenter personalResultPresenter) {
        this.portfolioDtos = list;
        this.personalResultPresenter = personalResultPresenter;
    }

    @Override
    public PortfolioItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_example_item, parent, false);

        return new PortfolioItemsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PortfolioItemsHolder holder, final int position) {
        holder.projectAvatar.setImageBitmap(personalResultPresenter.setProjectAvatarBitmap(portfolioDtos.get(position)));
    }

    @Override
    public int getItemCount() {
        return portfolioDtos.size();
    }
}