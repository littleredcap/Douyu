package liang.zhou.lane8.no5.my_player;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class ActivityPermission extends AppCompatActivity {

    protected final int CALENDAR=0;
    protected final int CAMERA=1;
    protected final int CONTACTS=2;
    protected final int LOCATION=3;
    protected final int MICROPHONE=4;
    protected final int PHONE=5;
    protected final int SENSORS=6;
    protected final int SMS=7;
    protected final int STORAGE=8;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void calendar(Context ctx){
        String permission=Manifest.permission.WRITE_CALENDAR;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) ctx,permission)){

        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},CALENDAR);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void camera(Context ctx){
        String permission=Manifest.permission.CAMERA;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},CAMERA);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void contacts(Context ctx){
        String permission=Manifest.permission.WRITE_CONTACTS;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},CONTACTS);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void location(Context ctx){
        String permission=Manifest.permission.ACCESS_FINE_LOCATION;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},LOCATION);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void microphone(Context ctx){
        String permission=Manifest.permission.RECORD_AUDIO;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},MICROPHONE);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void phone(Context ctx){
        String permission=Manifest.permission.READ_CALL_LOG;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},PHONE);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sensors(Context ctx){
        String permission=Manifest.permission.BODY_SENSORS;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},SENSORS);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sms(Context ctx){
        String permission=Manifest.permission.READ_SMS;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},SMS);//调用这个方法会弹出一个对话框
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void storage(Context ctx){
        String permission=Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ctx.checkSelfPermission(permission)==
                PackageManager.PERMISSION_GRANTED){
            afterPermissionGranted();
            return;
        }
        ActivityCompat.requestPermissions((Activity) ctx,
                new String[]{permission},STORAGE);//调用这个方法会弹出一个权限申请对话框
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("PermissionsResult",
                ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0])+"rationale");
        //app安装后首次启动，rationale返回false，同时权限肯定未获取，因此不用跟踪app是否首次启动
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Log.d("PermissionsResult", "granted");
            //rationale如果在此处调用则返回false
            afterPermissionGranted();
        }else if (grantResults[0]==PackageManager.PERMISSION_DENIED){
            //拒绝了权限，在点击“不再询问”前，无论再拒绝多少次，rational都返回true；
            //一旦点击了rationale，则返回false
            //MyToast.show(getApplication(),"使用此功能你必须允许此权限");
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0])){
                //提示用户：如果你不允许此权限，则无法使用这个功能
            }else{
                //提示用户：如果想开启此权限，可以到设置里面开启权限
            }
        }
    }

    protected abstract void afterPermissionGranted();
}
