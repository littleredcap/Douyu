package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.MainActivity;
import liang.zhou.lane8.no5.my_player.R;

public class MyGridCardViewAdapter extends BaseAdapter {

    private ArrayList<LiveRoom> liveRooms;
    RequestOptions options;

    public MyGridCardViewAdapter(ArrayList<LiveRoom> liveRooms, Context ctx){
        this.liveRooms=liveRooms;
        RoundCornerTrans r=new RoundCornerTrans(ctx);
        r.setNeedCorner(true,true,false,false);
        options= RequestOptions.bitmapTransform(r);
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

        GridCardViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.home_page_recommend_in_recommend_card_view,null);
            viewHolder=new GridCardViewHolder();
            viewHolder.imageView=convertView.findViewById(R.id.home_page_rec_in_rec_card_view_room_image);
            viewHolder.tv_room_name=convertView.findViewById(R.id.home_page_rec_in_rec_card_view_room_name);
            viewHolder.tv_room_title=convertView.findViewById(R.id.home_page_rec_in_rec_card_view_room_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (GridCardViewHolder) convertView.getTag();
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

    class GridCardViewHolder{
        ImageView imageView;
        TextView tv_room_title;
        TextView tv_room_heat;
        TextView tv_room_name;
    }
}
