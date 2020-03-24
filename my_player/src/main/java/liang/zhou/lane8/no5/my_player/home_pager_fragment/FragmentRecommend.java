package liang.zhou.lane8.no5.my_player.home_pager_fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.home_page_general_game_fragment.ListArrivedListener;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.Classify;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.ClassifyManager;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.DataManager;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentClassify;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentAll;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentGame;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.FragmentRecommendInRec;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.Game;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.GameManager;
import liang.zhou.lane8.no5.my_player.my_view_pager.MyPagerAdapter;
import liang.zhou.lane8.no5.my_player.ui.Utils;

public class FragmentRecommend extends Fragment {

    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private ArrayList<Game> games;
    private String[] titlesStr = {"分类", "推荐", "全部"};
    private TabLayout tabLayout;
    private Context ctx;
    private MyHandler handler;
    private int ON_GAME_FETCH = 1;
    private GameManager gameManager;
    private ViewGroup fragmentRootView;

    private void initTabLayout(ViewGroup viewGroup) {
        tabLayout = viewGroup.findViewById(R.id.tabLayout);
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
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(getTabView(i));
        }
    }

    private void select(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView tv = view.findViewById(R.id.home_page_view_pager_recommend_tv);
        tv.setTextSize(18);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void unSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView tv = view.findViewById(R.id.home_page_view_pager_recommend_tv);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tv.setTextSize(14);
    }

    private View getTabView(int i) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.home_page_view_pager_recommend_text_view, null);
        TextView textView = view.findViewById(R.id.home_page_view_pager_recommend_tv);
        textView.setText(titles.get(i));
        return view;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragmentRootView == null) {
            fragmentRootView = (ViewGroup) inflater.
                    inflate(R.layout.home_page_view_pager_recommend, null);
        }
        ctx = container.getContext();
        handler = new MyHandler();
        gameManager = new GameManager();
        viewPager = fragmentRootView.findViewById(R.id.home_page_view_pager_in_view_pager);
        initGames();
        return fragmentRootView;
    }

    private void initGames() {
        gameManager.fetchAllGames(new ListArrivedListener<Game>() {
            @Override
            public void onListArrived(List<Game> data) {
                Log.d("onListArrived", data.toString());
                Message m = handler.obtainMessage();
                m.what = ON_GAME_FETCH;
                m.obj = data;
                handler.sendMessage(m);
            }
        });
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ON_GAME_FETCH) {
                games = (ArrayList<Game>) msg.obj;
                if (games != null && games.size() > 0) {
                    initTitles();
                    initFragments();
                    viewPager.setAdapter(new MyPagerAdapter(getFragmentManager(), fragments, titles));
                    viewPager.setCurrentItem(0);
                    initTabLayout(fragmentRootView);
                }
            }
        }
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        for(int i=0;i<titlesStr.length;i++){
            Fragment f = null;
            Bundle bundle = new Bundle();
            if (titles.get(i).equals("分类")) {
                f = new FragmentClassify();
                bundle.putInt("gameId", -1);
            } else if (titles.get(i).equals("推荐")) {
                f = new FragmentRecommendInRec();
                bundle.putInt("gameId", -1);
            } else if (titles.get(i).equals("全部")) {
                f = new FragmentAll();
                bundle.putInt("gameId", -1);
            }
            f.setArguments(bundle);
            fragments.add(f);
        }
        /*fragments.add(new FragmentGame());
        fragments.add(new FragmentGame());
        fragments.add(new FragmentGame());
        fragments.add(new FragmentGame());*/
        for (int i = 0; i < games.size(); i++) {
            Fragment f ;
            Bundle bundle = new Bundle();
            f = new FragmentGame();
            bundle.putInt("gameId", games.get(i).getGameId());
            f.setArguments(bundle);
            fragments.add(f);
        }
    }

    private void initTitles() {
        titles = new ArrayList<>();
        for (int i = 0; i < titlesStr.length; i++) {
            titles.add(titlesStr[i]);
        }
        Log.d("initTitle", games.get(0).toString());
        for (Game g : games) {
            titles.add(g.getGameName());
        }
    }
}
