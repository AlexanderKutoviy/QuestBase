package com.questbase.app.net.entity.transactions;

import com.questbase.app.utils.Objects;

import java.util.Date;
import java.util.List;

public class TransactionEventsContainerDto {

    public PayoutEventDto event;
    public Date timestamp;
    public List<TransactionDto> transactions;

    public TransactionEventsContainerDto() {
    }

    public TransactionEventsContainerDto(PayoutEventDto event, long timestamp, List<TransactionDto> transactions) {
        this.event = event;
        this.timestamp = new Date(timestamp);
        this.transactions = transactions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(event,
                timestamp,
                transactions);
    }

    @Override
    public String toString() {
        return "TransactionEventsContainerDto{" +
                "event=" + event +
                ", timestamp=" + timestamp +
                ", transactions=" + transactions +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof TransactionEventsContainerDto)) {
            return false;
        }
        TransactionEventsContainerDto transactionEventsContainerDto = (TransactionEventsContainerDto) object;
        return Objects.equal(event, transactionEventsContainerDto.event)
                && Objects.equal(timestamp, transactionEventsContainerDto.timestamp)
                && Objects.equal(transactions, transactionEventsContainerDto.transactions);
    }
}