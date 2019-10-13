package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import liang.zhou.lane8.no5.my_player.FansManager;
import liang.zhou.lane8.no5.my_player.R;

public class FragmentAristocrat extends FragmentBase {

    private RecyclerView recyclerView;
    private FansManager fansManager;
    private MyAristocratAdapter aristocratAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) LayoutInflater.from(FragmentAristocrat.this.getContext()).
                inflate(R.layout.video_view_main_fragment,null);
        fansManager=FansManager.getDanMuManger();
        initRecyclerView(viewGroup);
        View bottom_bar=LayoutInflater.from(FragmentAristocrat.this.getContext()).
                inflate(R.layout.video_view_main_aristocrat_bottom_bar,viewGroup);
        return viewGroup;
    }

    private void initRecyclerView(ViewGroup viewGroup) {
        recyclerView=viewGroup.findViewById(R.id.video_view_main_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(FragmentAristocrat.this.getContext()));
        aristocratAdapter=new MyAristocratAdapter(fansManager.getContributors());
        recyclerView.setAdapter(aristocratAdapter);
    }
}
