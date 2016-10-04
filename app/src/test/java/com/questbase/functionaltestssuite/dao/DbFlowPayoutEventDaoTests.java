package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.transaction.DbFlowPayoutEventDao;
import com.questbase.app.dao.transaction.PayoutEventDao;
import com.questbase.app.net.entity.transactions.PayoutEventDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowPayoutEventDaoTests {

    private final int AMOUNT = 10;
    private final String userId = "111";

    @Test
    public void createReadPayoutEventTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        PayoutEventDao payoutEventDao = new DbFlowPayoutEventDao();

        for (PayoutEventDto payoutEventDto : initPayoutEventsList()) {
            payoutEventDao.create(payoutEventDto, userId);
        }

        assertEquals(initPayoutEventsList(), payoutEventDao.read(userId));
        FlowManager.destroy();
    }

    private List<PayoutEventDto> initPayoutEventsList() {
        List<PayoutEventDto> list = new ArrayList<>(AMOUNT);
        for (int i = 0; i < AMOUNT; i++) {
            list.add(new PayoutEventDto(i, String.valueOf(i), String.valueOf(i), i, String.valueOf(i), 0, "0"));
        }
        return list;
    }
}