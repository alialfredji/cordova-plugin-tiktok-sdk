package com.example.tiktoksdkplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.bytedance.sdk.open.tiktok.TikTokOpenApiFactory;
import com.bytedance.sdk.open.tiktok.TikTokOpenConfig;
import com.bytedance.sdk.open.tiktok.api.TikTokOpenApi;
import com.bytedance.sdk.open.tiktok.authorize.model.Authorization;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.pm.PackageManager;

public class TikTokSDKPlugin extends CordovaPlugin {

    private TikTokOpenApi tiktokOpenApi;
    private static CallbackContext loginCallbackContext;

    @Override
    protected void pluginInitialize() {
        String packageName = cordova.getActivity().getPackageName();
        int resId = cordova.getActivity().getResources().getIdentifier("TikTokAppID", "string", packageName);
        String clientKey = cordova.getActivity().getString(resId);
        TikTokOpenConfig tikTokOpenConfig = new TikTokOpenConfig(clientKey);
        TikTokOpenApiFactory.init(tikTokOpenConfig);
        tiktokOpenApi = TikTokOpenApiFactory.create(cordova.getActivity());

        registerBroadcastReceiver();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("login".equals(action)) {
            login(args.getString(0), callbackContext);
            return true;
        }

        if ("isAppInstalled".equals(action)) {
            boolean isInstalled = isAppInstalled(cordova.getActivity());
            callbackContext.success(isInstalled ? 1 : 0);
            return true;
        }

        return false;
    }

    private void login(String scope, CallbackContext callbackContext) {
        boolean isInstalled = isAppInstalled(cordova.getActivity());
        if (isInstalled == false) {
            try {
                JSONObject response = new JSONObject()
                    .put("errorCode", -1000)
                    .put("errorMsg", "tiktok app not installed");
                callbackContext.error(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        this.loginCallbackContext = callbackContext;
        cordova.setActivityResultCallback(this);

        Authorization.Request request = new Authorization.Request();
        request.scope = scope;
        request.callerLocalEntry = "com.example.tiktoksdkplugin.TikTokEntryActivity";

        tiktokOpenApi.authorize(request);
    }

    private boolean isAppInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo("com.zhiliaoapp.musically", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void handleTikTokAuthResult(Intent intent) {
        if (intent != null && "com.example.tiktoksdkplugin.ACTION_AUTHORIZATION_RESPONSE".equals(intent.getAction())) {
            boolean success = intent.getBooleanExtra("success", false);

            if (success) {
                String authCode = intent.getStringExtra("authCode");
                String permissions = intent.getStringExtra("permissions");

                try {
                    JSONObject response = new JSONObject()
                        .put("code", authCode)
                        .put("permissions", permissions);
                    loginCallbackContext.success(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
            } else {
                int errorCode = intent.getIntExtra("errorCode", -1);
                String errorMsg = intent.getStringExtra("errorMsg");
                if (errorMsg == null) {
                    errorMsg = "something went wrong";
                }

                try {
                    JSONObject response = new JSONObject()
                        .put("errorCode", errorCode)
                        .put("errorMsg", errorMsg);
                    loginCallbackContext.error(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void registerBroadcastReceiver() {
        BroadcastReceiver receiver = new TikTokAuthBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.tiktoksdkplugin.ACTION_AUTHORIZATION_RESPONSE");
        cordova.getActivity().registerReceiver(receiver, filter);
    }
}
