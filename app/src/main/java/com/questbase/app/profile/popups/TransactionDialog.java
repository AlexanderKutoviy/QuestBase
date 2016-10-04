package com.questbase.app.profile.popups;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.questbase.app.R;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;

import java.util.List;

public class TransactionDialog {

    public TransactionDialog(Context context, List<TransactionEventsContainerDto> eventsWithTransactions) {
        Resources resources = context.getResources();
        MaterialDialog transactionHistoryDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.transactions_list_dialog, true)
                .build();
        transactionHistoryDialog.show();

        RecyclerView payoutEvents = (RecyclerView) transactionHistoryDialog.getCustomView().findViewById(R.id.transactions_list);
        PayoutEventAdapter payoutEventAdapter = new PayoutEventAdapter(eventsWithTransactions, resources);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        payoutEvents.setLayoutManager(verticalLayoutManager);
        payoutEvents.setAdapter(payoutEventAdapter);
    }
}