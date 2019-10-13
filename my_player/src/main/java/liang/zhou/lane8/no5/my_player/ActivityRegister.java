package liang.zhou.lane8.no5.my_player;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Response;

public class ActivityRegister extends AppCompatActivity {

    private EditText username_et,password_et;
    private TextView register_tv;
    private MyApplication app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        app= (MyApplication) getApplication();
        username_et=findViewById(R.id.register_by_username);
        password_et=findViewById(R.id.register_password);
        register_tv=findViewById(R.id.register_reg);
        register_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String username=username_et.getText().toString().trim();
                String password=password_et.getText().toString().trim();
                OKHttpUtil.uploadJSONs(Constant.HOST + "RegisterServlet", getJson(username,password),
                        new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            toUser(app.currentUser,jsonObject);
                            if(app.currentUser.getUserId()>0){
                                Intent intent=new Intent(ActivityRegister.this,
                                        ActivityHomePage.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }
        });
    }

    private void toUser(User user, JSONObject resultJSON){
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

    private String getJson(String username, String password) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
