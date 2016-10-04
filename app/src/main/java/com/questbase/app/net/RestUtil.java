package com.questbase.app.net;

import com.google.gson.JsonObject;
import com.questbase.app.CommonUtils;
import com.questbase.app.utils.AuthUtils;
import com.questbase.app.utils.Lu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RestUtil {

    public static final String CONTENT_LENGTH = "Content-Length";
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String WWW_FORM = "application/json";
    private static final String CONTENT_LANGUAGE = "Content-Language";
    private static final String EN_US = "en-US";
    private static final int RESPONSE_OK = 200;
    private static final int RESPONSE_UNAUTHORIZED = 401;
    private static final String STUB_BODY_NON_ZERO_LENGTH = "1";

    private enum Path {

        FORM("/device/v1/dbForm/get"),
        FEED("/device/v1/dbForm/feed"),
        RESULT("/device/v1/dbForm/result"),
        DOWNLOAD_FORM_ZIP("/device/v1/dbForm/zip-android"),
        LIST_PAYABLE_TEST_SESSIONS("/device/v1/payout/test-session-list"),
        LIST_PAYABLE_TRANSACTIONS("/device/v1/payout/transaction-list"),
        PROFILE("/device/v1/profile/get");

        Path(String value) {
            this.value = value;
        }

        private final String value;
    }

    public static String doPayableFormTestSessionListRequest(String tokenId, String token) throws IOException {
        return doSignedPostRequest(Path.LIST_PAYABLE_TEST_SESSIONS.value, tokenId, token, "");
    }

    public static String doPayableFormTransactionListRequest(String tokenId, String token) throws IOException {
        return doSignedPostRequest(Path.LIST_PAYABLE_TRANSACTIONS.value, tokenId, token, "");
    }

    public static String doProfileRequest(String userId,
                                          String tokenId, String token) throws IOException {
        return doSignedPostRequest(
                Path.PROFILE.value, tokenId, token,
                "{\"userId\":\"" + userId + "\"}");
    }

    public static String doFormRequest(String tokenId, String token, int formId)
            throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("formId", formId);
        return doSignedPostRequest(Path.FORM.value, tokenId, token, body.toString());
    }

    public static String doFeedRequest(String tokenId, String token) throws IOException {
        return doSignedPostRequest(Path.FEED.value, tokenId, token);
    }

    public static String doPostResponseRequest(String userId, String tokenId,
                                               String token, int testSessionId,
                                               int formId, int formVersion,
                                               String sessionState, int completed,
                                               int sent) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("formId", formId);
        body.addProperty("formVersion", formVersion);
        body.addProperty("userId", userId);
        body.addProperty("id", testSessionId);
        body.addProperty("state", sessionState);
        body.addProperty("completed", completed);
        body.addProperty("sent", sent);
        return doSignedPostRequest(
                Path.RESULT.value, tokenId, token, body.toString());
    }

    public static String doSignedPostRequest(String path, String openKey, String secretKey)
            throws IOException {
        return doSignedPostRequest(path, openKey, secretKey, STUB_BODY_NON_ZERO_LENGTH);
    }

    public static String doSignedPostRequest(String path, String openKey, String secretKey, String body)
            throws IOException {
        String postUrl = CommonUtils.HOST_PREFIX +
                AuthUtils.createSignedUriPath(path, openKey, secretKey, body);
        URL url = new URL(postUrl);
        trustAllCertificates();
        byte[] bytes = body.getBytes(Charset.defaultCharset());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(POST);
        connection.setRequestProperty(CONTENT_TYPE, WWW_FORM);
        connection.setRequestProperty(CONTENT_LENGTH,
                Integer.toString(bytes.length));
        connection.setRequestProperty(CONTENT_LANGUAGE, EN_US);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        // send request
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(bytes);
        wr.flush();
        wr.close();
        // get Response
        InputStream is = connection.getInputStream();
        int responseCode = connection.getResponseCode();
        if (responseCode == RESPONSE_OK) {
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = responseReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            responseReader.close();
            return response.toString();
        } else if (responseCode == RESPONSE_UNAUTHORIZED) {
            throw new RuntimeException("Server didn't accept the signature");
        } else {
            throw new RuntimeException("Illegal server response code: " + responseCode);
        }
    }

    /**
     * WARNING: used only for debug purposes with sandbox server.
     * Never use this on production as it will provide zero security level
     */
    private static void trustAllCertificates() {
        if (!CommonUtils.IGNORE_CERTIFICATES) {
            return;
        }
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            Lu.handleException(e);
        }
    }

    public static String getZipDownloadPath() {
        return Path.DOWNLOAD_FORM_ZIP.value;
    }
}
