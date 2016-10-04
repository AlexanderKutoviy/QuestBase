package com.questbase.app.dao.transaction;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.net.entity.transactions.TransactionDto;

@Table(database = RespoDatabase.class)
public class TransactionModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public long id;

    @Column
    public float amount;

    @Column
    public long timestamp;

    @Column
    public String details;

    @Column
    public String payoutEventId;

    @Column
    String userId;

    public TransactionModel(TransactionDto transactionDto, String userId) {
        this.id = transactionDto.id;
        this.amount = transactionDto.amount;
        this.timestamp = transactionDto.timestamp.getTime();
        this.details = transactionDto.details;
        this.payoutEventId = transactionDto.payoutEventId;
        this.userId = userId;
    }

    public TransactionModel() {
    }

    public TransactionDto toTransaction() {
        return new TransactionDto(this);
    }
}
