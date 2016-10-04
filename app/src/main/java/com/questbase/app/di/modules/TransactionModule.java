package com.questbase.app.di.modules;

import com.questbase.app.controller.transaction.DefaultTransactionController;
import com.questbase.app.controller.transaction.TransactionController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.dao.transaction.DbFlowPayoutEventDao;
import com.questbase.app.dao.transaction.DbFlowTransactionDao;
import com.questbase.app.dao.transaction.PayoutEventDao;
import com.questbase.app.dao.transaction.TransactionDao;
import com.questbase.app.net.RestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TransactionModule {

    @Singleton
    @Provides
    TransactionController provideTransactionController(TransactionDao transactionDao,
                                                       PayoutEventDao payoutEventDao,
                                                       RestApi restApi,
                                                       AuthDao authDao) {
        return new DefaultTransactionController(transactionDao, payoutEventDao, restApi, authDao);
    }

    @Singleton
    @Provides
    TransactionDao provideTransactionDao() {
        return new DbFlowTransactionDao();
    }

    @Singleton
    @Provides
    PayoutEventDao providePayoutEventDao() {
        return new DbFlowPayoutEventDao();
    }
}
