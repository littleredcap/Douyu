package liang.zhou.lane8.no5.my_player.home_pager_fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentClassify;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentAll;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentGame;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentRecommendInRec;
import liang.zhou.lane8.no5.my_player.my_view_pager.MyPagerAdapter;
import liang.zhou.lane8.no5.my_player.ui.Utils;

public class FragmentRecommend extends Fragment {

    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private String[] titlesStr={"分类","推荐","全部","泡泡卡丁车","泡泡堂","dota2","lol"};
    private TabLayout tabLayout;
    private Context ctx;

    private void initTabLayout(ViewGroup viewGroup) {
        tabLayout=viewGroup.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                select(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                unSelect(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (int i=0;i<tabLayout.getTabCount();i++) {
            tabLayout.getTabAt(i).setCustomView(getTabView(i));
        }
    }

    private void select(TabLayout.Tab tab){
        View view=tab.getCustomView();
        TextView tv=view.findViewById(R.id.home_page_view_pager_recommend_tv);
        tv.setTextSize(18);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    private void unSelect(TabLayout.Tab tab){
        View view=tab.getCustomView();
        TextView tv=view.findViewById(R.id.home_page_view_pager_recommend_tv);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tv.setTextSize(14);
    }

    private View getTabView(int i) {
        View view=LayoutInflater.from(ctx).inflate(R.layout.home_page_view_pager_recommend_text_view,null);
        TextView textView=view.findViewById(R.id.home_page_view_pager_recommend_tv);
        textView.setText(titlesStr[i]);
        return view;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_view_pager_recommend,null);
        ctx=container.getContext();
        viewPager=viewGroup.findViewById(R.id.home_page_view_pager_in_view_pager);
        initTitles();

        initFragments();
        viewPager.setAdapter(new MyPagerAdapter(getFragmentManager(),fragments,titles));
        viewPager.setCurrentItem(0);
        initTabLayout(viewGroup);
        return viewGroup;
    }

    private void initFragments() {
        fragments=new ArrayList<>();
        fragments.add(new FragmentClassify());
        fragments.add(new FragmentRecommendInRec());
        fragments.add(new FragmentAll());
        fragments.add(new FragmentGame());
        fragments.add(new FragmentGame());
        fragments.add(new FragmentGame());
        fragments.add(new FragmentGame());
    }

    private void initTitles() {
        titles=new ArrayList<>();
        for(int i=0;i<titlesStr.length;i++){
            titles.add(titlesStr[i]);
        }
    }
}
