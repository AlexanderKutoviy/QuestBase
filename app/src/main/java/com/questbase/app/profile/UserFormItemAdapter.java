package com.questbase.app.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.questbase.app.R;
import com.questbase.app.domain.Form;

import java.util.List;

public class UserFormItemAdapter extends RecyclerView.Adapter<UserFormItemAdapter.UserFormItemHolder> {

    private final PersonalCabPresenter personalCabPresenter;
    private List<Form> formList;

    public class UserFormItemHolder extends RecyclerView.ViewHolder {
        public TextView formsDescription;
        public ImageView formsAvatar;

        public UserFormItemHolder(View view) {
            super(view);
            formsAvatar = (ImageView) view.findViewById(R.id.form_avatar);
            formsDescription = (TextView) view.findViewById(R.id.form_description);
        }
    }

    public UserFormItemAdapter(List<Form> list, PersonalCabPresenter personalCabPresenter) {
        this.formList = list;
        this.personalCabPresenter = personalCabPresenter;
    }

    @Override
    public UserFormItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_form_item, parent, false);

        return new UserFormItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserFormItemHolder holder, final int position) {
        holder.formsDescription.setText(formList.get(position).title);
        holder.formsAvatar.setImageBitmap(personalCabPresenter.displayFormAvatarBitmap(formList.get(position)));
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }

}