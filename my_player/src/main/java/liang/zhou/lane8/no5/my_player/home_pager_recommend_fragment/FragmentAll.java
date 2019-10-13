package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import liang.zhou.lane8.no5.my_player.R;

public class FragmentAll extends Fragment {

    private RecyclerView recyclerView;
    private AllAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup recyclerViewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_view_pager_recommend_recycler_view,null);
        initRecyclerView(recyclerViewGroup);
        return recyclerViewGroup;
    }

    private void initRecyclerView(ViewGroup recyclerViewGroup) {
        recyclerView=recyclerViewGroup.findViewById(R.id.
                home_page_view_pager_recommend_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerViewGroup.getContext()));
        adapter=new AllAdapter();
        recyclerView.setAdapter(adapter);
    }
}
