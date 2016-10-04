package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.transaction.DbFlowTransactionDao;
import com.questbase.app.dao.transaction.TransactionDao;
import com.questbase.app.net.entity.transactions.TransactionDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowTransactionDaoTest {

    private final int AMOUNT = 10;
    private final String userId = "111";

    @Test
    public void createReadTransactionTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        TransactionDao transactionDao = new DbFlowTransactionDao();

        for (TransactionDto transactionDto : initTransactionsList()) {
            transactionDao.create(transactionDto, userId);
        }

        assertEquals(initTransactionsList(), transactionDao.read(userId));
        FlowManager.destroy();
    }

    private List<TransactionDto> initTransactionsList() {
        List<TransactionDto> list = new ArrayList<>(AMOUNT);
        for (int i = 0; i < AMOUNT; i++) {
            list.add(new TransactionDto(i, i, new Date(i), String.valueOf(i), String.valueOf(i)));
        }
        return list;
    }
}