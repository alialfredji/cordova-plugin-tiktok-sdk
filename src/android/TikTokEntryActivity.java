package com.example.tiktoksdkplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.HashMap;

import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.common.handler.IApiEventHandler;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;
import com.bytedance.sdk.open.tiktok.common.model.BaseReq;
import com.bytedance.sdk.open.tiktok.common.model.BaseResp;

public class TikTokEntryActivity extends Activity implements IApiEventHandler {

    private TikTokOpenApi ttOpenApi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttOpenApi = TikTokOpenApiFactory.create(this);
        ttOpenApi.handleIntent(getIntent(), this); // receive and parse callback
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof Authorization.Response) {
            Authorization.Response response = (Authorization.Response) resp;

            Intent broadcastIntent = new Intent("com.example.tiktoksdkplugin.ACTION_AUTHORIZATION_RESPONSE");

            if (response.isSuccess()) {
                broadcastIntent.putExtra("success", true);
                broadcastIntent.putExtra("authCode", response.authCode);   
                broadcastIntent.putExtra("permissions", response.grantedPermissions);   
            } else {
                broadcastIntent.putExtra("success", false);
                broadcastIntent.putExtra("errorCode", response.errorCode);
                broadcastIntent.putExtra("errorMsg", errorMessageForErrorCode(response.errorCode));
            }

            sendBroadcast(broadcastIntent);
        }
        finish();
    }

    public String errorMessageForErrorCode(int errorCode) {
        HashMap<Integer, String> errorMessages = new HashMap<>();
        errorMessages.put(-1, "unknown error");
        errorMessages.put(-2, "user cancelled");
        errorMessages.put(-3, "send failed");
        errorMessages.put(-4, "auth denied");
        errorMessages.put(-5, "unsupported");
        errorMessages.put(-12, "network not connected");
        errorMessages.put(-13, "network connection timed out");
        errorMessages.put(-14, "network timeout");
        errorMessages.put(-15, "network io error");
        errorMessages.put(-16, "network unknown host error");
        errorMessages.put(-21, "network ssl error");
        errorMessages.put(-30, "user cancel login or login failure");

        errorMessages.put(2100004, "system busy");
        errorMessages.put(2100005, "invalid parameter");
        errorMessages.put(2100007, "no permission operation");
        errorMessages.put(2100009, "user is banned from using this operation");
        errorMessages.put(2190001, "quota has been used up");
        errorMessages.put(2190004, "the application has not obtained this ability");
        errorMessages.put(2190015, "request parameter access_token openid does not match");

        errorMessages.put(10002, "parameter error");
        errorMessages.put(10003, "illegal application configuration");
        errorMessages.put(10004, "illegal authorization scope");
        errorMessages.put(10005, "missing parameters");
        errorMessages.put(10006, "illegal redirection uri");
        errorMessages.put(10007, "authorization code expired");
        errorMessages.put(10008, "illegal call credentials");
        errorMessages.put(10009, "illegal parameter");
        errorMessages.put(10010, "refresh_token expired");
        errorMessages.put(10011, "application package name inconsistent");
        errorMessages.put(10012, "app is under review and cannot be authorized");
        errorMessages.put(10013, "client key or client secret error");
        errorMessages.put(10014, "authorized client key inconsistent");
        errorMessages.put(10015, "application type error");
        errorMessages.put(10017, "authorization failed");
        errorMessages.put(2190002, "user has not authorized the api");
        errorMessages.put(2190008, "access_token expired");
        errorMessages.put(6007061, "tiktok app version is too low");
        errorMessages.put(6007062, "age restriction");
    
        String errorMessage = errorMessages.get(errorCode);
        if (errorMessage != null) {
            return errorMessage;
        } else {
            return "Something went wrong";
        }
    }

    @Override
    public void onErrorIntent(@Nullable Intent intent) {
        Toast.makeText(this, "Intent Error", Toast.LENGTH_LONG).show();
    }
}
