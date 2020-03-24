package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.MainActivity;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class AllAdapter extends RecyclerView.Adapter {

    private GridView gridView;
    private Context ctx;
    private MyHandler handler;
    private final static int LOAD_RECYCLER_VIEW=0;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ctx=viewGroup.getContext();
        handler=new MyHandler();
        ViewGroup itemView = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.home_page_rec_all_recycler_view_item, viewGroup, false);
        initGridView(itemView);
        return new AllAdapterViewHolder(itemView);
    }

    private void initGridView(ViewGroup itemView) {
        gridView = itemView.findViewById(R.id.home_page_rec_all_recycler_view_grid_view);
        OKHttpUtil.uploadJson(Constant.HOST+"FetchLiveRoomServlet",-1,"howMany",
                7+"",new ServerResponse(){

            @Override
            public void response(Call call, Response response) {
                try {
                    String jsonFromServer=response.body().string();
                    Gson gson=new Gson();
                    //LiveRoom liveRoom=gson.fromJson(jsonFromServer,LiveRoom.class);
                    ArrayList<LiveRoom> rooms=gson.fromJson(jsonFromServer,
                            new TypeToken<ArrayList<LiveRoom>>(){}.getType());
                    Message message=handler.obtainMessage();
                    message.what=LOAD_RECYCLER_VIEW;
                    message.obj=rooms;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class AllAdapterViewHolder extends RecyclerView.ViewHolder {

        public AllAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==LOAD_RECYCLER_VIEW){
                ArrayList<LiveRoom> liveRooms= (ArrayList<LiveRoom>) msg.obj;
                //LiveRoom liveRoom= (LiveRoom) msg.obj;
                //liveRooms.add(liveRoom);
                gridView.setAdapter(new GridViewAdapter(liveRooms));
            }
        }
    }

    class GridViewAdapter extends BaseAdapter {

        private ArrayList<LiveRoom> liveRooms;
        private RequestOptions options;

        public GridViewAdapter(ArrayList<LiveRoom> liveRooms){
            this.liveRooms=liveRooms;
            Log.d("gridViewAdapter","size"+liveRooms.size());
            RoundCornerTrans r=new RoundCornerTrans(ctx);
            r.setNeedCorner(true,true,false,false);
            options=RequestOptions.bitmapTransform(r);
        }

        @Override
        public int getCount() {
            return liveRooms.size();
        }

        @Override
        public Object getItem(int position) {
            return liveRooms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.home_page_recommend_in_recommend_card_view, null);
                viewHolder.imageView=convertView.findViewById(R.id.home_page_rec_in_rec_card_view_room_image);
                viewHolder.tv_room_name=convertView.findViewById(R.id.home_page_rec_in_rec_card_view_room_name);
                viewHolder.tv_room_title=convertView.findViewById(R.id.home_page_rec_in_rec_card_view_room_title);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            LiveRoom l=liveRooms.get(position);
            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent=new Intent(parent.getContext(), MainActivity.class);
                    intent.putExtra("roomName",l.getRoomName());
                    intent.putExtra("coverUrl",l.getRoomPicUrl());
                    intent.putExtra("roomId",l.getId());
                    parent.getContext().startActivity(intent);
                    return false;
                }
            });
            Glide.with(parent.getContext()).load(l.getRoomPicUrl()).apply(options).into(viewHolder.imageView);
            //viewHolder.tv_room_heat.setText(l.getRoomHeat()+"");
            viewHolder.tv_room_name.setText(l.getRoomName());
            viewHolder.tv_room_title.setText(l.getRoomTitle());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageView;
        TextView tv_room_title;
        TextView tv_room_heat;
        TextView tv_room_name;
    }
}
