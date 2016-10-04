package com.questbase.functionaltestssuite.controller;

import com.annimon.stream.Optional;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.transaction.DefaultTransactionController;
import com.questbase.app.controller.transaction.TransactionController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.transaction.DbFlowPayoutEventDao;
import com.questbase.app.dao.transaction.DbFlowTransactionDao;
import com.questbase.app.dao.transaction.PayoutEventDao;
import com.questbase.app.dao.transaction.TransactionDao;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.entity.transactions.PayoutEventDto;
import com.questbase.app.net.entity.transactions.TransactionDto;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;
import com.questbase.app.net.entity.transactions.TransactionsResponseDto;
import com.questbase.app.utils.Auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DefaultTransactionControllerTests {

    private final Auth auth = new Auth("1", "1", "1");
    private final int AMOUNT_COEF = 5;

    @Test
    public void createReadTransactions() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TransactionController transactionController = initTransactionsController();
        int eventsAmount = (int) (Math.random() * AMOUNT_COEF);
        int transactionsAmount = (int) (Math.random() * AMOUNT_COEF);
        TransactionsResponseDto responseDto = getTransactions(eventsAmount, transactionsAmount);

        for (TransactionEventsContainerDto eventsContainerDto : responseDto.events) {
            transactionController.addTransaction(eventsContainerDto, auth.userId);
        }

        List<TransactionEventsContainerDto> payoutEventsFromDb = transactionController.getPayoutEventsWithTransactions();
        assertEquals(responseDto.events, payoutEventsFromDb);

        FlowManager.destroy();
    }

    private TransactionsResponseDto getTransactions(int eventsAmount, int transactionsAmount) {
        int coef = 0;
        TransactionsResponseDto transactionsResponseDto = new TransactionsResponseDto();
        for (int i = 0; i < eventsAmount; i++) {
            List<TransactionDto> transactions = new ArrayList<>();
            for (int j = coef; j < coef + transactionsAmount; j++) {
                transactions.add(new TransactionDto(j, j, new Date(j), String.valueOf(j), String.valueOf(j)));
            }
            coef += transactionsAmount;

            TransactionEventsContainerDto transactionEventsContainerDto =
                    new TransactionEventsContainerDto(new PayoutEventDto(i, String.valueOf(i),
                            String.valueOf(i), i, String.valueOf(i),0, "0"), 0, transactions);
            transactionsResponseDto.events.add(transactionEventsContainerDto);
        }
        return transactionsResponseDto;
    }

    private DefaultTransactionController initTransactionsController() {
        TransactionDao transactionDao = new DbFlowTransactionDao();
        PayoutEventDao payoutEventDao = new DbFlowPayoutEventDao();
        RestApi restApi = mock(RestApi.class);
        AuthDao authDao = mock(AuthDao.class);
        when(authDao.getAuth()).thenReturn(Optional.of(auth));
        return new DefaultTransactionController(transactionDao, payoutEventDao, restApi, authDao);
    }
}