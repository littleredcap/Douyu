package liang.zhou.lane8.no5.my_player;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyViewPager {

    private ViewGroup parentView;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;

    public MyViewPager(ViewGroup parentView){
        this.parentView=parentView;
    }

    public void initViewPager(int viewPagerId){
        viewPager=parentView.findViewById(viewPagerId);
    }
    public void initFragment(Fragment fragment){
        fragments.add(fragment);
    }
}
