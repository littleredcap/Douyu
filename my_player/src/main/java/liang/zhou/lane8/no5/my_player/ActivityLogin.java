package liang.zhou.lane8.no5.my_player;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import liang.zhou.lane8.no5.my_business.login_register.DefaultL_R;
import liang.zhou.lane8.no5.my_business.login_register.Login_Register;

public class ActivityLogin extends AppCompatActivity {

    private MyApplication app;
    private EditText tv_mobile_number;
    private EditText tv_account_password;
    private TextView tv_login;
    private Login_Register lr ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getApplication();
        setContentView(R.layout.login_by_mobile);
        tv_mobile_number=findViewById(R.id.login_by_mobile_number);
        tv_account_password=findViewById(R.id.login_password);
        tv_login=findViewById(R.id.button_login_in_by_mobile);
        lr= new DefaultL_R();
        tv_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String mobile_number=tv_mobile_number.getText().toString().trim();
                String password=tv_account_password.getText().toString().trim();
                String result=lr.login(mobile_number,password,app.currentUser);
                Toast.makeText(ActivityLogin.this,result,Toast.LENGTH_LONG).show();
                if(app.currentUser.getUserId()>0){
                    Intent intent=new Intent(ActivityLogin.this,ActivityHomePage.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
