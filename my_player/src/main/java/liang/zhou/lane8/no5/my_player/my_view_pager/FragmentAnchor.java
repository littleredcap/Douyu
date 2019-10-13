package liang.zhou.lane8.no5.my_player.my_view_pager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import liang.zhou.lane8.no5.my_player.AnchorEventManager;
import liang.zhou.lane8.no5.my_player.EventCommentManager;
import liang.zhou.lane8.no5.my_player.R;

public class FragmentAnchor extends FragmentBase {

    private RecyclerView recyclerView;
    private MyRecyclerAnchorAdapter anchorAdapter;
    private AnchorEventManager anchorEventManager;
    private EventCommentManager eventCommentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) inflater.inflate(R.layout.video_view_main_fragment,null);

        recyclerView=viewGroup.findViewById(R.id.video_view_main_rv);

        anchorEventManager=AnchorEventManager.getAnchorEventManager();
        eventCommentManager=EventCommentManager.getEventCommentManager();
        anchorAdapter=new MyRecyclerAnchorAdapter(anchorEventManager.getAnchorEvents(),
                eventCommentManager.getEventComments());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(anchorAdapter);
        /*ViewGroup anchor_top= (ViewGroup) inflater.inflate(R.layout.video_view_main_recyclerview_anchor_top,
                recyclerView);*/
        return viewGroup;
    }
}
