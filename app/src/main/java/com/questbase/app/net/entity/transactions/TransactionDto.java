package com.questbase.app.net.entity.transactions;

import com.questbase.app.dao.transaction.TransactionModel;
import com.questbase.app.utils.Objects;

import java.util.Date;

public class TransactionDto {

    public long id;
    public float amount;
    public Date timestamp;
    public String details;
    public String payoutEventId;

    public TransactionDto() {
    }

    public TransactionDto(long id, float amount, Date timestamp, String details, String payoutEventId) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.details = details;
        this.payoutEventId = payoutEventId;
    }

    public TransactionDto(TransactionModel transactionModel) {
        this.id = transactionModel.id;
        this.amount = transactionModel.amount;
        this.timestamp = new Date(transactionModel.timestamp);
        this.details = transactionModel.details;
        this.payoutEventId = transactionModel.payoutEventId;
    }

    public boolean isNull() {
        return (id == 0 &&
                amount == 0 &&
                timestamp == null &&
                details == null &&
                payoutEventId == null);
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id=" + id +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", details='" + details + '\'' +
                ", payoutEventId='" + payoutEventId + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                amount,
                timestamp,
                details,
                payoutEventId);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof TransactionDto)) {
            return false;
        }
        TransactionDto transactionDto = (TransactionDto) object;
        return Objects.equal(id, transactionDto.id)
                && Objects.equal(amount, transactionDto.amount)
                && Objects.equal(timestamp, transactionDto.timestamp)
                && Objects.equal(payoutEventId, transactionDto.payoutEventId)
                && Objects.equal(details, transactionDto.details);
    }
}