package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import liang.zhou.lane8.no5.my_player.FansManager;
import liang.zhou.lane8.no5.my_player.R;

public class FragmentFans extends Fragment {

    private RecyclerView recyclerView;
    private MyFansRankAdapter fansRankAdapter;
    private FansManager fansManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup recyclerViewGroup= (ViewGroup) LayoutInflater.from(this.getContext()).
                inflate(R.layout.video_view_main_recycleview_rank_recyclerview,null);
        fansManager=FansManager.getDanMuManger();
        initRecyclerView(recyclerViewGroup);
        ViewGroup bottomBar= (ViewGroup) LayoutInflater.from(this.getContext()).
                inflate(R.layout.video_view_main_recycleview_rank_fans_bottom,recyclerViewGroup);
        return recyclerViewGroup;
    }

    private void initRecyclerView(ViewGroup recyclerViewGroup) {
        recyclerView=recyclerViewGroup.findViewById(R.id.video_view_main_recycleview_rank_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerViewGroup.getContext()));
        fansRankAdapter=new MyFansRankAdapter(fansManager.getContributors());
        recyclerView.setAdapter(fansRankAdapter);
    }
}
