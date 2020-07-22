package com.app.chalo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;

import androidx.core.content.PermissionChecker;

import com.app.chalo.App;
import com.danikula.videocache.HttpProxyCacheServer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Utils {

    public static File GetVideoFile(Context context, String videoId){
        return new File( context.getCacheDir(),videoId);
    }


    public static File FetchTempDir(Context context){
        File file = new File( context.getExternalCacheDir(),"temp");
        if(!file.exists())
            file.mkdirs();
        return file;
    }

    public static String FetchFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor =context. getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    public static void CopyFile(Context context, Uri uri, File dest) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        try {
             is =context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
    public static void CopyFile(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void SaveFile(String url, String filePath){
        try {
            URL website = new URL(url);

            FileUtils.copyURLToFile(website, new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SaveFileToCache(Context context, String uri){

        try{

            HttpProxyCacheServer proxy = App. getProxy(context);
            URL url = new URL(uri);
            InputStream inputStream = url.openStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
            }
        }
        catch (Exception ex){}
    }
    public static boolean SelfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = false;

        int targetSdkVersion =  Build.VERSION_CODES.M;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    public static File FetchFile(Context context, String videoId){
        return new File( context.getCacheDir(),videoId);
    }



    public static void SaveUserName(Context context, String userName){

        SharedPreferences mySharedPreferences =context. getSharedPreferences(App.Prefs, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(App.User ,userName);
        editor.apply();
    }

    public static String GetUserName(Context context){

        SharedPreferences mySharedPreferences =context. getSharedPreferences(App.Prefs, Activity.MODE_PRIVATE);
        return mySharedPreferences.getString(App.User,null);
    }

    public static void SaveString(String id,Context context, String value){

        SharedPreferences mySharedPreferences =context. getSharedPreferences(App.Prefs, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(id ,value);
        editor.apply();
    }

    public static String GetString(String id,Context context){

        SharedPreferences mySharedPreferences =context. getSharedPreferences(App.Prefs, Activity.MODE_PRIVATE);
        return mySharedPreferences.getString(id,null);
    }


}
