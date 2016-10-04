package com.questbase.app.profile;

import android.graphics.Bitmap;

import com.questbase.app.domain.Form;
import com.questbase.app.domain.UserEmailDto;
import com.questbase.app.domain.UserPhoneDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;

import java.util.List;
import java.util.Map;

public interface PersonalCabView {

    void renderAvatar(Bitmap img);

    void renderPassedFormsGraphs(Map<String, Float> values);

    void renderBalance(String balance);

    void renderAmountOfFormsInProcess(String formsInProcess);

    void renderTimeOfLastTransaction(String time);

    void renderAmountOfTransactions(String amountOfTransactions);

    void renderAmountOfCharityTransactions(String amountOfCharityTransactions);

    void renderListOfPassedForms(List<Form> passedForms);

    void noRenderFormsBlock();

    void renderListOfEmails(List<UserEmailDto> emails);

    void renderListOfPhones(List<UserPhoneDto> phones);

    void showPerformTransactionDialog();

    void showTransactionHistoryDialog(List<TransactionEventsContainerDto> eventsWithTransactions);

    void createCellConfirmationDialog();

    void createAllTransactionDialog();

    void showMessageWithUserBalance(Float balance);

    void showMessageWithUserPhone(String phone, Float money);

    void setCanApprove(boolean canApprove);

    void setCanApprovePhone(boolean canApprove);

    void createConfirmationCharityPopup(String charityTarget);

    void createConfirmationPhonePopup(String phone);

    void createPhoneConfirmationDialog(Float money);
}