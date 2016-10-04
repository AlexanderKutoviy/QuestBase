package com.questbase.app.controller.transaction;

import com.questbase.app.controller.Event;
import com.questbase.app.net.entity.transactions.TransactionDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;

import java.util.List;

import rx.Observable;

public interface TransactionController {

    void sync();

    Observable<Event<TransactionEventsContainerDto>> observe();

    void addTransaction(TransactionEventsContainerDto payoutTransaction, String userId);

    List<TransactionEventsContainerDto> getPayoutEventsWithTransactions();

    List<TransactionEventsContainerDto> getTransactions();

    List<TransactionDto> getStandaloneTransactions();

    void deleteTransaction(TransactionEventsContainerDto payoutTransaction, String userId);

    void updateTransaction(TransactionEventsContainerDto payoutTransaction, String userId);
}