package liang.zhou.lane8.no5.my_player.home_page_general_game_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.MainActivity;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.LiveRoom;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.RoundCornerTrans;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.Utils;
import okhttp3.Call;
import okhttp3.Response;

public class FragmentLive extends Fragment {

    private RecyclerView recyclerView;
    private MyHandler myHandler;
    private final int LOAD_RECYCLER_VIEW = 0;
    private Context ctx;
    private boolean userVisible = false;
    private boolean is_first_load = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && is_first_load&&recyclerView!=null) {
            //fillForRecycle();
        }
    }

    private void fillForRecycle() {
        OKHttpUtil.uploadJson(Constant.HOST + "FetchLiveRoomServlet", -1, "howMany",
                8 + "", new ServerResponse() {

                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer = response.body().string();
                            Gson gson = new Gson();
                            //LiveRoom liveRoom=gson.fromJson(jsonFromServer,LiveRoom.class);
                            ArrayList<LiveRoom> rooms = gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<LiveRoom>>() {
                                    }.getType());
                            Message message = myHandler.obtainMessage();
                            message.what = LOAD_RECYCLER_VIEW;
                            message.obj = rooms;
                            myHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
       /* if(hidden) {
            if (recyclerView != null) {
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    recyclerView.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }else{
            if (recyclerView != null) {
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    recyclerView.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
        }*/
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOAD_RECYCLER_VIEW) {
                ArrayList<LiveRoom> liveRooms = (ArrayList<LiveRoom>) msg.obj;
                //LiveRoom liveRoom= (LiveRoom) msg.obj;
                //liveRooms.add(liveRoom);
                recyclerView.setAdapter(new MyRecyclerViewAdapter(liveRooms));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.home_page_rec_general_game_live_fragment,
                null);
        ctx = container.getContext();
        myHandler = new MyHandler();
        initRecyclerView(viewGroup);
        return viewGroup;
    }

    private void initRecyclerView(ViewGroup viewGroup) {
        recyclerView = viewGroup.findViewById(R.id.home_page_live_fragment_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(viewGroup.getContext(), 2);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = Utils.dp2px(viewGroup.getContext(), 15);
                if (parent.getChildAdapterPosition(view) % 2 == 0) {
                    outRect.left = Utils.dp2px(viewGroup.getContext(), 15);
                    outRect.right = Utils.dp2px(viewGroup.getContext(), 7);
                } else {
                    outRect.right = Utils.dp2px(viewGroup.getContext(), 15);
                    outRect.left = Utils.dp2px(viewGroup.getContext(), 7);
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        fillForRecycle();
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {

        private ArrayList<LiveRoom> liveRooms;
        private RequestOptions options;

        public MyRecyclerViewAdapter(ArrayList<LiveRoom> liveRooms) {
            this.liveRooms = liveRooms;
            RoundCornerTrans r = new RoundCornerTrans(ctx);
            r.setNeedCorner(true, true, false, false);
            options = RequestOptions.bitmapTransform(r);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            ViewGroup itemView = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.home_page_recommend_in_recommend_card_view, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            LiveRoom l = liveRooms.get(position);
            myViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent=new Intent(ctx, MainActivity.class);
                    intent.putExtra("roomName",l.getRoomName());
                    intent.putExtra("coverUrl",l.getRoomPicUrl());
                    ctx.startActivity(intent);
                    return false;
                }
            });
            Glide.with(ctx).load(l.getRoomPicUrl()).apply(options).into(myViewHolder.imageView);
            //viewHolder.tv_room_heat.setText(l.getRoomHeat()+"");
            myViewHolder.tv_room_name.setText(l.getRoomName());
            myViewHolder.tv_room_title.setText(l.getRoomTitle());
        }

        @Override
        public int getItemCount() {
            return liveRooms.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView tv_room_title;
            TextView tv_room_heat;
            TextView tv_room_name;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.home_page_rec_in_rec_card_view_room_image);
                tv_room_name = itemView.findViewById(R.id.home_page_rec_in_rec_card_view_room_name);
                tv_room_title = itemView.findViewById(R.id.home_page_rec_in_rec_card_view_room_title);
            }
        }
    }
}
