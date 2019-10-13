package liang.zhou.lane8.no5.my_player;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.my_view_pager.MyPagerAdapter;

public class ActivityAddress extends AppCompatActivity implements AddressSelectedListener {

    private TextView domestic_tv,abroad_tv;
    private ViewPager viewPager;
    private Fragment domestic_fragment,abroad_fragment;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition transition= TransitionInflater.from(this).inflateTransition(android.R.transition.explode);
        getWindow().setExitTransition(transition);
        getWindow().setEnterTransition(transition);
        getWindow().setReenterTransition(transition);*/
        setContentView(R.layout.activity_address_picker_top);
        fragments=new ArrayList<>();
        domestic_tv=findViewById(R.id.activity_address_picker_top_dome_tv);
        abroad_tv=findViewById(R.id.activity_address_picker_top_abroad_tv);
        initFragment();
        initViewPager();
    }

    private void initFragment(){
        domestic_fragment=new FragmentDomestic();
        ((FragmentDomestic) domestic_fragment).setListener(this);
        abroad_fragment=new FragmentAbroad();
        fragments.add(domestic_fragment);
        fragments.add(abroad_fragment);
    }

    private void initViewPager(){
        viewPager=findViewById(R.id.activity_address_picker_top_view_pager);
        viewPager.setCurrentItem(0);
        selectedTextViewState(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0; j < fragments.size();j++) {
                    if (j == i) {
                        selectedTextViewState(j);
                    } else {
                        unSelectedTextViewState(j);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragments,null));
    }
    private void selectedTextViewState(int index) {
        if(index==0){
            domestic_tv.setTextColor(Color.WHITE);
            domestic_tv.setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg_b);
        }else if(index==1){
            abroad_tv.setTextColor(Color.WHITE);
            abroad_tv.setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg2_b);
        }
    }
    private void unSelectedTextViewState(int index) {
        if(index==0){
            domestic_tv.setTextColor(0xffff8800);
            domestic_tv.setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg);
        }else if(index==1){
            abroad_tv.setTextColor(0xffff8800);
            abroad_tv.setBackgroundResource(R.drawable.video_view_main_recycleview_rank_tv_bg2);
        }
    }

    @Override
    public void onAddressSelected(String address, int itemPosition,String city[]) {
        setContentView(R.layout.activity_address_city_select);
        Toolbar toolbar=findViewById(R.id.activity_address_city_select_toolbar);
        toolbar.setTitle("选择城市");
        Drawable drawableBack=getDrawable(R.drawable.video_view_top_bar_back_24dp);
        drawableBack.setTint(Color.GRAY);
        toolbar.setNavigationIcon(drawableBack);
        setSupportActionBar(toolbar);
        RecyclerView r=findViewById(R.id.activity_address_city_select_recycler_view);
        r.setAdapter(new MyAdapter(city));
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private class MyAdapter extends RecyclerView.Adapter{

        private String city[];
        MyAdapter(String city[]){
            this.city=city;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            ViewGroup itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.activity_address_picker_recycler_item,viewGroup,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            MyViewHolder myViewHolder= (MyViewHolder) viewHolder;
            myViewHolder.address_tv.setText(city[position]);
            myViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("ActivityAddress_onTouch",city[position]);
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        Intent intent=new Intent(ActivityAddress.this,ActivityInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("address", city[position]);
                        bundle.putInt("itemPosition", position);
                        intent.putExtra("addressBundle", bundle);
                        startActivity(intent);
                    }
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return city.length;
        }
        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView address_tv;
            ImageView forward_iv;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                address_tv=itemView.findViewById(R.id.address_picker_recycler_item_province);
                forward_iv=itemView.findViewById(R.id.address_picker_recycler_item_forward);
                forward_iv.setVisibility(View.GONE);
            }
        }
    }
}
