package com.example.tiktoksdkplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TikTokAuthBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the received TikTok authentication result
        TikTokSDKPlugin.handleTikTokAuthResult(intent);
    }
}