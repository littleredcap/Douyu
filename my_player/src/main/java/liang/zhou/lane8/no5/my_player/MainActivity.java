package liang.zhou.lane8.no5.my_player;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import liang.zhou.lane8.no5.my_player.my_view_pager.FragmentAnchor;
import liang.zhou.lane8.no5.my_player.my_view_pager.FragmentAristocrat;
import liang.zhou.lane8.no5.my_player.my_view_pager.FragmentChat;
import liang.zhou.lane8.no5.my_player.my_view_pager.FragmentForum;
import liang.zhou.lane8.no5.my_player.my_view_pager.FragmentRank;
import liang.zhou.lane8.no5.my_player.my_view_pager.MyPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private PLVideoView plVideoView;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private View bottom_line_indicator;
    private TextView middle_bar_textView[];
    private LinearLayout middle_bar_ll;
    private LinearLayout aristocrat_count_ll;
    private Toast toast;
    private boolean isFirstTimeLoad = true;
    private float[] textViewDiff;
    private int currentPage;
    private int bottom_line_bar_state;
    private float last_position = 0;
    private ImageView iv_fullScreen;
    private LinearLayout ll_view_pager;
    private ImageView iv_pause;
    private boolean isPlaying = true;
    private Tencent mTencent;
    private ImageView share;
    private String roomName;
    private String coverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_main);

        hideStatusBar();
        roomName=getIntent().getStringExtra("roomName");
        coverUrl=getIntent().getStringExtra("coverUrl");
        bottom_line_indicator = findViewById(R.id.video_view_viewpager_indicator_bottom_line);
        aristocrat_count_ll = findViewById(R.id.video_view_viewpager_aristocrat_count_ll);
        iv_fullScreen = findViewById(R.id.video_view_bottom_bar_full_screen);
        ll_view_pager = findViewById(R.id.video_view_main_view_pager);
        iv_fullScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                return false;
            }
        });
        iv_pause = findViewById(R.id.video_view_bottom_bar_pause);
        iv_pause.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isPlaying) {
                    pause();
                    isPlaying = false;
                } else {
                    play();
                    isPlaying = true;
                }
                return false;
            }
        });
        initPager();

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK}, 0);
        //initPlayerSurface();
        mTencent = Tencent.createInstance("101580796", getApplicationContext());
        share=findViewById(R.id.video_view_top_share_iv);
        share.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                doShareToQzone();
                //login();
                return false;
            }
        });


    }

    private void login(){
        Log.d("login",Environment.getExternalStorageDirectory()+"/tieba/Yukee.jpg");
        mTencent.login(this, "all",loginListener);
    }

    private UserInfo userInfo=null;
    IUiListener loginListener=new IUiListener() {
        @Override
        public void onComplete(Object o) {
            Log.d("loginSuccess",o.toString());
            JSONObject json= (JSONObject) o;
            try {
                String openID = json.getString("openid");
                String accessToken = json.getString("access_token");
                String expires = json.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                userInfo=new UserInfo(getApplicationContext(),qqToken);

                userInfo.getOpenId(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.d("getUserInfo",o.toString());

                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }catch (JSONException e){

            }
        }

        @Override
        public void onError(UiError uiError) {
            Log.d("loginError",uiError.errorMessage+uiError.errorCode+
                    uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            Log.d("cancel","cancel");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
    }

    private void play() {
        iv_pause.setImageDrawable(getDrawable(R.drawable.video_view_bottom_bar_pause_24dp));
        plVideoView.start();
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        } else {
            ll_view_pager.setVisibility(View.GONE);
        }
        super.onConfigurationChanged(newConfig);
    }

    private void initMiddleBarTextView() {
        middle_bar_textView = new TextView[fragments.size()];
        middle_bar_textView[0] = findViewById(R.id.video_view_viewpager_middle_bar_chat);
        middle_bar_textView[1] = findViewById(R.id.video_view_viewpager_middle_bar_anchor);
        middle_bar_textView[2] = findViewById(R.id.video_view_viewpager_middle_bar_rank);
        middle_bar_textView[3] = findViewById(R.id.video_view_viewpager_middle_bar_aristocrat);
        middle_bar_textView[4] = findViewById(R.id.video_view_viewpager_middle_bar_forum);
        final ArrayMap<View, Integer> map = new ArrayMap<>();
        for (int i = 0; i < middle_bar_textView.length; i++) {
            map.put(middle_bar_textView[i], i);
            middle_bar_textView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(map.get(v));
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        roomName=intent.getStringExtra("roomName");
        coverUrl=intent.getStringExtra("coverUrl");
    }

    private void initPager() {
        viewPager = findViewById(R.id.video_view_main_viewPager);
        fragments = new ArrayList<>();
        fragments.add(new FragmentChat());
        fragments.add(new FragmentAnchor());
        fragments.add(new FragmentRank());
        fragments.add(new FragmentAristocrat());
        FragmentForum fragmentForum=new FragmentForum();
        Bundle bundle=new Bundle();
        bundle.putString("roomName",roomName);
        fragmentForum.setArguments(bundle);
        fragments.add(fragmentForum);
        initMiddleBarTextView();
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments, null));
        viewPager.setCurrentItem(0);
        currentPage = 0;
        middle_bar_textView[0].setTextColor(0xffff8800);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

                if (bottom_line_bar_state == ViewPager.SCROLL_STATE_DRAGGING) {
                    float baseX = textViewDiff[currentPage] + middle_bar_textView[currentPage].getWidth() / 2 -
                            bottom_line_indicator.getWidth() / 2;
                    if (currentPage > 0 && currentPage < 4) {
                        if (v - last_position > 0) {
                            bottom_line_indicator.setX(baseX + (textViewDiff[currentPage + 1] -
                                    textViewDiff[currentPage]) * v);
                        } else {
                            bottom_line_indicator.setX(baseX - (textViewDiff[currentPage] -
                                    textViewDiff[currentPage - 1]) * (1 - v));
                        }
                    } else if (currentPage == 0) {
                        if (v - last_position > 0) {
                            bottom_line_indicator.setX(baseX + (textViewDiff[currentPage + 1] -
                                    textViewDiff[currentPage]) * v);
                        }
                    } else if (currentPage == 4) {
                        if (v - last_position < 0) {
                            bottom_line_indicator.setX(baseX - (textViewDiff[currentPage] -
                                    textViewDiff[currentPage - 1]) * (1 - v));
                        }
                    }
                } else if (!isFirstTimeLoad && bottom_line_bar_state == ViewPager.SCROLL_STATE_SETTLING) {
                    bottom_line_indicator.setX(textViewDiff[currentPage] +
                            middle_bar_textView[currentPage].getWidth() / 2 - bottom_line_indicator.getWidth() / 2);
                }

                last_position = v;
                isFirstTimeLoad = false;
            }

            @Override
            public void onPageSelected(int index) {
                currentPage = index;
                for (int i = 0; i < middle_bar_textView.length; i++) {
                    if (i == index) {
                        middle_bar_textView[i].setTextColor(0xffff8800);
                    } else {
                        middle_bar_textView[i].setTextColor(0xff000000);
                    }
                }
                float textViewX = 0;
                if (index == 3) {
                    textViewX = aristocrat_count_ll.getLeft() + aristocrat_count_ll.getWidth() / 2;
                } else {
                    textViewX = middle_bar_textView[index].getLeft() + middle_bar_textView[index].getWidth() / 2;
                }
                bottom_line_indicator.setX((int) (textViewX - bottom_line_indicator.getWidth() / 2));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                bottom_line_bar_state = state;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*textViewDiff=new float[4];
        for(int i=0;i<textViewDiff.length;i++){
            if(i==2){
                textViewDiff[i]=aristocrat_count_ll.getX()-middle_bar_textView[i].getX();
            }else {
                textViewDiff[i] = middle_bar_textView[i + 1].getX() - middle_bar_textView[i].getX();
            }
        }*/
        textViewDiff = new float[5];
        for (int i = 0; i < middle_bar_textView.length; i++) {
            if (i == 3) {
                textViewDiff[i] = aristocrat_count_ll.getX();
            } else {
                textViewDiff[i] = middle_bar_textView[i].getX();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                PackageManager.PERMISSION_GRANTED) {
            initPlayerSurface();
        }
    }

    private void initPlayerSurface() {
        plVideoView = findViewById(R.id.plVideoView_player);
        plVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideStatusBar();
                return false;
            }
        });
        //MediaController controller=new MediaController(this);
        //plVideoView.setMediaController(controller);
        View buffer_animation_view = LayoutInflater.from(this).
                inflate(R.layout.buffer_animation, null);
        plVideoView.setBufferingIndicator(buffer_animation_view);
        plVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);

        Log.d("MainActivity", Environment.getExternalStorageDirectory().getAbsolutePath());
        /*plVideoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/DCIM/tieba/testVideo.mp4");*/
        plVideoView.setVideoPath("rtmp://192.168.88.107:1935/hls/test");
        //plVideoView.setVideoPath("/storage/emulated/0/DCIM/tieba/testVideo.mp4");
        plVideoView.start();
    }

    private void pause() {
        iv_pause.setImageDrawable(getDrawable(R.drawable.video_view_play_button));
        plVideoView.pause();
    }

    private void doShareToQzone() {
        Bundle bundle = new Bundle();
        bundle.putInt("req_type",1);
        bundle.putString("title","直播");
        bundle.putString("summary","有一种陪伴叫斗鱼直播,我在"+roomName+"直播间等你");

        bundle.putString("targetUrl", "http://www.qq.com");
        ArrayList<String> imageUrls = new ArrayList<String>();

        //imageUrls.add(Environment.getExternalStorageDirectory()+"/tieba/Yukee.jpg");
        imageUrls.add(coverUrl);
        bundle.putStringArrayList("imageUrl", imageUrls);
        Log.d("doShareToQ",coverUrl);
        if (null != mTencent) {
            //mTencent.publishToQzone(this, bundle, qZoneShareListener);
            mTencent.shareToQzone(this,bundle,qZoneShareListener);
        }
    }

    IUiListener qZoneShareListener = new IUiListener() {

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this,"cancel",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Toast.makeText(MainActivity.this,e.errorDetail+"|"+e.errorCode+"|"+
                    e.errorMessage, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
        }

    };
}
