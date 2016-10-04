package com.questbase.app.dao.transaction;

import com.questbase.app.net.entity.transactions.PayoutEventDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;

import java.util.List;

public interface PayoutEventDao {

    void create(PayoutEventDto payoutEventDto, String userId);

    List<PayoutEventDto> read(String userId);

    List<TransactionEventsContainerDto> readWithTransactionsLists(String userId);

    void update(PayoutEventDto payoutEventDto, String userId);

    void delete(PayoutEventDto payoutEventDto, String userId);
}