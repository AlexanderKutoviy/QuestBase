package com.questbase.app.controller.files;

import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;
import com.questbase.app.net.body.ResourceRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FilesController {

    void saveToFile(ResourceRequest body, Form form, FormResource resource);

    void saveFormattedFile(String url, Form form, FormResource resource);

    void saveUsersAvatar(String avatarUrl);

    void saveProjectExamplesResources(String url);

    String formatRequestUrl(FormResource resource, Form form);

    File getFormJsPath(long formId);

    File getBasePath();

    File getUsersAvatarPath();

    File getProjectExamplesResourcesBasePath(String url);

    String loadFileByPath(String path);

    String getLibPath(String libVersion);

    InputStream openFileByName(String fileName) throws IOException;

    File getFormDirectory(int formId);

    boolean hasResources(long formId);
}