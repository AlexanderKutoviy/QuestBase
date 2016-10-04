package com.questbase.app.profile.popups;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.febaisi.CustomTextView;
import com.questbase.app.R;
import com.questbase.app.net.entity.transactions.TransactionDto;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<TransactionDto> atomicTransactions;
    private final String RESPOS = " респо";
    private final String DATE_FORMAT = "dd.MM 'o' HH:mm";

    public class TransactionHolder extends RecyclerView.ViewHolder {
        public CustomTextView transaction;
        public CustomTextView subtransaction;

        public TransactionHolder(View view) {
            super(view);
            transaction = (CustomTextView) view.findViewById(R.id.transaction);
            subtransaction = (CustomTextView) view.findViewById(R.id.subtransaction);
        }
    }

    public TransactionAdapter(List<TransactionDto> atomicTransactions) {
        this.atomicTransactions = atomicTransactions;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_layout, parent, false);

        return new TransactionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TransactionHolder holder, final int position) {
        holder.transaction.setText(formatTransactionText(atomicTransactions.get(position)));
        holder.subtransaction.setText(atomicTransactions.get(position).details);
    }

    @Override
    public int getItemCount() {
        return atomicTransactions.size();
    }

    private String formatTransactionText(TransactionDto transactionDto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (transactionDto.amount > 0) {
            return dateFormat.format(transactionDto.timestamp) + "  +" + transactionDto.amount + RESPOS;
        } else {
            return dateFormat.format(transactionDto.timestamp) + "  " + transactionDto.amount + RESPOS;

        }
    }
}