package com.questbase.app.net.entity.transactions;

import com.questbase.app.dao.transaction.PayoutEventModel;
import com.questbase.app.utils.Objects;

import java.util.Date;

public class PayoutEventDto {

    public long id;
    public String account;
    public String service;
    public float amount;
    public String state;
    public Date timeUpdated;
    public String details;

    public PayoutEventDto() {
    }

    public PayoutEventDto(long id, String account, String service, float amount, String state,
                          long timeUpdated, String details) {
        this.id = id;
        this.account = account;
        this.service = service;
        this.amount = amount;
        this.state = state;
        this.timeUpdated = new Date(timeUpdated);
        this.details = details;
    }

    public PayoutEventDto(PayoutEventModel payoutEventModel) {
        this.id = payoutEventModel.id;
        this.account = payoutEventModel.account;
        this.service = payoutEventModel.service;
        this.amount = payoutEventModel.amount;
        this.state = payoutEventModel.state;
        this.timeUpdated = new Date(payoutEventModel.timeUpdated);
        this.details = payoutEventModel.details;
    }

    public boolean isNull() {
        return (id == 0 &&
                account == null &&
                service == null &&
                amount == 0 &&
                state == null &&
                timeUpdated == null &&
                details == null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                account,
                service,
                amount,
                state,
                timeUpdated,
                details);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PayoutEventDto)) {
            return false;
        }
        PayoutEventDto payoutEventDto = (PayoutEventDto) object;
        return Objects.equal(id, payoutEventDto.id)
                && Objects.equal(account, payoutEventDto.account)
                && Objects.equal(service, payoutEventDto.service)
                && Objects.equal(amount, payoutEventDto.amount)
                && Objects.equal(timeUpdated, payoutEventDto.timeUpdated)
                && Objects.equal(details, payoutEventDto.details)
                && Objects.equal(state, payoutEventDto.state);
    }

    @Override
    public String toString() {
        return "PayoutEventDto{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", service='" + service + '\'' +
                ", amount=" + amount +
                ", state='" + state + '\'' +
                ", timeUpdated=" + timeUpdated +
                ", details='" + details + '\'' +
                '}';
    }
}