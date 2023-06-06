package com.example.testfinder.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Map;

public class RequestPermission {
    private Activity activity;

    public void requestPermissions(Activity activity, String[] permissions) {
        Map<Integer, String> permissionsMap = new HashMap<>();
        this.activity = activity;


        for (int i = 0; i < permissions.length; i++) {
            permissionsMap.put(i, permissions[i]);
        }

        for (Map.Entry<Integer, String> permission : permissionsMap.entrySet()) {
            if (!isPermissionGranted(activity, permission.getValue())) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.getValue())) {
                    Toast.makeText(activity.getApplicationContext(), "No permission", Toast.LENGTH_LONG).show();
                }
                else {
                    ActivityCompat.requestPermissions(activity, permissions, permission.getKey());
                    return;
                }
            }
        }
    }



    private boolean isPermissionGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
