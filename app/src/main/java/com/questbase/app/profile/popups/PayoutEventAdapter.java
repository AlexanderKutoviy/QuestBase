package com.questbase.app.profile.popups;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.annimon.stream.Stream;
import com.febaisi.CustomTextView;
import com.questbase.app.R;
import com.questbase.app.net.entity.transactions.PayoutEventDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;

import java.util.List;

public class PayoutEventAdapter extends RecyclerView.Adapter<PayoutEventAdapter.PayoutEventHolder> {

    private final Resources resources;
    private List<TransactionEventsContainerDto> eventsWithTransactions;

    private final String MTS = "500";
    private final String KYIVSTAR = "670";
    private final String LIFE = "630";
    private final String VODAFONE = "910";
    private final String CONFIRMED = "Confirmed";
    private final String PAY_ERROR = "PayError";
    private final String IN_PROGRESS = "InProgress";
    private final String FOR_ARMY = "4army";
    private final String HAPPY_PAW = "happypaw";
    private final String ZERO = "0";

    public class PayoutEventHolder extends RecyclerView.ViewHolder {
        public CustomTextView transactionId;
        public CustomTextView details;
        public CustomTextView target;
        public ImageView logo;
        public ImageView statusPic;
        public RecyclerView transactionsRecycler;

        public PayoutEventHolder(View view) {
            super(view);
            transactionId = (CustomTextView) view.findViewById(R.id.transactionId);
            details = (CustomTextView) view.findViewById(R.id.details);
            target = (CustomTextView) view.findViewById(R.id.target);
            logo = (ImageView) view.findViewById(R.id.phone_provider_logo);
            statusPic = (ImageView) view.findViewById(R.id.statusPic);
            transactionsRecycler = (RecyclerView) view.findViewById(R.id.transactions_recycler);
        }
    }

    public PayoutEventAdapter(List<TransactionEventsContainerDto> eventsWithTransactions,
                              Resources resources) {
        this.eventsWithTransactions = eventsWithTransactions;
        this.resources = resources;
    }

    @Override
    public PayoutEventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payout_event_layout, parent, false);

        return new PayoutEventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PayoutEventHolder holder, final int position) {
        holder.transactionId.setText(String.valueOf(position));
        holder.statusPic.setImageBitmap(getDefaultStatusImg());
        if (!eventsWithTransactions.get(position).event.isNull()) {
            String[] detailsParts = formatPayoutEventDescription(eventsWithTransactions.get(position).event.details);
            holder.details.setText(detailsParts[0]);
            holder.target.setText(detailsParts[1]);
            holder.logo.setImageBitmap(getCorrectLogo(eventsWithTransactions.get(position).event));
            holder.statusPic.setImageBitmap(getCorrectStatus(eventsWithTransactions.get(position).event));
        } else {
            ((ViewGroup) (holder.details.getParent()).getParent())
                    .removeView((View) holder.details.getParent());
        }
        TransactionAdapter transactionAdapter =
                new TransactionAdapter(eventsWithTransactions.get(position).transactions);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(holder.details.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.transactionsRecycler.setLayoutManager(verticalLayoutManager);
        holder.transactionsRecycler.setAdapter(transactionAdapter);
    }

    @Override
    public int getItemCount() {
        return eventsWithTransactions.size();
    }

    private String[] formatPayoutEventDescription(String details) {
        String[] detailsParts = details.split(" ");
        String target = detailsParts[detailsParts.length - 1];
        StringBuilder strBuilder = new StringBuilder();
        Stream.range(0, detailsParts.length - 2).forEach(i -> {
            detailsParts[i] = detailsParts[i] + " ";
            strBuilder.append(detailsParts[i]);
        });
        String detail = strBuilder.toString();
        return new String[]{detail, target};
    }

    private Bitmap getCorrectLogo(PayoutEventDto event) {
        if (event.service.equals(ZERO)) {
            if (event.account.equals(FOR_ARMY)) {
                return BitmapFactory.decodeResource(resources, R.drawable.transaction_naarodnyityl);
            } else if (event.account.equals(HAPPY_PAW)) {
                return BitmapFactory.decodeResource(resources, R.drawable.transaction_happypaw);
            }
        } else {
            switch (event.service) {
                case MTS:
                    return BitmapFactory.decodeResource(resources, R.drawable.mts);
                case KYIVSTAR:
                    return BitmapFactory.decodeResource(resources, R.drawable.kyivstar);
                case LIFE:
                    return BitmapFactory.decodeResource(resources, R.drawable.lifecell);
                case VODAFONE:
                    return BitmapFactory.decodeResource(resources, R.drawable.vodafone);
            }
        }
        return null;
    }

    private Bitmap getCorrectStatus(PayoutEventDto event) {
        switch (event.state) {
            case CONFIRMED:
                return BitmapFactory.decodeResource(resources, R.drawable.transaction_history_mark_confirmed);
            case PAY_ERROR:
                return BitmapFactory.decodeResource(resources, R.drawable.transaction_history_mark_denied);
            case IN_PROGRESS:
                return BitmapFactory.decodeResource(resources, R.drawable.transaction_history_mark_in_progress);
            default:
                return BitmapFactory.decodeResource(resources, R.drawable.transaction_history_mark_confirmed);
        }
    }

    private Bitmap getDefaultStatusImg() {
        return BitmapFactory.decodeResource(resources, R.drawable.transaction_history_mark_confirmed);
    }
}