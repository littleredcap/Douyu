package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.LinkedList;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.FansManager;
import liang.zhou.lane8.no5.my_player.R;

public class FragmentDaily extends Fragment {

    private RecyclerView recyclerView;
    private LinkedList<User> dailyRankFans;
    private FansManager fansManager;
    private MyDailyRankAdapter rankAdapter;
    private UpdateListHandler handler;
    private final int UPDATE_LIST=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup recyclerViewGroup= (ViewGroup) LayoutInflater.from(this.getContext()).
                inflate(R.layout.video_view_main_recycleview_rank_recyclerview,null);
        fansManager=FansManager.getDanMuManger();
        initRecyclerView(recyclerViewGroup);
        handler=new UpdateListHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    fansManager.update_daily_contribute_value();
                    handler.sendEmptyMessage(UPDATE_LIST);
                }
            }
        }).start();
        return recyclerViewGroup;
    }

    private void initRecyclerView(ViewGroup recyclerViewGroup) {
        recyclerView=recyclerViewGroup.findViewById(R.id.video_view_main_recycleview_rank_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(FragmentDaily.this.getContext()));
        rankAdapter=new MyDailyRankAdapter(fansManager.getContributors());
        recyclerView.setAdapter(rankAdapter);
    }
    class UpdateListHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==UPDATE_LIST){
                rankAdapter.notifyDataSetChanged();
            }
        }
    }
}
