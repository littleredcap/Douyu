package liang.zhou.lane8.no5.my_player;

import android.app.Application;
import android.util.Log;

import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_business.login_register.DefaultL_R;
import liang.zhou.lane8.no5.my_business.login_register.Login_Register;

public class MyApplication extends Application {

    public User currentUser;
    private Login_Register lr = new DefaultL_R();

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        currentUser=new User();
        /*Log.d("onApplicationCreate()",lr.login("陈钰琪_yukee",
                "199208044038",currentUser));*/
    }
    private String lowerCaseFirstChar(String key) {
        StringBuffer s=new StringBuffer();
        s.append(Character.toLowerCase(key.charAt(0))).append(key.substring(1));
        return s.toString();
    }
    public void updateUser(String json){
        JSONObject jsonObject;
        String updatedColumn="";
        String columnValue="";
        try {
            jsonObject=new JSONObject(json);
            updatedColumn=jsonObject.getString("updatedColumn");
            columnValue=jsonObject.getString(lowerCaseFirstChar(updatedColumn));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Class<?> class_user;
        Method method;
        try {
            class_user=currentUser.getClass();
            method=class_user.getMethod("set"+updatedColumn,String.class);
            method.invoke(currentUser,columnValue);
            Log.d("myApplication",currentUser.getAlwaysAppearance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public class UserUpdateObservable extends Observable{

    }
}
