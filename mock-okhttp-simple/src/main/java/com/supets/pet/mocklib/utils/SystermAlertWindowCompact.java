package com.supets.pet.mocklib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.MainThread;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.supets.pet.mocklib.R;

public class SystermAlertWindowCompact {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 0x1002;

    @MainThread
    public static void checkPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(context)) {
                startService(context);
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    context.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            startService(context);
        }

    }

    public static void checkPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(context)) {
                startService(context);
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            startService(context);
        }

    }
    @MainThread
    protected static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(context)) {
                    Toast.makeText(context, context.getString(R.string.permission_fail), Toast.LENGTH_SHORT).show();
                } else {
                    startService(context);
                }
            }
        }
    }


    public static void startService(Context context) {
        Intent intent = new Intent(context, FBService.class);
        intent.putExtra("startType", 1);
        ContextCompat.startForegroundService(context, intent);
    }
}
