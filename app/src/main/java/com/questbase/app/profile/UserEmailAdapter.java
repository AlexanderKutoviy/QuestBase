package com.questbase.app.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.febaisi.CustomTextView;
import com.questbase.app.R;
import com.questbase.app.domain.UserEmailDto;

import java.util.List;

public class UserEmailAdapter extends RecyclerView.Adapter<UserEmailAdapter.UserEmailHolder> {

    private List<UserEmailDto> emails;

    public class UserEmailHolder extends RecyclerView.ViewHolder {
        public CustomTextView email;

        public UserEmailHolder(View view) {
            super(view);
            email = (CustomTextView) view.findViewById(R.id.email_item);
        }
    }

    public UserEmailAdapter(List<UserEmailDto> list) {
        this.emails = list;
    }

    @Override
    public UserEmailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_item, parent, false);

        return new UserEmailHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserEmailHolder holder, final int position) {
        final int PRIMARY = 1;
        final int NOT_VERIFIED = 0;
        UserEmailDto email = emails.get(position);
        holder.email.setText(email.address);
        if (email.isPrimary == PRIMARY) {
            holder.email.setTextColor(holder.email.getResources().getColor(R.color.dark_green));
        }
        if (email.isVerified == NOT_VERIFIED) {
            holder.email.setTextColor(holder.email.getResources().getColor(R.color.dark_red));
        }
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }
}