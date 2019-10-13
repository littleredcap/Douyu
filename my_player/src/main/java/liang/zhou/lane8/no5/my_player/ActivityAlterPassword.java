package liang.zhou.lane8.no5.my_player;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.sdk.GT3ConfigBean;
import com.geetest.sdk.GT3ErrorBean;
import com.geetest.sdk.GT3GeetestUtils;
import com.geetest.sdk.GT3Listener;
import com.geetest.sdk.views.GT3GeetestButton;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaConfiguration;
import com.netease.nis.captcha.CaptchaListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Response;

public class ActivityAlterPassword extends AppCompatActivity{

    private GT3GeetestButton button;
    private Toolbar toolbar;
    private MyApplication ma;
    private TextView tv_mobile,tv_request_sms,tv_validate;
    private boolean is_validate_success=false;
    private EventHandler eventHandler;
    private MyHandler myHandler;
    private TextView tv_confirm;
    private EditText et_verify_code;
    private TextView tv_mobile_label;
    private LinearLayout ll,pw_ll;
    private RelativeLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_password);
        ma= (MyApplication) getApplication();
        tv_mobile=findViewById(R.id.activity_alter_password_mobile_my_tv);
        tv_request_sms=findViewById(R.id.activity_alter_password_request_sms);
        tv_validate=findViewById(R.id.validate_tv);
        tv_mobile_label=findViewById(R.id.activity_alter_password_label);
        ll=findViewById(R.id.linearLayout2);
        frameLayout=findViewById(R.id.frameLayout);
        tv_first_pw=findViewById(R.id.activity_alter_password_first_pw);
        tv_second_pw=findViewById(R.id.activity_alter_password_second_pw);
        tv_pw_confirm=findViewById(R.id.activity_alter_password_password_confirm);
        pw_ll=findViewById(R.id.activity_alter_password_alter_ll);
        tv_pw_confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("tv_pw_confirm","onConfirmClick");
                if(tv_first_pw.getText().toString().trim().length()>6&&
                        tv_first_pw.getText().toString().trim().length()<25&&
                        tv_first_pw.getText().toString().equals(tv_second_pw.getText().toString())){
                    OKHttpUtil.uploadJson(Constant.HOST + "UpdatePersonalInfoServlet",
                            ma.currentUser.getUserId(),
                            "password", tv_first_pw.getText().toString().trim(), new ServerResponse() {
                                @Override
                                public void response(Call call, Response response) {
                                    try {
                                        Log.d("tv_pw_confirm",response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
                return false;
            }
        });
        tv_request_sms.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                requestSMS();
                return false;
            }
        });
        tv_confirm=findViewById(R.id.activity_alter_password_confirm);
        tv_confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String verify_code=et_verify_code.getText().toString();
                SMSSDK.submitVerificationCode("86",ma.currentUser.getMobile(),verify_code);
                return false;
            }
        });
        et_verify_code=findViewById(R.id.activity_alter_password_verify_code);
        //initVerify();
        myHandler=new MyHandler(this);
        initToolbar();
        tv_mobile.setText(ma.currentUser.getMobile());
        init();
    }

    private void initToolbar() {
        toolbar=findViewById(R.id.activity_alter_password_toolbar);
        toolbar.setTitle("个人资料");
        Drawable drawableBack=getDrawable(R.drawable.video_view_top_bar_back_24dp);
        drawableBack.setTint(Color.GRAY);
        toolbar.setTitle("修改密码");
        toolbar.setNavigationIcon(drawableBack);
        setSupportActionBar(toolbar);
    }

    private void init() {
        final CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
                .captchaId("2327b70a8fba49109fca666565075bec")
                .listener(new CaptchaListener() {
                    @Override
                    public void onReady() {

                    }

                    @Override
                    public void onValidate(String s, String s1, String s2) {
                        Log.d("onValidate",s+","+s2);
                        if(s.equals("true")){
                            Log.d("onValidate",s+","+s2);
                            is_validate_success=true;
                        }
                    }

                    @Override
                    public void onError(String s) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onClose() {

                    }
                })
                .languageType(CaptchaConfiguration.LangType.LANG_ZH_CN)
                .debug(true)
                .position(-1, -1, 0, 0)
                .controlBarImageUrl("https://www.baidu.com/img/bd_logo1.png",
                        "https://www.baidu.com/img/bd_logo1.png",
                        "https://www.baidu.com/img/bd_logo1.png")
                .backgroundDimAmount(0.5f)
                .build(this);
        final Captcha captcha = Captcha.getInstance().init(configuration);
        captcha.validate();
    }

    private void initVerify() {
        final GT3GeetestUtils gt3GeetestUtils =new GT3GeetestUtils(this);
        GT3ConfigBean gt3ConfigBean=new GT3ConfigBean();
        gt3ConfigBean.setPattern(1);
        gt3ConfigBean.setCanceledOnTouchOutside(true);
        gt3ConfigBean.setDebug(false);
        gt3ConfigBean.setLang(null);
        gt3ConfigBean.setTimeout(10000);
        gt3ConfigBean.setWebviewTimeout(10000);


        gt3ConfigBean.setListener(new GT3Listener() {
            @Override
            public void onStatistics(String s) {
            }

            @Override
            public void onClosed(int i) {

            }

            @Override
            public void onSuccess(String s) {
                gt3GeetestUtils.showSuccessDialog();
            }

            @Override
            public void onFailed(GT3ErrorBean gt3ErrorBean) {

            }

            @Override
            public void onButtonClick() {
                Log.d("onButtonClick", "GT3BaseListener-->onButtonClick-->" );

            }

            @Override
            public void onDialogResult(String s) {
                Log.d("onDialogResult", "GT3BaseListener-->onDialogResult-->" + s);
            }
        });

        gt3ConfigBean.setApi1Json(initJsonObject());
        gt3GeetestUtils.init(gt3ConfigBean);
        button.setGeetestUtils(gt3GeetestUtils);

        //gt3GeetestUtils.startCustomFlow();
    }

    private JSONObject initJsonObject() {
        JSONObject json=null;
        String s="{\"success\":1," +
                "\"challenge\":\"4a5cef77243baa51b2090f7258bf1368\"," +
                "\"gt\":\"019924a82c70bb123aae90d483087f94\"}";
        try {
            json=new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void requestSMS(){
        Log.d("requestSMS",is_validate_success+"");
        if(is_validate_success){
            Log.d("requestSMS",ma.currentUser.getMobile());
            initSMSSDK();
            SMSSDK.getVerificationCode("86",ma.currentUser.getMobile());
        }
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
                    firstGone();
                    secondIn();
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

    private TextView tv_first_pw,tv_second_pw,tv_pw_confirm;
    private void secondIn() {
        tv_validate.setText("输入新密码");
        tv_pw_confirm.setVisibility(View.VISIBLE);
        pw_ll.setVisibility(View.VISIBLE);
    }

    private void firstGone() {

        tv_mobile.setVisibility(View.GONE);
        tv_mobile_label.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        tv_confirm.setVisibility(View.GONE);
    }
}
