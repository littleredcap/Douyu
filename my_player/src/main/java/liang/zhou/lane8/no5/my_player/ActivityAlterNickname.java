package liang.zhou.lane8.no5.my_player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.tools.gui.SmoothScroller;
import com.mob.wrappers.SMSSDKWrapper;

import java.lang.ref.WeakReference;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;
import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;


public class ActivityAlterNickname extends AppCompatActivity {

    private Toolbar toolbar;
    private Intent intent;
    private String nickname = "";
    private TextView nickname_tv, get_code_tv,finish_tv;
    private EventHandler eventHandler;
    private MyHandler myHandler;
    private EditText new_nickname_et;
    private MyApplication app;
    private EditText verify_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_alter_nickname);
        app= (MyApplication) getApplication();
        toolbar = findViewById(R.id.activity_alter_nickname_toolBar);
        toolbar.setTitle("修改昵称");
        toolbar.setNavigationIcon(getDrawable(R.drawable.video_view_top_bar_back_24dp));
        setSupportActionBar(toolbar);
        initSMSSDK();
        intent = getIntent();
        if (intent != null) {
            nickname = intent.getExtras().getString("昵称");
        }
        nickname_tv = findViewById(R.id.alter_nickname_username);
        nickname_tv.setText(nickname);
        get_code_tv = findViewById(R.id.personal_info_nickname_get_code);
        get_code_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (TextUtils.isEmpty(new_nickname_et.getText().toString().trim())) {
                    Toast.makeText(ActivityAlterNickname.this,
                            "名字不能为空",Toast.LENGTH_LONG).show();
                    return false;
                }
                if(app.currentUser!=null){
                    if(app.currentUser.getUsername().equals(new_nickname_et.getText().toString().trim())){
                        Toast.makeText(ActivityAlterNickname.this,
                                "不能和当前名字相同", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if(new_nickname_et.getText().toString().trim().length()<5||
                        new_nickname_et.getText().toString().trim().length()>30){
                    Toast.makeText(ActivityAlterNickname.this,"名字太长了",Toast.LENGTH_LONG).show();
                    return false;
                }
                SMSSDK.getVerificationCode("86",app.currentUser.getMobile());
                return false;
            }
        });
        myHandler = new MyHandler(this);
        new_nickname_et=findViewById(R.id.personal_info_alter_nickname_new_nickname_et);
        finish_tv=findViewById(R.id.personal_info_alter_nickname_finish_tv);
        verify_code=findViewById(R.id.personal_info_alter_nickname_verify_code);
        finish_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SMSSDK.submitVerificationCode("86",app.currentUser.getMobile(),
                        verify_code.getText().toString());
                return false;
            }
        });
    }

    private void initSMSSDK() {
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int i, int i1, Object o) {
                super.afterEvent(i, i1, o);
                Message message = new Message();
                message.arg1 = i;
                message.arg2 = i1;
                message.obj = o;
                myHandler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    private final class MyHandler extends Handler {
        private WeakReference<Context> weak_c;

        public MyHandler(Context c) {
            weak_c = new WeakReference<>(c);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int result = msg.arg2;
            int event = msg.arg1;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                // 处理你自己的逻辑

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Log.d("handleVerifyMessage",event+"");
                    OKHttpUtil.uploadJson(Constant.HOST+"UpdatePersonalInfoServlet",
                            app.currentUser.getUserId(),
                            "username",new_nickname_et.getText().toString(),null);
                    //提交验证码成功
                    //客户端验证成功，可以进行注册,返回校验的手机和国家代码phone/country
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    Toast.makeText(weak_c.get(), "获取验证码成功", Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                Toast.makeText(weak_c.get(), "验证失败", Toast.LENGTH_SHORT).show();
                ((Throwable) data).printStackTrace();
                Log.d("handleMessage",((Throwable) data).getMessage());
            }
        }

    }
}
