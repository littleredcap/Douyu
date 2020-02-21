package liang.zhou.lane8.no5.my_player;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.business_utils.JSONUtil;
import liang.zhou.lane8.no5.my_player.common_utils.StringUtils;
import liang.zhou.lane8.no5.my_player.network_model.DataArrivedListener;
import liang.zhou.lane8.no5.my_player.network_model.NetworkModelContext;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.Utils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AlwaysAppearance extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_add_location;
    private FlexboxLayout flexboxLayout;
    private RelativeLayout rl_container,rl_activity_always_appeared_address;
    private TextView tv_add;
    private MyApplication ma;
    private NetworkModelContext networkModelContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma= (MyApplication) getApplication();
        networkModelContext=new NetworkModelContext();
        networkModelContext.useRxJava();
        setContentView(R.layout.activity_always_appeared_address);
        flexboxLayout=findViewById(R.id.activity_always_appeared_address_flexBox);
        rl_container=findViewById(R.id.activity_always_appeared_address_label_container);
        rl_activity_always_appeared_address=findViewById(R.id.activity_always_appeared_address_rl);
        tv_add=findViewById(R.id.activity_always_appeared_address_add);
        String al[]=ma.currentUser.getAlwaysAppearance().split("_");
        for(int i=0;i<al.length;i++){
            if(!al[i].equals("")) {
                createTextView(null, al[i]);
            }
        }
        if(flexboxLayout.getChildCount()>0){
            rl_container.setVisibility(View.GONE);
            rl_activity_always_appeared_address.setVisibility(View.VISIBLE);
        }
        tv_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog();
                return false;
            }
        });
        initBackIv();
        tv_add_location=findViewById(R.id.activity_always_appeared_address_addLocation);
        tv_add_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog();
                return false;
            }
        });
        //initToolbar();
    }

    private void showDialog() {
        if(flexboxLayout.getChildCount()<5) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //final Dialog dialog=new Dialog(this);
            final AlertDialog dialog = builder.create();
            final ViewGroup dialogView = (ViewGroup) LayoutInflater.from(this).
                    inflate(R.layout.activity_always_appeared_address_dialog, null);
            final EditText editText = dialogView.findViewById(R.id.activity_always_appeared_address_dialog_edit);
            dialog.setView(dialogView);
            dialog.show();
            TextView tv_cancel = dialogView.findViewById(R.id.activity_always_appeared_address_dialog_cancel);
            tv_cancel.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dialog.dismiss();
                    return false;
                }
            });
            TextView tv_confirm = dialogView.findViewById(R.id.activity_always_appeared_address_dialog__confirm);
            tv_confirm.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    String text = editText.getText().toString();
                    if (text != null && !text.equals("")) {
                        createTextView(dialogView, text);
                        StringBuffer s=new StringBuffer();
                        collectAllAppearance(s);
                        /*String data="userId,"+Global.myself.getUserId()+";alwaysAppearance,"+
                                s.toString()+";updatedColumn,"+
                                StringUtils.upperCaseFirstChar("alwaysAppearance");
                        networkModelContext.updatePersonalInfo(new DataArrivedListener() {
                            @Override
                            public void dataArrived(List data_collection) {

                            }

                            @Override
                            public void jsonArrived(JSONObject response) {
                                Log.d("jsonArrived",response.toString());
                                //ma.updateUser(response.toString());
                            }
                        },JSONUtil.toJson(data).toString());*/
                        OKHttpUtil.uploadJson(Constant.HOST + "UpdatePersonalInfoServlet",
                                ma.currentUser.getUserId(),
                                "alwaysAppearance", s.toString(), new ServerResponse() {
                                    @Override
                                    public void response(Call call, Response response) {
                                        try {
                                            ma.updateUser(response.body().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        dialog.dismiss();
                    }
                    return true;
                }
            });
        }
    }

    private void collectAllAppearance(StringBuffer s){
        for(int i=0;i<flexboxLayout.getChildCount();i++){
            View view=flexboxLayout.getChildAt(i);
            if(view instanceof TextView) {
                TextView t= (TextView) view;
                s.append(t.getText());
                if (i != flexboxLayout.getChildCount() - 1) {
                    s.append("_");
                }
            }
        }
    }

    private void createTextView(ViewGroup dialogView,String text) {
        TextView label=new TextView(this);
        label.setText(text);
        label.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("onLongClick",v+"");

                flexboxLayout.removeView(v);
                StringBuffer s=new StringBuffer();
                collectAllAppearance(s);
                OKHttpUtil.uploadJson(Constant.HOST + "UpdatePersonalInfoServlet",
                        ma.currentUser.getUserId(),
                        "alwaysAppearance", s.toString(), new ServerResponse() {
                            @Override
                            public void response(Call call, Response response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response.body().string());
                                    ma.currentUser.setAlwaysAppearance(jsonObject.getString("alwaysAppearance"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e){

                                }
                            }
                        });
                if(flexboxLayout.getChildCount()==0){
                    rl_container.setVisibility(View.VISIBLE);
                    rl_activity_always_appeared_address.setVisibility(View.GONE);
                }
                return true;
            }
        });
        label.setBackgroundResource(R.drawable.alter_nickname_edit_text_round_corner_bg);
        label.setPadding(Utils.dp2px(this,10),Utils.dp2px(this,10),
                Utils.dp2px(this,10),Utils.dp2px(this,10));
        FlexboxLayout.LayoutParams vlp=new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        vlp.setMargins(Utils.dp2px(this,15),Utils.dp2px(this,15),
                Utils.dp2px(this,10),Utils.dp2px(this,10));
        label.setLayoutParams(vlp);
        flexboxLayout.addView(label);
        //flexboxLayout
        rl_container.setVisibility(View.GONE);
        rl_activity_always_appeared_address.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        //toolbar=findViewById(R.id.activity_always_appeared_address_toolBar);
        Drawable drawable_back=getDrawable(R.drawable.video_view_top_bar_back_24dp);
        drawable_back.setTint(Color.GRAY);
        toolbar.setNavigationIcon(drawable_back);
        TextView tv=new TextView(this);
        tv.setText("添加");
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //rl.setMarginStart(200);
        tv.setLayoutParams(rl);
        toolbar.addView(tv);
        toolbar.setTitle("常出没地点");
    }

    private void initBackIv() {
        ImageView iv_back=findViewById(R.id.activity_always_appeared_address_back_iv);
        Drawable drawable_back=getDrawable(R.drawable.video_view_top_bar_back_24dp);
        drawable_back.setTint(Color.GRAY);
        iv_back.setImageDrawable(drawable_back);
    }
}
