package com.questbase.app.controller.resource;

import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;

import java.io.InputStream;

public interface ResourceController {

    void sync(Form form);

    boolean areAllResourcesUpdated(Form form);

    InputStream getResourceStream(FormResource formResource);
}