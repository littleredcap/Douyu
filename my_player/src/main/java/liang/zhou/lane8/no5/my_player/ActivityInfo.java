package liang.zhou.lane8.no5.my_player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.jeanboy.cropview.cropper.CropperHandler;
import com.jeanboy.cropview.cropper.CropperManager;
import com.jeanboy.cropview.cropper.CropperParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.DividerItemDecoration;
import okhttp3.Call;
import okhttp3.Response;

public class ActivityInfo extends AppCompatActivity implements CropperHandler {

    private RecyclerView recyclerView;
    private InfoRecyclerViewAdapter recycler_adapter;
    private MyApplication ma;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ma = (MyApplication) getApplication();
        setContentView(R.layout.activity_personal_info);
        Toolbar toolbar = findViewById(R.id.activity_personal_info_toolBar);
        toolbar.setTitle("个人资料");
        Drawable drawableBack = getDrawable(R.drawable.video_view_top_bar_back_24dp);
        drawableBack.setTint(Color.GRAY);
        toolbar.setNavigationIcon(drawableBack);
        setSupportActionBar(toolbar);
        CropperManager.getInstance().build(this);
        initInfo();
        parseJson(returnResult.toString());
        /*Log.d("InfoOnCreate",returnResult.toString());
        Log.d("InfoOnCreate",items.size()+"");
        Log.d("InfoOnCreate",items.get(1).value.toString());*/
        //Log.d("InfoOnCreate",items.get(0).key);
        //Log.d("InfoOnCreate",items.get(0).value.toString());
        initRecyclerView();
    }

    private void initRecyclerView() {
        recycler_adapter = new InfoRecyclerViewAdapter(items);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.activity_personal_info_recycler_view);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(recycler_adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getBundleExtra("addressBundle");
        items.get(recycler_adapter.getCurrentItemPosition()).value = bundle.getString("address");
        OKHttpUtil.uploadJson(Constant.HOST + "UpdatePersonalInfoServlet", ma.currentUser.getUserId(),
                "homeAddress",
                items.get(recycler_adapter.getCurrentItemPosition()).value.toString(), null);
        recycler_adapter.notifyItemChanged(recycler_adapter.getCurrentItemPosition());
        Log.d("onNewIntent", "onNewIntent");
    }

    private ArrayList<ItemsInInfo> items = new ArrayList<>();
    JSONObject returnResult = new JSONObject();



    private void initInfo() {
        try {
            /*Bitmap bitmap = BitmapFactory.decodeFile(Environment.
                    getExternalStorageDirectory() + "/tieba/portrait.jpg");
            Bitmap b=BitmapFactory.decodeFile(ma.currentUser.getPortrait());*/
            returnResult.put("头像", ma.currentUser.getPortrait());
            returnResult.put("昵称", ma.currentUser.getUsername());
            returnResult.put("性别", ma.currentUser.getSex());
            returnResult.put("生日", ma.currentUser.getBirthday());
            returnResult.put("所在地", ma.currentUser.getHomeAddress());
            returnResult.put("常出没地", extractTheFirst(ma.currentUser.getAlwaysAppearance()));
            Log.d("initInfo", extractTheFirst(ma.currentUser.getAlwaysAppearance()));
            returnResult.put("个性签名", "请填写个性签名");
            returnResult.put("实名认证", "未认证");
            returnResult.put("密码", "修改");
            returnResult.put("邮箱", "立即绑定");
            returnResult.put("手机", "****7690");
            returnResult.put("QQ", "立即绑定");
            returnResult.put("经验值", "11.00/40");
            returnResult.put("鱼丸", "10");
            returnResult.put("鱼翅", "10");
            returnResult.get("头像");

        } catch (JSONException e) {

        }
    }

    private String extractTheFirst(String alwaysAppearance) {
        String s[] = alwaysAppearance.split(",");
        return s[0];
    }

    private String[] specialColorKey = {"昵称", "所在地", "常出没地",
            "个性签名", "邮箱", "手机", "QQ", "经验值", "鱼丸", "鱼翅", "密码"};

    private void parseJson(String results) {
        try {
            Iterator<String> keys = returnResult.keys();
            while (keys.hasNext()) {
                ItemsInInfo itemInfo = new ItemsInInfo();
                itemInfo.key = keys.next();
                //Log.d("initInfo",returnResult.get("头像").getClass().getName());
                itemInfo.value = returnResult.get(itemInfo.key);
                for (int i = 0; i < specialColorKey.length; i++) {
                    if (itemInfo.key.equals(specialColorKey[i])) {
                        itemInfo.valueColor = Color.argb(255, 255, 101, 0);
                        break;
                    }
                    itemInfo.valueColor = Color.GRAY;
                }
                items.add(itemInfo);
            }
        } catch (JSONException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).key.equals("常出没地")) {
                items.get(i).value = extractTheFirst(ma.currentUser.getAlwaysAppearance());
                recycler_adapter.notifyDataSetChanged();
            }
        }

        Log.d("onActivityInfoResume", ma.currentUser.getAlwaysAppearance() + "你好");
    }

    private final int select_photo = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*Uri uri = Uri.parse(data.getData().getPath().replace("/raw", ""));
        int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        getContentResolver().takePersistableUriPermission(uri,takeFlags);
        data.setData(uri);*/
        CropperManager.getInstance().handlerResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            Log.d("onActivityResult", data.getData().getPath());
            //CropperManager.getInstance().handlerResult(requestCode, resultCode, data);
            //recycler_adapter.setImageUri(data.getData().getPath().replace("/raw",""));
            //OKHttpUtil.uploadImage(data.getData().getPath().replace("/raw",""));
            //Uri source=Uri.parse(data.getData().getPath().replace("/raw",""));
            //final Uri resultUri = UCrop.getOutput(data);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestPermission", requestCode + "");
                CropperManager.getInstance().pickFromCamera();
            }
        }else if(requestCode==4){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestPermission", requestCode + "");
                CropperManager.getInstance().pickFromGallery();
            }
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public CropperParams getParams() {
        return new CropperParams(1, 1);
    }

    @Override
    public void onCropped(Uri uri) {
        Log.d("=====onCropped======", "======裁切成功=======" + uri);
        //recycler_adapter.setImageUri(uri.getPath());
        OKHttpUtil.uploadImage(ma.currentUser.getUserId(), uri.getPath(), new ServerResponse() {
            @Override
            public void response(Call call, Response response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json=new JSONObject(response.body().string());
                                String portraitUrl=json.getString("portrait");
                                Glide.with(ActivityInfo.this).load(portraitUrl).
                                        into(recycler_adapter.getPortraitImageView());
                                ma.currentUser.setPortrait(portraitUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e){

                            }
                        }
                    });
            }
        });
    }

    @Override
    public void onCropCancel() {
        Log.d("=====onCropCancel====", "======裁切取消=====");
    }

    @Override
    public void onCropFailed(String msg) {
        Log.d("=====onCropFailed===", "=======裁切失败======" + msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CropperManager.getInstance().destroy();
    }
}
