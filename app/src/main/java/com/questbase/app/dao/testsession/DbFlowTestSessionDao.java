package com.questbase.app.dao.testsession;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.controller.testsession.TestSession;
import com.questbase.app.domain.Form;
import com.questbase.app.utils.OptionalUtils;

import java.util.List;

public class DbFlowTestSessionDao implements TestSessionDao {

    @Override
    public void create(TestSession testSession) {
        new TestSessionModel(testSession).saveOnDuplicateUpdate();
    }

    @Override
    public Optional<TestSession> read(TestSession testSession) {
        return OptionalUtils.fromCollection(
                SQLite.select().from(TestSessionModel.class)
                        .where(TestSessionModel_Table.formId.eq(testSession.formId))
                        .and(TestSessionModel_Table.formVersion.eq(testSession.formVersion))
                        .limit(1).queryList())
                .map(TestSessionModel::toTestSession);
    }

    @Override
    public Optional<List<TestSession>> readCompletedNotSentTestSessions() {
        List<TestSessionModel> testSessionModels = SQLite.select().from(TestSessionModel.class)
                .where(TestSessionModel_Table.completed.eq(true))
                .and(TestSessionModel_Table.sent.eq(false))
                .queryList();
        return Optional.of(Stream.of(testSessionModels)
                .map(TestSessionModel::toTestSession)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<TestSession> lookup(Form form) {
        return OptionalUtils.fromCollection(
                SQLite.select().from(TestSessionModel.class)
                        .where(TestSessionModel_Table.formId.eq(form.formId))
                        .and(TestSessionModel_Table.formVersion.eq(form.version))
                        .and(TestSessionModel_Table.completed.eq(false))
                        .limit(1).queryList())
                .map(TestSessionModel::toTestSession);
    }

    @Override
    public void delete(TestSession testSession) {
        SQLite.delete(TestSessionModel.class)
                .where(TestSessionModel_Table.id.eq(testSession.id))
                .execute();
    }

    @Override
    public void updateByFormIdAndVersion(TestSession testSession) {
        SQLite.update(TestSessionModel.class)
                .set(TestSessionModel_Table.completed.eq(testSession.completed),
                        TestSessionModel_Table.state.eq(testSession.state))
                .where(TestSessionModel_Table.formId.eq(testSession.formId))
                .and(TestSessionModel_Table.formVersion.eq(testSession.formVersion))
                .execute();
    }

    @Override
    public void updateSentStatus(int id, boolean sent) {
        SQLite.update(TestSessionModel.class)
                .set(TestSessionModel_Table.sent.eq(sent))
                .where(TestSessionModel_Table.id.eq(id))
                .execute();
    }
}