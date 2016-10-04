package com.questbase.app.net.entity.transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionsResponseDto {
    public List<TransactionEventsContainerDto> events;

    public TransactionsResponseDto() {
        events = new ArrayList<>();
    }
}
