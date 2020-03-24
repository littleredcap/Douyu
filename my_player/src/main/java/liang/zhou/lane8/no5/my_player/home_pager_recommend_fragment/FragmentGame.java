package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.home_page_general_game_fragment.FragmentLive;
import liang.zhou.lane8.no5.my_player.home_page_general_game_fragment.FragmentMatchPreview;
import liang.zhou.lane8.no5.my_player.home_page_general_game_fragment.FragmentVideo;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Response;

public class FragmentGame extends Fragment {

    private Banner banner;
    private ArrayList<String> images;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private TextView[] textViews;
    private FragmentLive fragmentLive;
    private FragmentVideo fragmentVideo;
    private FragmentMatchPreview fragmentMatchPreview;
    private MyHandler myHandler;
    private final int INIT_BANNER=0;
    private int gameId;
    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=getArguments();
        //gameId=b.getInt("gameId");
        fragmentManager=getChildFragmentManager();
        if(savedInstanceState==null){
            initFragment();
        }else{
            fragmentLive= (FragmentLive) fragmentManager.findFragmentByTag("live");
            fragmentLive.setArguments(bundle);
            fragmentVideo= (FragmentVideo) fragmentManager.findFragmentByTag("video");
            fragmentMatchPreview= (FragmentMatchPreview) fragmentManager.findFragmentByTag("preview");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myHandler=new MyHandler();

        ViewGroup viewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_rec_general_game,null);
        initBanner(viewGroup);


        initTextView(viewGroup);
        return viewGroup;
    }



    private void initFragment() {
        if(fragmentLive==null) {
            fragmentLive = new FragmentLive();
            fragmentLive.setArguments(bundle);
            transaction=fragmentManager.beginTransaction();
            transaction.add(R.id.home_page_rec_general_game_fragment,fragmentLive,"live");
            transaction.commit();
        }
        if(fragmentVideo==null) {
            fragmentVideo = new FragmentVideo();

        }
        if(fragmentMatchPreview==null) {
            fragmentMatchPreview = new FragmentMatchPreview();

        }
    }
    private void initTextView(ViewGroup viewGroup){
        textViews=new TextView[3];

        textViews[0]=viewGroup.findViewById(R.id.home_page_general_game_live);
        textViews[0].setTextColor(Color.BLACK);
        textViews[1]=viewGroup.findViewById(R.id.home_page_general_game_video);
        textViews[2]=viewGroup.findViewById(R.id.home_page_general_game_match_preview);
        textViews[0].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //transaction.replace(R.id.home_page_rec_general_game_fragment,fragmentLive);
                //transaction.show(fragmentLive);
                textViews[0].setTextColor(Color.BLACK);
                textViews[1].setTextColor(0xffC4C4C4);
                textViews[2].setTextColor(0xffC4C4C4);
                if(!fragmentLive.isVisible()){

                    transaction=fragmentManager.beginTransaction();
                    transaction.show(fragmentLive);
                    transaction.hide(fragmentMatchPreview);
                    transaction.hide(fragmentVideo);
                    //transaction.replace(R.id.home_page_rec_general_game_fragment,fragmentLive);
                    transaction.commit();
                }

                /*if(fragmentLive!=null&&!fragmentLive.isVisible()){
                    transaction.show(fragmentLive);
                }else if(fragmentLive==null) {
                    fragmentLive = new FragmentLive();
                    transaction.add(R.id.home_page_rec_general_game_fragment, fragmentLive);
                }*/

                return false;
            }
        });
        textViews[1].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //transaction.replace(R.id.home_page_rec_general_game_fragment,fragmentVideo);
                //transaction.add(R.id.home_page_rec_general_game_fragment,fragmentVideo);
                textViews[1].setTextColor(Color.BLACK);
                textViews[0].setTextColor(0xffC4C4C4);
                textViews[2].setTextColor(0xffC4C4C4);

                if(!fragmentVideo.isVisible()){
                    transaction=fragmentManager.beginTransaction();
                    if(!fragmentVideo.isAdded()){
                        transaction.add(R.id.home_page_rec_general_game_fragment,fragmentVideo,"video");
                    }
                    transaction.show(fragmentVideo);
                    transaction.hide(fragmentLive);
                    transaction.hide(fragmentMatchPreview);
                    //transaction.replace(R.id.home_page_rec_general_game_fragment,fragmentVideo);
                    transaction.commit();
                }

                /*if(fragmentVideo!=null&&!fragmentVideo.isVisible()){
                    transaction.show(fragmentVideo);
                }else if(fragmentVideo==null) {
                    fragmentVideo = new FragmentVideo();
                    transaction.add(R.id.home_page_rec_general_game_fragment, fragmentVideo);
                }*/
                return false;
            }
        });
        textViews[2].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                textViews[2].setTextColor(Color.BLACK);
                textViews[1].setTextColor(0xffC4C4C4);
                textViews[0].setTextColor(0xffC4C4C4);

                if(!fragmentMatchPreview.isVisible()){
                    transaction=fragmentManager.beginTransaction();
                    if(!fragmentMatchPreview.isAdded()){
                        transaction.add(R.id.home_page_rec_general_game_fragment,
                                fragmentMatchPreview,"preview");
                    }
                    transaction.show(fragmentMatchPreview);
                    transaction.hide(fragmentVideo);
                    transaction.hide(fragmentLive);
                    //transaction.replace(R.id.home_page_rec_general_game_fragment,fragmentMatchPreview);
                    transaction.commit();
                }
                /*if(fragmentMatchPreview!=null&&!fragmentMatchPreview.isVisible()){
                    transaction.show(fragmentMatchPreview);
                }else if(fragmentMatchPreview==null) {
                    fragmentMatchPreview = new FragmentMatchPreview();
                    transaction.add(R.id.home_page_rec_general_game_fragment, fragmentMatchPreview);
                }*/

                return false;
            }
        });
    }

    private void initImagePath(ArrayList<Advertisement> advs) {
        if (images == null) {
            images = new ArrayList<>();
        }
        for (Advertisement adv : advs) {
            images.add(adv.getPicUrl());
        }
        banner.setImages(images).start();
    }

    private void initBanner(ViewGroup viewGroup) {
        banner = viewGroup.findViewById(R.id.home_page_recommend_in_recommend_banner);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setImageLoader(new MyImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.d("onBannerClick", position + "");
            }
        });
        OKHttpUtil.uploadJson(Constant.HOST + "FetchAdvertisement",
                -1, "howManyAdv", "8", new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer = response.body().string();
                            Gson gson = new Gson();
                            Log.d("initBanner", jsonFromServer);
                            ArrayList<Advertisement> advs = gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<Advertisement>>() {
                                    }.getType());
                            Message message = myHandler.obtainMessage();
                            message.obj = advs;
                            message.what = INIT_BANNER;
                            ;
                            myHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==INIT_BANNER){
                initImagePath((ArrayList<Advertisement>) msg.obj);
            }
        }
    }
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            RequestOptions options = new RequestOptions();
            options.transform(new RoundedCornersTransformation(25, 0));
            Glide.with(context.getApplicationContext())
                    .load(path).apply(options)
                    .into(imageView);
        }
    }
}
