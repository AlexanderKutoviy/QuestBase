package com.questbase.app.dao.testsession;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.controller.testsession.TestSession;

@Table(database = RespoDatabase.class)
public class TestSessionModel extends RespoBaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column
    public long formId;

    @Column
    public long formVersion;

    @Column
    public String state;

    @Column
    public Boolean completed;

    @Column
    public Boolean sent;

    public TestSessionModel() {
    }

    public TestSessionModel(TestSession testSession) {
        formId = testSession.formId;
        formVersion = testSession.formVersion;
        state = testSession.state;
        completed = testSession.completed;
        sent = testSession.sent;
    }

    public TestSession toTestSession() {
        return new TestSession(id, formId, formVersion, state, completed, sent);
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "TestSessionModel{" +
                "id=" + id +
                ", formId=" + formId +
                ", formVersion=" + formVersion +
                ", state='" + state + '\'' +
                ", completed=" + completed +
                ", sent=" + sent +
                '}';
    }
}
