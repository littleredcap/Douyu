package liang.zhou.lane8.no5.my_player;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Response;

public class ActivityMailBinding extends AppCompatActivity implements ServerResponse{

    private Toolbar toolbar;
    private EditText et_mail_address,et_verify_code;
    private TextView tv_get_verify_code;
    private String mail_regex="^[^@].*[@+].*$";
    private String mail_box_regex="^[a-zA-Z0-9]+([.-_][A-Za-z0-9]+)*@([A-Za-z0-9]+[-.])+[a-zA-Z0-9]{2,4}$";
    private Drawable drawable_get_verify_code;
    private String verify_code="";
    private int count_down=60;
    private TextView tv_confirm_bind;
    private String mailBox_return;
    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_binding);

        myApplication= (MyApplication) getApplication();
        init_toolbar();
        et_mail_address=findViewById(R.id.activity_mail_binding_mail_box);
        et_verify_code=findViewById(R.id.activity_mail_binding_verify_code);
        tv_confirm_bind=findViewById(R.id.activity_mail_binding_confirm_bind);
        tv_confirm_bind.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(et_verify_code.getText().toString().trim().equals(verify_code)){
                    OKHttpUtil.uploadJson(Constant.HOST+"UpdatePersonalInfoServlet",
                            myApplication.currentUser.getUserId(),
                            "mailBox",mailBox_return,null);
                }else{
                    Toast.makeText(ActivityMailBinding.this,"验证码错误",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        et_mail_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged",s.toString()+","+start+","+before);
                is_get_code_tv_enable(s,start);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged",s.toString());
            }
        });

        tv_get_verify_code=findViewById(R.id.activity_mail_binding_get_verify_code);
        tv_get_verify_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String mail_box_address=et_mail_address.getText().toString();
                if(verify_mail_box_isAvailable(mail_box_address)){
                    String verify_code=get_verify_code();
                    sendEmail(verify_code,mail_box_address);
                    Toast.makeText(ActivityMailBinding.this,"验证码已发送",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        drawable_get_verify_code=tv_get_verify_code.getBackground();
    }

    private boolean verify_mail_box_isAvailable(String mail_box_address) {
        return mail_box_address.matches(mail_box_regex);
    }

    private void is_get_code_tv_enable(CharSequence c,int start) {
        String s=c.toString();
        if(s.matches(mail_regex)){
            Log.d("is_get_code_tv_enable","true");
            tv_get_verify_code.setEnabled(true);
            drawable_get_verify_code.setTint(0xffFF6500);
        }else{
            tv_get_verify_code.setEnabled(false);
            drawable_get_verify_code.setTint(0xffaaaaaa);
        }
    }

    private String get_verify_code() {
        return (int)((Math.random()*9+1)*1000)+"";
        //未完、待续
    }

    private void init_toolbar(){
        toolbar=findViewById(R.id.activity_mail_binding_toolbar);
        toolbar.setTitle("邮箱绑定");
        Drawable drawableBack=getDrawable(R.drawable.video_view_top_bar_back_24dp);
        drawableBack.setTint(Color.GRAY);
        toolbar.setNavigationIcon(drawableBack);
        setSupportActionBar(toolbar);
    }

    private final String MAIL_SMTP_HOST = "smtp.qq.com";
    private final String MAIL_SMTP_PORT = "587";
    private final String MAIL_SMTP_AUTH = "true";
    private final String MAIL_SMTP_STARTTLS_ENABLE = "true";
    private final String MAIL_FROM_PWD="mtaixlacgyqebjjg";
    private final String IMAP="knlcqeyuivubbibd";
    private void send(final String mail_content, final String to_whom){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Properties properties=new Properties();
                properties.put("mail.smtp.host",MAIL_SMTP_HOST);
                properties.put("mail.smtp.port",MAIL_SMTP_PORT);
                properties.put("mail.smtp.auth",MAIL_SMTP_AUTH);
                properties.put("mail.smtp.starttls.enable",MAIL_SMTP_STARTTLS_ENABLE);
                try {
                    Authenticator auth = new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("410283413@qq.com",MAIL_FROM_PWD);
                        }
                    };
                    Session session = Session.getInstance(properties, auth);
                    MimeMessage message = new MimeMessage(session);
                    Address addressFrom = new InternetAddress("410283413@qq.com", "C č");
                    Address addressTo = new InternetAddress(to_whom, "");
                    message.setSubject("验证码");
                    message.setText(mail_content);
                    message.setFrom(addressFrom);
                    message.addRecipient(Message.RecipientType.TO, addressTo);
                    message.saveChanges();
                    Transport transport = session.getTransport("smtp");
                    transport.connect(MAIL_SMTP_HOST, "410283413@qq.com", MAIL_FROM_PWD);
                    transport.send(message);
                    transport.close();
                    Log.d("sendEmail","sendEmail");
                }catch (Exception e){

                }
            }
        }).start();
    }
    private void sendEmail(final String mail_content, final String to_whom){
        count_down=60;
        OKHttpUtil.uploadJson(Constant.HOST+"SendEmailServlet",
                myApplication.currentUser.getUserId(),
                "mailBox",to_whom,this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(count_down>0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_get_verify_code.setEnabled(false);
                            drawable_get_verify_code.setTint(0xffaaaaaa);
                            tv_get_verify_code.setText(count_down+"s后重发");
                        }
                    });
                    count_down--;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_get_verify_code.setEnabled(true);
                        drawable_get_verify_code.setTint(0xffFF6500);
                        tv_get_verify_code.setText("获取验证邮件");
                    }
                });
            }
        }).start();
    }

    @Override
    public void response(Call call,Response response) {
        try {
            Log.d("mailResponse","mailResponse");
            parsonJson(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parsonJson(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            verify_code= jsonObject.getString("verify_code");
            mailBox_return=jsonObject.getString("mailBox");
            Log.d("mailBinding",result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
