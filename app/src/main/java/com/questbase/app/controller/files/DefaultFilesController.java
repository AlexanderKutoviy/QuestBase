package com.questbase.app.controller.files;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.questbase.app.domain.Form;
import com.questbase.app.domain.FormResource;
import com.questbase.app.net.RestApi;
import com.questbase.app.net.body.ResourceRequest;
import com.questbase.app.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaultFilesController implements FilesController {

    private final Context context;
    private final RestApi restApi;
    private static final String TAG = DefaultFilesController.class.getSimpleName();
    private static final String USER_AVATAR = "user_avatar.jpg";
    private static final String FORM_JS = "form.js";
    private static final String PROFILE = "profile";
    private static final int BYTES_AMOUNT = 1024;
    private static final String PROJECT_EXAMPLES_DIR = "projectExamplesResources";

    public DefaultFilesController(Context context, RestApi restApi) {
        this.context = context;
        this.restApi = restApi;
    }

    @Override
    public void saveToFile(ResourceRequest body, Form form, FormResource resource) {
        File allResourcesDir = FileUtils.getFormsParentDirectory(context);
        allResourcesDir.mkdirs();
        File formResourcesDir = new File(allResourcesDir, String.valueOf(form.formId));
        try {
            formResourcesDir.mkdirs();
            Response<ResponseBody> response = restApi.loadFiles(body).execute();
            loadFileViaRetrofit(response.body().byteStream(), formResourcesDir, resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveFormattedFile(String url, Form form, FormResource resource) {
        restApi.loadFormattedFiles(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    File allResourcesDir = FileUtils.getFormsParentDirectory(context);
                    allResourcesDir.mkdirs();
                    File formResourcesDir;
                    try {
                        formResourcesDir = new File(allResourcesDir, String.valueOf(form.formId));
                        formResourcesDir.mkdirs();
                        loadFileViaRetrofit(response.body().byteStream(), formResourcesDir, resource);
                        formResourcesDir.deleteOnExit();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.d(TAG, "server contact failed : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "No response : " + t);
            }
        });
    }

    @Override
    public void saveUsersAvatar(String avatarUrl) {
        restApi.loadUsersAvatar(avatarUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    File allResourcesDir = FileUtils.getFormsParentDirectory(context);
                    allResourcesDir.mkdirs();
                    File avatarDir = new File(allResourcesDir, "profile");
                    try {
                        avatarDir.mkdirs();
                        loadFileViaRetrofit(response.body().byteStream(), avatarDir);
                        avatarDir.deleteOnExit();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.d(TAG, "server contact failed : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "No response : " + t);
            }
        });
    }

    @Override
    public void saveProjectExamplesResources(String projectExampleAvatarUrl) {
        restApi.loadFormattedFiles(projectExampleAvatarUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    File allResourcesDir = FileUtils.getFormsParentDirectory(context);
                    allResourcesDir.mkdirs();
                    File projectExamplesDir = new File(allResourcesDir, PROJECT_EXAMPLES_DIR);
                    try {
                        projectExamplesDir.mkdirs();
                        loadFileViaRetrofit(response.body().byteStream(), projectExamplesDir, projectExampleAvatarUrl);
                        projectExamplesDir.deleteOnExit();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.d(TAG, "server contact failed: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "No response : " + t);
            }
        });
    }

    @Override
    public String formatRequestUrl(FormResource resource, Form form) {
        String[] chunks = resource.resId.split("\\.");
        String title = chunks[0];
        String type = chunks[1];
        String dimens = "150x100";
        String crc32 = resource.version;
        if (title.equals("avatar")) {
            dimens = "160x160";
        }
        String subBaseUrl = "media/form-js/" + form.formId + "/";
        return subBaseUrl + title + "." + dimens + "." + crc32 + "." + type;
    }

    @Override
    public File getFormJsPath(long formId) {
        File formResourcesPath = new File(FileUtils.getFormsParentDirectory(context), String.valueOf(formId));
        File formJsPath = new File(formResourcesPath, FORM_JS);
        if (!formJsPath.exists()) {
            return null;
        }
        return formJsPath;
    }

    @Override
    public File getBasePath() {
        return FileUtils.getFormsParentDirectory(context);
    }

    @Override
    public File getUsersAvatarPath() {
        File base = new File(FileUtils.getFormsParentDirectory(context), PROFILE);
        return new File(base, USER_AVATAR);
    }

    @Override
    public File getProjectExamplesResourcesBasePath(String url) {
        File file = new File(FileUtils.getFormsParentDirectory(context), PROJECT_EXAMPLES_DIR);
        return new File(file, verifyResName(url));
    }

    @Override
    public String loadFileByPath(String path) {
        StringBuilder buf = new StringBuilder();
        InputStream json;
        try {
            json = context.getAssets().open(path);
        } catch (IOException e) {
            Log.e(TAG, "Can't open " + path + e);
            throw new RuntimeException("Can't open " + path + e);
        }
        BufferedReader in;
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(json, "UTF-8");
            in = new BufferedReader(inputStreamReader);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Can't create InputStreamReader for " + json + " " + e);
            throw new RuntimeException(e);
        }
        String str;
        try {
            while ((str = in.readLine()) != null) {
                buf.append(str).append("\n");
            }
            return buf.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                json.close();
                inputStreamReader.close();
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getLibPath(String libVersion) {
        AssetManager assetManager = context.getAssets();
        String libPath = "assets:";
        String js[];
        String libs[];
        String libVersions[];
        try {
            libPath = libPath + "js";
            js = assetManager.list("js");
            if (js.length == 0) {
                Log.e(TAG, "No js directory");
            } else {
                libPath = libPath + "/" + "libs";
                libs = assetManager.list("js" + "/" + "libs");
                if (libs.length == 0) {
                    Log.e(TAG, "No libs directory");
                } else {
                    libPath = libPath + "/" + libVersion;
                    libVersions = assetManager.list("js" + "/" + "libs" + "/" + libVersion);
                    if (libVersions.length == 0) {
                        Log.e(TAG, "No " + libVersion + " directory");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to get asset file list.", e);
        }
        return libPath;
    }

    @Override
    public InputStream openFileByName(String fileName) throws IOException {
        return context.getAssets().open(fileName);
    }

    @Override
    public File getFormDirectory(int formId) {
        return new File(FileUtils.getFormsParentDirectory(context), String.valueOf(formId));
    }

    @Override
    public boolean hasResources(long formId) {
        File formResourcesPath = new File(FileUtils.getFormsParentDirectory(context), String.valueOf(formId));
        File formJsPath = new File(formResourcesPath, FORM_JS);
        return formJsPath.exists();
    }

    /**
     * @param is       is server input stream to load avatar from
     * @param formDir  device file path to load avatar to
     * @param resource we get file name form resource
     */
    private void loadFileViaRetrofit(InputStream is, File formDir, FormResource resource) throws IOException {
        File target = new File(formDir, verifyResName(resource.resId));
        InputStream input = new BufferedInputStream(is);
        OutputStream output = new FileOutputStream(target);
        byte data[] = new byte[BYTES_AMOUNT];
        int count;
        try {
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
        } finally {
            try {
                output.close();
            } finally {
                input.close();
            }
        }
    }

    /**
     * @param is      is server input stream to load avatar from
     * @param formDir device file path to load avatar to
     */
    private void loadFileViaRetrofit(InputStream is, File formDir) throws IOException {
        File target = new File(formDir, USER_AVATAR);
        InputStream input = new BufferedInputStream(is);
        OutputStream output = new FileOutputStream(target);
        byte data[] = new byte[BYTES_AMOUNT];
        int count;
        try {
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
        } finally {
            try {
                output.close();
            } finally {
                input.close();
            }
        }
    }

    /**
     * @param is             is server input stream to load avatar from
     * @param projectExample device file path to load content to
     * @param url            name of file
     */
    private void loadFileViaRetrofit(InputStream is, File projectExample, String url) throws IOException {
        File target = new File(projectExample, verifyResName(url));
        InputStream input = new BufferedInputStream(is);
        OutputStream output = new FileOutputStream(target);
        byte data[] = new byte[BYTES_AMOUNT];
        int count;
        try {
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
        } finally {
            try {
                output.close();
            } finally {
                input.close();
            }
        }
    }

    private String verifyResName(String resourceName) {
        if (resourceName.contains("/")) {
            String[] splitFileName = resourceName.split("/");
            return splitFileName[splitFileName.length - 1];
        }
        return resourceName;
    }
}