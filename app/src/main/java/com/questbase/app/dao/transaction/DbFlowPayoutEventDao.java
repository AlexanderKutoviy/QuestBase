package com.questbase.app.dao.transaction;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.net.entity.transactions.PayoutEventDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;

import java.util.List;

public class DbFlowPayoutEventDao implements PayoutEventDao {
    @Override
    public void create(PayoutEventDto payoutEventDto, String userId) {
        new PayoutEventModel(payoutEventDto, userId).saveOnDuplicateUpdate();
    }

    @Override
    public List<PayoutEventDto> read(String userId) {
        return Stream.of(SQLite.select().from(PayoutEventModel.class)
                .where(PayoutEventModel_Table.userId.eq(userId)).queryList())
                .map(PayoutEventModel::toPayoutEventDto).collect(Collectors.toList());
    }

    @Override
    public List<TransactionEventsContainerDto> readWithTransactionsLists(String userId) {
        return Stream.of(SQLite.select().from(PayoutEventModel.class)
                .where(PayoutEventModel_Table.userId.eq(userId)).queryList())
                .map(PayoutEventModel::toTransactionEventContainer).collect(Collectors.toList());
    }

    @Override
    public void update(PayoutEventDto payoutEventDto, String userId) {
        new PayoutEventModel(payoutEventDto, userId).delete();
    }

    @Override
    public void delete(PayoutEventDto payoutEventDto, String userId) {
        create(payoutEventDto, userId);
    }
}