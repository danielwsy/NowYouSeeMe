package com.seeme.daniel.nowyouseeme;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/20 15:06
 */
public class PermissionUtils {


    public static void askPermission(Activity context, String[] permissions, int req, Runnable
            runnable){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int result= 0;
            for(String a:permissions){
                result+= ActivityCompat.checkSelfPermission(context,a);
            }
            if(result== PackageManager.PERMISSION_GRANTED){
                runnable.run();
            }else{
                ActivityCompat.requestPermissions(context,permissions,req);
            }
        }else{
            runnable.run();
        }
    }
}
