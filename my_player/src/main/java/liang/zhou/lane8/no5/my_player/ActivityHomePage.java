package liang.zhou.lane8.no5.my_player;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentDiscovery;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentFollow;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentForum;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentRecommend;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentRecreation;
import liang.zhou.lane8.no5.my_player.my_view_pager.MyPagerAdapter;
import liang.zhou.lane8.no5.my_player.ui.MyCircleImageView;

public class ActivityHomePage extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private MyCircleImageView portrait;
    private FrameLayout frameLayout;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private MyApplication app;
    private RequestOptions options;
    private final int LOGIN_REGISTER=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getApplication();
        Log.d("onHomePageCreate","user"+app.currentUser.getUserId());
        nullOrNot();
    }
    private void nullOrNot(){
        if(app.currentUser.getUserId()<=0){
            Intent intent=new Intent(this, ActivityShowDialog.class);
            startActivityForResult(intent,LOGIN_REGISTER);
        }else {
            initUI();
        }
    }

    private void initUI(){
        setContentView(R.layout.home_page);
        options = RequestOptions.placeholderOf(R.mipmap.yukee);
        portrait = findViewById(R.id.home_page_top_portrait);
        Glide.with(this).load(app.currentUser.getPortrait()).apply(options).into(portrait);
        portrait.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent dest = new Intent(ActivityHomePage.this, ActivityInfo.class);
                startActivity(dest);
                return false;
            }
        });
        initViewPager();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("onNewIntent","user"+app.currentUser.getUserId());
        nullOrNot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(portrait!=null){
            Glide.with(this).load(app.currentUser.getPortrait()).into(portrait);
        }
    }

    private void initViewPager() {
        frameLayout=findViewById(R.id.home_page_frame);
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        transaction.add(R.id.home_page_frame,new FragmentRecommend());
        transaction.add(R.id.home_page_frame,new FragmentRecreation());
        transaction.add(R.id.home_page_frame,new FragmentFollow());
        transaction.add(R.id.home_page_frame,new FragmentForum());
        transaction.add(R.id.home_page_frame,new FragmentDiscovery());
        transaction.commit();
        //initFragments();
        //viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragments,null));
    }

    private void initFragments() {
        fragments=new ArrayList<>();
        fragments.add(new FragmentRecommend());
        fragments.add(new FragmentRecreation());
        fragments.add(new FragmentFollow());
        fragments.add(new FragmentForum());
        fragments.add(new FragmentDiscovery());
    }
}
