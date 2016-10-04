package com.questbase.app.controller.transaction;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.controller.Event;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.transaction.PayoutEventDao;
import com.questbase.app.dao.transaction.TransactionDao;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.transactions.PayoutEventDto;
import com.questbase.app.net.entity.transactions.TransactionDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;
import com.questbase.app.utils.RespoSchedulers;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class DefaultTransactionController implements TransactionController {

    private TransactionDao transactionDao;
    private PayoutEventDao payoutEventDao;
    private RestApi restApi;
    private AuthDao authDao;
    private ReplaySubject<Event<TransactionEventsContainerDto>> replaySubject;

    public DefaultTransactionController(TransactionDao transactionDao,
                                        PayoutEventDao payoutEventDao,
                                        RestApi restApi,
                                        AuthDao authDao) {
        this.replaySubject = ReplaySubject.create(1);
        this.transactionDao = transactionDao;
        this.payoutEventDao = payoutEventDao;
        this.authDao = authDao;
        this.restApi = restApi;
    }

    @Override
    public void sync() {
        restApi.getTransactions().subscribeOn(RespoSchedulers.io())
                .subscribe(response ->
                        Stream.of(response.events)
                                .forEach(transaction ->
                                        addTransaction(transaction, authDao.getAuth().get().userId))
                );
    }

    @Override
    public Observable<Event<TransactionEventsContainerDto>> observe() {
        return replaySubject;
    }

    @Override
    public void addTransaction(TransactionEventsContainerDto payoutTransaction, String userId) {
        if (payoutTransaction.event != null) {
            payoutEventDao.create(payoutTransaction.event, userId);
            if (payoutTransaction.transactions != null) {
                Stream.of(payoutTransaction.transactions).forEach(transaction -> {
                    transaction.payoutEventId = String.valueOf(payoutTransaction.event.id);
                    transactionDao.create(transaction, userId);
                });
            }
        } else {
            Stream.of(payoutTransaction.transactions).forEach(transaction -> {
                transactionDao.create(transaction, userId);
            });
        }
        replaySubject.onNext(new Event<>(payoutTransaction, Event.Type.WRITE));
    }

    @Override
    public List<TransactionEventsContainerDto> getPayoutEventsWithTransactions() {
        return payoutEventDao.readWithTransactionsLists(authDao.getAuth().get().userId);
    }

    @Override
    public List<TransactionEventsContainerDto> getTransactions() {
        List<TransactionEventsContainerDto> eventsWithTransactions = payoutEventDao.readWithTransactionsLists(authDao.getAuth().get().userId);
        List<TransactionDto> standaloneTransactions = Stream.of(transactionDao.read(authDao.getAuth().get().userId))
                .filter(transaction -> transaction.payoutEventId == null)
                .collect(Collectors.toList());

        Stream.of(standaloneTransactions).map(transaction -> new TransactionEventsContainerDto(new PayoutEventDto(),
                0, Collections.singletonList(transaction)))
                .forEach(eventsWithTransactions::add);
        return eventsWithTransactions;
    }

    @Override
    public List<TransactionDto> getStandaloneTransactions() {
        return Stream.of(transactionDao.read(authDao.getAuth().get().userId))
                .filter(transaction -> transaction.payoutEventId == null)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTransaction(TransactionEventsContainerDto payoutTransaction, String userId) {
        payoutEventDao.update(payoutTransaction.event, userId);
        Stream.of(payoutTransaction.transactions).forEach(transaction -> {
            transaction.payoutEventId = String.valueOf(payoutTransaction.event.id);
            transactionDao.update(transaction, userId);
        });
        replaySubject.onNext(new Event<>(payoutTransaction, Event.Type.UPDATE));
    }

    @Override
    public void deleteTransaction(TransactionEventsContainerDto payoutTransaction, String userId) {
        payoutEventDao.delete(payoutTransaction.event, userId);
        Stream.of(payoutTransaction.transactions).forEach(transaction -> {
            transaction.payoutEventId = String.valueOf(payoutTransaction.event.id);
            transactionDao.delete(transaction, userId);
        });
        replaySubject.onNext(new Event<>(payoutTransaction, Event.Type.DELETE));
    }
}