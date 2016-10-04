package com.questbase.app.dao.transaction;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.net.entity.transactions.PayoutEventDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;

import java.util.List;

@Table(database = RespoDatabase.class)
public class PayoutEventModel extends RespoBaseModel {

    @Column
    @PrimaryKey
    public long id;

    @Column
    public String account;

    @Column
    public String service;

    @Column
    public float amount;

    @Column
    public String state;

    @Column
    public long timeUpdated;

    @Column
    public String details;

    @Column
    public String userId;

    public List<TransactionModel> transactions;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "transactions")
    public List<TransactionModel> getTransactions() {
        if (transactions == null || transactions.isEmpty()) {
            transactions = SQLite.select()
                    .from(TransactionModel.class)
                    .where(TransactionModel_Table.payoutEventId.eq(String.valueOf(id)))
                    .queryList();
        }
        return transactions;
    }

    public PayoutEventModel(PayoutEventDto payoutEventDto, String userId) {
        this.id = payoutEventDto.id;
        this.account = payoutEventDto.account;
        this.service = payoutEventDto.service;
        this.amount = payoutEventDto.amount;
        this.state = payoutEventDto.state;
        this.timeUpdated = payoutEventDto.timeUpdated.getTime();
        this.details = payoutEventDto.details;
        this.userId = userId;
    }

    public PayoutEventModel() {
    }

    public PayoutEventDto toPayoutEventDto() {
        return new PayoutEventDto(this);
    }

    public TransactionEventsContainerDto toTransactionEventContainer() {
        return new TransactionEventsContainerDto(toPayoutEventDto(), 0,
                Stream.of(transactions).map(TransactionModel::toTransaction).collect(Collectors.toList()));
    }
}