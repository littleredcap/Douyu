package liang.zhou.lane8.no5.my_business;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import java.math.BigDecimal;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_business.login_register.DefaultL_R;
import liang.zhou.lane8.no5.my_business.login_register.Login_Register;
import liang.zhou.lane8.no5.my_network.poster.MyPoster;

public class MainActivity extends AppCompatActivity {

    private TextView text_result;
    private Login_Register lr = new DefaultL_R();
    private PopupWindow popupWindow;
    private int viewHeight=800;
    private int viewWidth=800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_result = findViewById(R.id.text_result);


        popupDialog();
        //popupWindow();
        //testLayoutSize();

    }

    private void popupWindow3() {
        View contentView= LayoutInflater.from(this).inflate(R.layout.login_reg_dialog,null);


        popupWindow=new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);

        TextView tv_login=contentView.findViewById(R.id.tv_login);

        tv_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //setText();
                return false;
            }
        });
    }


    private void testLayoutSize(){
        final View contentView=LayoutInflater.from(this).inflate(R.layout.login_register_dialog, (ViewGroup) getWindow().getDecorView());
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
        View contentView= LayoutInflater.from(this).inflate(R.layout.login_register_dialog,null);

        //ViewHelper.setScaleX(contentView,0.78f);
        ViewHelper.setScaleY(contentView,0.6f);
        popupWindow=new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);

    }

    private void popupWindow(){
        ViewGroup contentView= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.login_register_dialog, null);
        LinearLayout ll_root=contentView.findViewById(R.id.ll_root);
        ll_root.setPadding(getAfterScaleWidth(ll_root.getPaddingLeft(),viewWidth),
                getAfterScaleHeight(ll_root.getPaddingTop(),viewHeight),
                getAfterScaleWidth(ll_root.getPaddingRight(),viewWidth),
                getAfterScaleHeight(ll_root.getPaddingBottom(),viewHeight));

        ImageView imageView=contentView.findViewById(R.id.iv_right_corner_icon);
        imageView.getLayoutParams().height=getAfterScaleHeight(imageView.getHeight(),viewHeight);
        imageView.getLayoutParams().width=getAfterScaleWidth(imageView.getWidth(),viewWidth);

        LinearLayout ll_below=findViewById(R.id.ll_below);



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
        View dialogContentView=LayoutInflater.from(this).inflate(R.layout.login_register_dialog,null);
        AlertDialog dialog=new AlertDialog.Builder(this).setView(dialogContentView).show();
        TextView login_tv=dialogContentView.findViewById(R.id.tv_login);
        login_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.height=500;
        params.width=300;
        dialog.getWindow().setAttributes(params);*/
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

    private void setText() {
        Log.d("mainActivity", "granted1");
        User user =new User();
        String result = lr.login("cici", "199208044038", user);
        text_result.setText(result + ",正确为：" + user.getUsername() + ":" + user.getPassword());
    }
}
