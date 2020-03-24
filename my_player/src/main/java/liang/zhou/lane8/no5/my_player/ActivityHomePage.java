package liang.zhou.lane8.no5.my_player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.animation.AnimationUtils;
import liang.zhou.lane8.no5.my_player.animation.BackgroundTintWrapper;
import liang.zhou.lane8.no5.my_player.common.ProgressListener;
import liang.zhou.lane8.no5.my_player.glide.GlideApp;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentDiscovery;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentFollow;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentForum;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentRecommend;
import liang.zhou.lane8.no5.my_player.home_pager_fragment.FragmentRecreation;
import liang.zhou.lane8.no5.my_player.okhttp.ProgressInterceptor;
import liang.zhou.lane8.no5.my_player.screen_adapt.AutoSize;
import liang.zhou.lane8.no5.my_player.screen_adapt.ResourcesWrapper;
import liang.zhou.lane8.no5.my_player.ui.JellyButton;
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
    private FloatingActionButton fab,fabHelper;
    private Button testBtn,test2Btn;
    private JellyButton jellyButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MyApplication) getApplication();
        Log.d("onHomePageCreate","user"+Global.myself.getUserId());
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        x[1]=jellyButton.getCircleX();
        y[1]=jellyButton.getCircleY();
        reverseX[1]=fabHelper.getX();
        reverseY[1]=fabHelper.getY();
    }
    float x[]=new float[2];
    float y[]=new float[2];
    float reverseX[]=new float[2];
    float reverseY[]=new float[2];

    private void initUI(){
        setContentView(R.layout.home_page);
        fab=findViewById(R.id.home_page_fab);
        fabHelper=findViewById(R.id.home_page_fab_helper);
        jellyButton=findViewById(R.id.jellyButton);
        BackgroundTintWrapper wrapper=new BackgroundTintWrapper(fab);
        BackgroundTintWrapper wrapper2=new BackgroundTintWrapper(fabHelper);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                x[0]=v.getX();
                y[0]=v.getY();
                fabHelper.setVisibility(View.GONE);
                AnimationUtils.locationChange(v,x,y,jellyButton,false);
                //AnimationUtils.colorChange(wrapper,AnimationUtils.TRANSPARENCY,AnimationUtils.currentColor);
            }
        });
        jellyButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                jellyButton.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                reverseX[0]=fab.getX();
                reverseY[0]=fab.getY();
                AnimationUtils.locationChange(fab,reverseX,reverseY,null,true);
                return false;
            }
        });
        AnimationUtils.colorChange(wrapper, AnimationUtils.OPAQUE,getColor(R.color.fab_color_red),
                getColor(R.color.fab_color_purple),getColor(R.color.fab_color_orange),
                getColor(R.color.fab_color_blue),getColor(R.color.fab_color_white));
        AnimationUtils.colorChange(wrapper2,AnimationUtils.TRANSPARENCY,0xFF1493,0x00008B,
                0x778899, 0x00FF7F,Color.TRANSPARENT);

        options = RequestOptions.placeholderOf(R.mipmap.yukee).error(R.mipmap.yukee);
        portrait = findViewById(R.id.home_page_top_portrait);

        ProgressInterceptor.addListener(Global.myself.getPortrait(), new ProgressListener() {
            @Override
            public void onProgressUpdate(int progress) {
                Log.d("onProgressUpdate", "onProgress: " + progress);
                //progressDialog.setProgress(progress);
            }
        });

        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                //progressDialog.dismiss();
                portrait.setImageDrawable(resource);
                //Log.d(TAG, "onResourceReady: ");
                ProgressInterceptor.removeListener(Global.myself.getPortrait());
            }

            @Override
            public void onStart() {
                super.onStart();
                //Log.d(TAG, "onStart: ");
                //progressDialog.show();
            }
        };
        GlideApp.with(this)
                .load(Global.myself.getPortrait())
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存
                .skipMemoryCache(true)
                .into(portrait);

        //GlideApp.with(this).load(app.currentUser.getPortrait()).apply(options).into(portrait);
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
    public ResourcesWrapper mResources;

    @Override
    public android.content.res.Resources getResources() {
        if (mResources == null) {
            final AutoSize autoSize = getAutoSize();
            if (autoSize != null) {
                mResources = new ResourcesWrapper(super.getResources(), autoSize);
            }
        }
        return mResources != null ? mResources : super.getResources();
    }

    /**
     * 子类可重写适配
     **/
    @Nullable
    protected AutoSize getAutoSize() {
        return new AutoSize(400/*设计的宽度dp*/, true);
    }
}
