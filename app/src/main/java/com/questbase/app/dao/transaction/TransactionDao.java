package com.questbase.app.dao.transaction;

import com.questbase.app.net.entity.transactions.TransactionDto;

import java.util.List;

public interface TransactionDao {

    void create(TransactionDto transactionDto, String userId);

    List<TransactionDto> read(String userId);

    List<TransactionDto> read(String userId, String payoutEventId);

    void update(TransactionDto transactionDto, String userId);

    void delete(TransactionDto transactionDto, String userId);
}