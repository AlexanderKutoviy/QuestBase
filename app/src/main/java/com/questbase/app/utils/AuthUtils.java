package com.questbase.app.utils;


import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.util.Base64;

import com.questbase.app.CommonUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AuthUtils {

    public static final String TOKEN_PARAM = "token";
    public static final String USER_ID_PARAM = "id";
    public static final String TOKEN_ID_PARAM = "tid";
    public static final String HASH_ALGORITHM = "HmacSHA256";
    private static final String LOGIN_URL_FORMAT = CommonUtils.THERESPO_HOST_PREFIX
            + "/user/login/%s?android=%s";
    private static final String LOCAL_URL_PREFIX = "therespo://local.therespo.com";
    private static final String SIGNATURE_PARAM = "sgn";

    public static String createVkLoginUrl(Context ctx) {
        return createLoginUrl(ctx, "vkontakte");
    }

    public static String createFbLoginUrl(Context ctx) {
        return createLoginUrl(ctx, "facebook");
    }

    public static String createGpLoginUrl(Context ctx) {
        return createLoginUrl(ctx, "google");
    }

    public static String createOkLoginUrl(Context ctx) {
        return createLoginUrl(ctx, "ok");
    }

    private static String createLoginUrl(Context ctx, String resource) {
        String androidId = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return String.format(LOGIN_URL_FORMAT, resource, androidId);
    }

    public static boolean isLocalUrl(String url) {
        return url.startsWith(LOCAL_URL_PREFIX);
    }

    public static String extractParam(String url, String paramName) {
        Uri uri = Uri.parse(url);
        for (String name : uri.getQueryParameterNames()) {
            if (name.equals(paramName)) {
                return uri.getQueryParameter(name);
            }
        }
        return null;
    }

    /**
     * Appends signature param to given uri.
     * Uri is expected to come with no protocol and no host.
     * On input, it has to be like: /device/forms
     * On output, /device/forms?tokenId=123&sgn=E3J8a12bGW2,
     * where signature is base64 encoded with no '=' symbol, '+' changed to '-' and '/' to '*'
     *
     * @param uriPath  uri 'path' part, like /device/forms
     * @param tokenId  public token part (token id in db)
     * @param token    private token part (the token itself)
     * @param postData string data to post
     * @return signed uri path with signature and token id appended as request params
     */
    public static String createSignedUriPath(
            String uriPath, String tokenId, String token, String postData) {
        String dataToSign = uriPath + postData;
        return uriPath + '?' +
                TOKEN_ID_PARAM + '=' + tokenId + '&' +
                SIGNATURE_PARAM + '=' + packSignature(calculateSignature(dataToSign, token));
    }

    /**
     * As long as signature is expected to come in base64, it may break url parsing.
     * This method replaces '+' and '/' to '*' and '-' along with cutting '='
     */
    public static String packSignature(String signature) {
        return signature.replace('+', '-').replace('/', '*').replaceAll("=", "").replaceAll("\n", "");
    }

    /**
     * Signs base string with hmac
     */
    public static String calculateSignature(String baseString, String key) {
        try {
            Mac mac = Mac.getInstance(HASH_ALGORITHM);
            SecretKeySpec secret = new SecretKeySpec(key.getBytes(), HASH_ALGORITHM);
            mac.init(secret);
            byte[] digest = mac.doFinal(baseString.getBytes());
            return Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (Exception e) {
            Lu.handleException(e);
        }
        return null;
    }
}
