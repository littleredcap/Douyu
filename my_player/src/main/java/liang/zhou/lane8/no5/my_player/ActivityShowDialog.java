package liang.zhou.lane8.no5.my_player;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_business.login_register.DefaultL_R;
import liang.zhou.lane8.no5.my_business.login_register.Login_Register;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Response;

public class ActivityShowDialog extends AppCompatActivity {

    private TextView text_result;
    private Login_Register lr = new DefaultL_R();
    private PopupWindow popupWindow;
    private int viewHeight=800;
    private int viewWidth=800;
    private Tencent tencent;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getApplication();
        setContentView(liang.zhou.lane8.no5.my_business.R.layout.activity_main);
        text_result = findViewById(liang.zhou.lane8.no5.my_business.R.id.text_result);

        tencent=Tencent.createInstance("101580796", getApplicationContext());
        popupDialog();
        //popupWindow();
        //testLayoutSize();

    }

    private void popupWindow3() {
        View contentView= LayoutInflater.from(this).inflate(liang.zhou.lane8.no5.my_business.R.layout.login_reg_dialog,null);


        popupWindow=new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);

        TextView tv_login=contentView.findViewById(liang.zhou.lane8.no5.my_business.R.id.tv_login);

        tv_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(ActivityShowDialog.this,ActivityLogin.class);
                startActivity(intent);
                //setText();
                return false;
            }
        });
    }


    private void testLayoutSize(){
        final View contentView=LayoutInflater.from(this).inflate(liang.zhou.lane8.no5.my_business.R.layout.login_register_dialog, (ViewGroup) getWindow().getDecorView());
        contentView.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        int measuredHeight=contentView.getMeasuredHeight();
        int measuredWidth=contentView.getMeasuredWidth();
        int width=contentView.getWidth();
        int height=contentView.getHeight();
        text_result.setText(measuredHeight+","+measuredWidth+","+height+","+width);
    }

    private void popupWindow2(){
        /*FrameLayout viewGroup=new FrameLayout(this);
        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int popupWindowWidth=(int)(point.x);
        int popupWindowHeight=(int)(point.y);
        WindowManager.LayoutParams layoutParams= getWindow().getAttributes();
        layoutParams.height=popupWindowHeight;
        layoutParams.width=popupWindowWidth;
        viewGroup.setLayoutParams(layoutParams);*/
        View contentView= LayoutInflater.from(this).inflate(liang.zhou.lane8.no5.my_business.R.layout.login_register_dialog,null);

        //ViewHelper.setScaleX(contentView,0.78f);
        //ViewHelper.setScaleY(contentView,0.6f);
        popupWindow=new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);

    }

    private void popupWindow(){
        ViewGroup contentView= (ViewGroup) LayoutInflater.from(this).inflate(liang.zhou.lane8.no5.my_business.R.layout.login_register_dialog, null);
        LinearLayout ll_root=contentView.findViewById(liang.zhou.lane8.no5.my_business.R.id.ll_root);
        ll_root.setPadding(getAfterScaleWidth(ll_root.getPaddingLeft(),viewWidth),
                getAfterScaleHeight(ll_root.getPaddingTop(),viewHeight),
                getAfterScaleWidth(ll_root.getPaddingRight(),viewWidth),
                getAfterScaleHeight(ll_root.getPaddingBottom(),viewHeight));

        ImageView imageView=contentView.findViewById(liang.zhou.lane8.no5.my_business.R.id.iv_right_corner_icon);
        imageView.getLayoutParams().height=getAfterScaleHeight(imageView.getHeight(),viewHeight);
        imageView.getLayoutParams().width=getAfterScaleWidth(imageView.getWidth(),viewWidth);

        LinearLayout ll_below=findViewById(liang.zhou.lane8.no5.my_business.R.id.ll_below);



        popupWindow=new PopupWindow(contentView, 800,800,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
    }

    /**
     *
     * @param rawHeight the vertical length in your layout file
     * @param viewHeight the height of your view
     * @return
     */
    private int getAfterScaleHeight(int rawHeight,int viewHeight){
        int screenHeight=getResources().getDisplayMetrics().heightPixels;
        BigDecimal bigDecimal=new BigDecimal(viewHeight/screenHeight);
        float scale=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
        return Math.round(rawHeight*scale);
    }
    private int getAfterScaleWidth(int rawWidth,int viewWidth){
        int screenWidth=getResources().getDisplayMetrics().widthPixels;
        BigDecimal bigDecimal=new BigDecimal(viewWidth/screenWidth);
        float scale=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
        return Math.round(rawWidth*scale);
    }
    private void popupDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View dialogContentView=LayoutInflater.from(this).inflate(liang.zhou.lane8.no5.my_business.R.layout.login_register_dialog,null);
        AlertDialog dialog=new AlertDialog.Builder(this).setView(dialogContentView).show();
        TextView login_tv=dialogContentView.findViewById(liang.zhou.lane8.no5.my_business.R.id.tv_login);
        login_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(ActivityShowDialog.this,ActivityLogin.class);
                startActivity(intent);
                return false;
            }
        });
        ImageView qq_login=dialogContentView.findViewById(R.id.imageView_QQ);
        qq_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //dialog.dismiss();
                login();
                return false;
            }
        });
        TextView tv_reg=dialogContentView.findViewById(R.id.tv_reg);
        tv_reg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(ActivityShowDialog.this,ActivityRegister.class);
                startActivity(intent);
                return false;
            }
        });
        /*WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.height=500;
        params.width=300;
        dialog.getWindow().setAttributes(params);*/
    }

    private void toUser(User user,JSONObject resultJSON){
        try {
            user.setUserId(Integer.parseInt(resultJSON.getString("userId")));
            user.setUsername(resultJSON.getString("username"));
            user.setPassword(resultJSON.getString("password"));
            user.setSex(resultJSON.getString("sex"));
            user.setBirthday(resultJSON.getString("birthday"));
            user.setAlwaysAppearance(resultJSON.getString("alwaysAppearance"));
            user.setHomeAddress(resultJSON.getString("homeAddress"));
            user.setMailBox(resultJSON.getString("mailBox"));
            user.setMobile(resultJSON.getString("mobile"));
            user.setPortrait(resultJSON.getString("portrait"));
            user.setSubscribedFuncId(resultJSON.getString("subscribedFuncId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void login(){
        tencent.login(this, "all",loginListener);
    }

    private UserInfo userInfo=null;
    IUiListener loginListener=new IUiListener() {
        @Override
        public void onComplete(Object o) {
            Log.d("loginSuccess",o.toString());
            JSONObject json= (JSONObject) o;
            try {
                String openID = json.getString("openid");
                String accessToken = json.getString("access_token");
                String expires = json.getString("expires_in");
                OKHttpUtil.uploadJSONs(Constant.HOST + "RegisterServlet", getJson(openID), new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            JSONObject resultJSON=new JSONObject(response.body().string());
                            toUser(app.currentUser,resultJSON);
                            if(app.currentUser.getUserId()>0){
                                Intent intent=new Intent(ActivityShowDialog.this,
                                        ActivityHomePage.class);
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e){

                        }
                    }
                });
                tencent.setOpenId(openID);
                tencent.setAccessToken(accessToken, expires);
                QQToken qqToken = tencent.getQQToken();
                userInfo=new UserInfo(getApplicationContext(),qqToken);

                userInfo.getOpenId(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.d("getUserInfo",o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }catch (JSONException e){

            }
        }

        @Override
        public void onError(UiError uiError) {
            Log.d("loginError",uiError.errorMessage+uiError.errorCode+
                    uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            Log.d("cancel","cancel");
        }
    };

    private String getJson(String openID) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",openID);
            jsonObject.put("password",openID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("getJson",jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //popupWindow();
        //testLayoutSize();
        //popupWindow3();
    }

    public int pxToDip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", grantResults.length + "onRequestPermissionsResult" + grantResults[0]);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //setText();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
    }

    private void setText() {
        Log.d("mainActivity", "granted1");
        User user =new User();
        String result = lr.login("cici", "199208044038", user);
        text_result.setText(result + ",正确为：" + user.getUsername() + ":" + user.getPassword());
    }
}
