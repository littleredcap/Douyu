package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;

public class FragmentClassify extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ClassifyManager manager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        manager=ClassifyManager.newInstance();
        //manager.setGameClassFetchListener(this);
        ViewGroup recyclerViewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_view_pager_recommend_recycler_view,null);
        myHandler=new MyHandler();
        initRecyclerView(recyclerViewGroup);

        return recyclerViewGroup;
    }



    private MyHandler myHandler;
    private static final int LOAD_RECYCLER_VIEW=0;
    private void initRecyclerView(ViewGroup recyclerViewGroup) {
        recyclerView=recyclerViewGroup.
                findViewById(R.id.home_page_view_pager_recommend_recycler_view);
        manager.fetchClassify(new ClassifyManager.GameClassFetchListener() {
            @Override
            public void onSuccess(ArrayList<GameClass> gameClasses) {
                Message message=myHandler.obtainMessage();
                message.obj=gameClasses;
                myHandler.sendMessage(message);
            }
        });
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==LOAD_RECYCLER_VIEW){
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                //Log.d("initRecyclerView",manager.getClassifies().get(0).getItems().size()+"");
                //ArrayList<GameClass> gc=new ArrayList<>();
                //gc.add(gameClasses.get(0));
                adapter=new RecommendAdapter((ArrayList<GameClass>) msg.obj);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
