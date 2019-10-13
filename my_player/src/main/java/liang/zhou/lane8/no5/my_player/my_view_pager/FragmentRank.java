package liang.zhou.lane8.no5.my_player.my_view_pager;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;

public class FragmentRank extends FragmentBase {

    private ViewPager viewPager;
    private FragmentManager fManager;
    private ArrayList<Fragment> fragments;
    private TextView[] textViews;
    private boolean isFirstTimeLoad=true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) LayoutInflater.from(this.getContext()).
                inflate(R.layout.video_view_main_recycleview_rank,null);
        initTextViews(viewGroup);
        initFragment(viewGroup);
        return viewGroup;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragments.clear();
        fragments=null;
        for(int i=0;i<textViews.length;i++){
            textViews[i]=null;
        }
        textViews=null;
        viewPager.removeAllViews();
        viewPager=null;
    }

    private void initTextViews(ViewGroup viewGroup) {
        textViews=new TextView[3];
        textViews[0]=viewGroup.findViewById(R.id.tv_video_view_main_recycleview_rank_daily);
        textViews[1]=viewGroup.findViewById(R.id.tv_video_view_main_recycleview_rank_weekly);
        textViews[2]=viewGroup.findViewById(R.id.tv_video_view_main_recycleview_rank_fans);
    }

    private void initFragment(ViewGroup viewGroup) {
        fragments=new ArrayList<>();
        viewPager=viewGroup.findViewById(R.id.video_view_main_recycleview_rank_viewpager);

        fragments.add(new FragmentDaily());
        fragments.add(new FragmentWeekly());
        fragments.add(new FragmentFans());
        viewPager.setAdapter(new MyPagerAdapter(getFragmentManager(),fragments,null));
        viewPager.setCurrentItem(1);
        selectedTextViewState(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                /*if(isFirstTimeLoad){

                    isFirstTimeLoad=false;
                }*/
            }

            @Override
            public void onPageSelected(int index) {
                for (int i = 0; i < textViews.length; i++) {
                    if (i == index) {
                        selectedTextViewState(i);
                    } else {
                        unSelectedTextViewState(i);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void selectedTextViewState(int index) {
        if(index==0){
            textViews[0].setTextColor(Color.WHITE);
            textViews[0].setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg_b);
        }else if(index==1){
            textViews[1].setTextColor(Color.WHITE);
            textViews[1].setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg3_b);
        }else if(index==2){
            textViews[2].setTextColor(Color.WHITE);
            textViews[2].setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg2_b);
        }
    }
    private void unSelectedTextViewState(int index) {
        if(index==0){
            textViews[0].setTextColor(0xffff8800);
            textViews[0].setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg);
        }else if(index==1){
            textViews[1].setTextColor(0xffff8800);
            textViews[1].setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg3);
        }else if(index==2){
            textViews[2].setTextColor(0xffff8800);
            textViews[2].setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg2);
        }
    }
}
