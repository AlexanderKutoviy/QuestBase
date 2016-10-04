package com.questbase.app.dao.resource;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.dao.form.FormModel;
import com.questbase.app.dao.form.FormModel_Table;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;

import java.util.List;

public class DbFlowResourceDao implements ResourceDao {

    @Override
    public void create(FormResource formResource) {
        new ResourceModel(formResource).saveOnDuplicateUpdate();
    }

    @Override
    public Optional<FormResource> read(FormResource formResource) {
        ResourceModel resourceModel = SQLite.select().from(ResourceModel.class)
                .where(ResourceModel_Table.resId.eq(formResource.resId)).querySingle();
        return Optional.ofNullable(resourceModel).map(ResourceModel::toFormResource);
    }

    @Override
    public List<FormResource> read(Form form) {
        FormModel formModel = SQLite.select().from(FormModel.class)
                .where(FormModel_Table.id.eq(form.formId)).querySingle();
        List<ResourceModel> resourceModels = formModel != null ? formModel.getResources() : null;
        return Stream.of(resourceModels).map(ResourceModel::toFormResource).collect(Collectors.toList());
    }

    @Override
    public List<FormResource> getAllResources() {
        List<ResourceModel> list = SQLite.select().from(ResourceModel.class).queryList();
        return Stream.of(list).map(model -> read(model.toFormResource()).get())
                .collect(Collectors.toList());
    }

    @Override
    public void update(FormResource formResource) {
        create(formResource);
    }

    @Override
    public void delete(FormResource formResource) {
        new ResourceModel(formResource).delete();
    }

    @Override
    public String getFilePath(FormResource formResource) {
        try {
            ResourceModel resourceModel = SQLite.select(ResourceModel_Table.filePath).from(ResourceModel.class)
                    .where(ResourceModel_Table.resId.eq(formResource.resId)).querySingle();
            return resourceModel != null ? resourceModel.filePath : null;
        } catch (NullPointerException ex) {
            throw new RuntimeException(ex);
        }
    }
}