package com.questbase.app.dao.resource;

import com.annimon.stream.Optional;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;

import java.util.List;

public interface ResourceDao {

    void create(FormResource formResource);

    Optional<FormResource> read(FormResource formResource);

    List<FormResource> read(Form form);

    List<FormResource> getAllResources();

    void update(FormResource formResource);

    void delete(FormResource formResource);

    String getFilePath(FormResource formResource);
}
