package liang.zhou.lane8.no5.my_player;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ActivityRealNameAuth extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_ali_pay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_athentification);
        Toolbar toolbar=findViewById(R.id.activity_address_city_select_toolbar);
        toolbar.setTitle("实名认证");
        Drawable drawableBack=getDrawable(R.drawable.video_view_top_bar_back_24dp);
        drawableBack.setTint(Color.GRAY);
        toolbar.setNavigationIcon(drawableBack);
        setSupportActionBar(toolbar);

        tv_ali_pay=findViewById(R.id.activity_real_name_auth_ali_tv);

    }
}
