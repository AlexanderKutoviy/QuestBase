package com.questbase.app.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.febaisi.CustomTextView;
import com.questbase.app.R;
import com.questbase.app.domain.UserPhoneDto;

import java.util.List;

public class UserPhoneAdapter extends RecyclerView.Adapter<UserPhoneAdapter.UserPhoneHolder> {

    private List<UserPhoneDto> phones;

    public class UserPhoneHolder extends RecyclerView.ViewHolder {
        public CustomTextView phone;

        public UserPhoneHolder(View view) {
            super(view);
            phone = (CustomTextView) view.findViewById(R.id.phone_item);
        }
    }

    public UserPhoneAdapter(List<UserPhoneDto> list) {
        this.phones = list;
    }

    @Override
    public UserPhoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_item, parent, false);

        return new UserPhoneHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserPhoneHolder holder, final int position) {
        final int PRIMARY = 1;
        final int NOT_VERIFIED = 0;
        UserPhoneDto phone = phones.get(position);
        holder.phone.setText(phone.phone);
        if (phone.isPrimary == PRIMARY) {
            holder.phone.setTextColor(holder.phone.getResources().getColor(R.color.dark_green));
        }
        if (phone.isVerified == NOT_VERIFIED) {
            holder.phone.setTextColor(holder.phone.getResources().getColor(R.color.dark_red));
        }
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }
}