package com.questbase.app.dao.transaction;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.net.entity.transactions.TransactionDto;

import java.util.List;

public class DbFlowTransactionDao implements TransactionDao {

    @Override
    public void create(TransactionDto transactionDto, String userId) {
        new TransactionModel(transactionDto, userId).saveOnDuplicateUpdate();
    }

    @Override
    public List<TransactionDto> read(String userId) {
        return Stream.of(SQLite.select().from(TransactionModel.class)
                .where(TransactionModel_Table.userId.eq(userId)).queryList())
                .map(TransactionModel::toTransaction).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> read(String userId, String payoutEventId) {
        return Stream.of(SQLite.select().from(TransactionModel.class)
                .where(TransactionModel_Table.userId.eq(userId))
                .and(TransactionModel_Table.payoutEventId.eq(payoutEventId)).queryList())
                .map(TransactionModel::toTransaction).collect(Collectors.toList());
    }

    @Override
    public void delete(TransactionDto transactionDto, String userId) {
        new TransactionModel(transactionDto, userId).delete();
    }

    @Override
    public void update(TransactionDto transactionDto, String userId) {
        create(transactionDto, userId);
    }
}