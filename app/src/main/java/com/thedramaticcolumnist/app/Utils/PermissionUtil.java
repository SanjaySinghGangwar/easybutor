package com.thedramaticcolumnist.app.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class PermissionUtil implements ActivityCompat.OnRequestPermissionsResultCallback {
    public final static int REQUEST_CODE = 1000;
    private static String[] galleryPermissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    private static String[] cameraPermissions = {
            "android.permission.CAMERA"
    };

    private static String[] contactPermissions = {
            "android.permission.READ_CONTACTS",
    };

    public static String[] getGalleryPermissions() {
        return galleryPermissions;
    }

    public static String[] getCameraPermissions() {
        return cameraPermissions;
    }

    public static String[] getContactPermissions() {
        return contactPermissions;
    }

    public static boolean verifyPermissions(Context context, String[] grantResults) {
        for (String result : grantResults) {
            if (ActivityCompat.checkSelfPermission(context, result) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            if (ActivityCompat.checkSelfPermission(context, result) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(String[] grantResults, Activity activity) {
        ActivityCompat.requestPermissions(activity, grantResults, REQUEST_CODE);

    }



    public boolean checkMarshMellowPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {

    }





}
