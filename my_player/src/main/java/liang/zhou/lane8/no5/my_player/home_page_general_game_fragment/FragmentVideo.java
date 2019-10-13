package liang.zhou.lane8.no5.my_player.home_page_general_game_fragment;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.AllAdapter;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.LiveRoom;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.Utils;
import okhttp3.Call;
import okhttp3.Response;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class FragmentVideo extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager l;
    private ArrayList<PLVideoView> plVideoViews;
    private final int LOAD_RECYCLER_VIEW=0;
    private MyHandler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) inflater.inflate(R.layout.home_page_rec_general_game_live_fragment,
                null);
        handler=new MyHandler();
        plVideoViews=new ArrayList<>();
        initRecyclerView(viewGroup);
        return viewGroup;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        pause();
    }

    private void initRecyclerView(ViewGroup viewGroup) {
        recyclerView=viewGroup.findViewById(R.id.home_page_live_fragment_recycler_view);
        l=new LinearLayoutManager(viewGroup.getContext());
        recyclerView.setLayoutManager(l);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom= Utils.dp2px(parent.getContext(),25);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean firstLoad=true;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==SCROLL_STATE_IDLE){
                    //Log.d("onScrollStateChanged", l.findFirstVisibleItemPosition()+",");
                    pause();
                    play(recyclerView);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(firstLoad){
                    firstLoad=false;
                    pause();
                    play(recyclerView);

                }
            }
        });
        OKHttpUtil.uploadJson(Constant.HOST+"FetchVideoServlet",-1,"howMany",
                6+"",new ServerResponse(){

                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer=response.body().string();
                            Gson gson=new Gson();
                            ArrayList<MyVideo> videos = gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<MyVideo>>() {}.getType());
                            Message message=handler.obtainMessage();
                            message.what=LOAD_RECYCLER_VIEW;
                            message.obj=videos;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void pause(){
        if(plVideoViews!=null) {
            for (int i = 0; i < plVideoViews.size(); i++) {
                if (plVideoViews.get(i).isPlaying()) {
                    plVideoViews.get(i).pause();
                }
            }
        }
    }

    private void play(RecyclerView recyclerView){
        int firstItem=l.findFirstCompletelyVisibleItemPosition();
        Log.d("onScrollStateChanged", ","+firstItem);
        if(firstItem>=recyclerView.getAdapter().getItemCount()-1||firstItem<0){
            return;
        }
        MyAdapter.MyViewHolder m= (MyAdapter.MyViewHolder) recyclerView.
                findViewHolderForAdapterPosition(firstItem);
        plVideoViews.add(m.videoView);
        if(m.videoView!=null&&!m.videoView.isPlaying()){
            m.videoView.start();
            Log.d("onScrollStateChanged", l.findFirstVisibleItemPosition()+",");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==LOAD_RECYCLER_VIEW){
                ArrayList<MyVideo> videos= (ArrayList<MyVideo>) msg.obj;
                //LiveRoom liveRoom= (LiveRoom) msg.obj;
                //liveRooms.add(liveRoom);
                recyclerView.setAdapter(new MyAdapter(videos));
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter{

        private final int ITEM_TYPE_BOTTOM_EMPTY=0;
        private final int ITEM_TYPE_NORMAL=1;
        private int position;
        private ArrayList<MyVideo> videos;

        public MyAdapter(ArrayList<MyVideo> videos){
            this.videos=videos;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            ViewGroup itemView=null;
            if(viewType==ITEM_TYPE_BOTTOM_EMPTY){
                itemView=createEmptyView(viewGroup);
            }else {
                itemView = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.fragment_video_recycler_view_item, viewGroup, false);
            }
            return new MyViewHolder(itemView);
        }

        private ViewGroup createEmptyView(ViewGroup viewGroup) {
            LinearLayout l=new LinearLayout(viewGroup.getContext());
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.dp2px(viewGroup.getContext(),500));
            l.setLayoutParams(ll);
            return l;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (i==getItemCount()-1){
                return;
            }

            MyVideo video=videos.get(i);
            MyViewHolder m= (MyViewHolder) viewHolder;

            /*View buffer_animation_view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.buffer_animation, null);*/
            //videoView.setBufferingIndicator(buffer_animation_view);
            m.videoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);
            /*m.videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/DCIM/tieba/testVideo.mp4");*/
            m.videoView.setVideoURI(Uri.parse(video.getVideoUrl()));
            int firstItem=l.findFirstVisibleItemPosition();

            //m.videoView.start();
            //plVideoView.setVideoPath("/storage/emulated/0/DCIM/tieba/testVideo.mp4");
            //Log.d("fragmentVideo_bindView", m.itemView.hashCode()+","+view.hashCode());
            Log.d("fragmentVideo_bindView", l.findFirstCompletelyVisibleItemPosition()+","+i);

        }


        @Override
        public int getItemViewType(int position) {
            this.position=position;
            if(position==getItemCount()-1){
                return ITEM_TYPE_BOTTOM_EMPTY;
            }
            return ITEM_TYPE_NORMAL;
        }

        @Override
        public int getItemCount() {
            return videos.size()+1;
        }
        private class MyViewHolder extends RecyclerView.ViewHolder{

            PLVideoView videoView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                //initVideoView((ViewGroup) itemView);
                videoView = itemView.findViewById(R.id.fragment_video_recycler_view_item_pl_video_view);
            }
        }
    }
}
