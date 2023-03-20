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
