package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;

public class MyGridViewRecAdapter extends BaseAdapter {


    private ArrayList<Game> games;
    private RequestOptions options=RequestOptions.circleCropTransform();
    public MyGridViewRecAdapter(ArrayList<Game> games){
        this.games=games;
    }

    @Override
    public int getCount() {
        return games.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.home_page_recommend_recycler_view_item_grid_view,null);
            viewHolder.textView=convertView.findViewById(R.id.home_page_grid_view_game_name);
            viewHolder.iv=convertView.findViewById(R.id.home_page_grid_view_game_icon);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.textView.setVisibility(View.VISIBLE);
        viewHolder.iv.setVisibility(View.VISIBLE);
        if(position==games.size()){
            Drawable d=parent.getContext().getDrawable(R.drawable.video_view_more);
            d.setTint(Color.GRAY);
            viewHolder.iv.setImageDrawable(d);
            viewHolder.textView.setText("更多");
        }else {

            ViewHolder finalViewHolder = viewHolder;

            Glide.with(parent.getContext()).load(games.get(position).getGameIconUrl()).apply(options)
                    .into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource,
                                            @Nullable Transition<? super Drawable> transition) {
                    finalViewHolder.iv.setImageDrawable(resource);
                }
            });
            viewHolder.textView.setText(games.get(position).getGameName());
        }
        return convertView;
    }
}
class ViewHolder{

    ImageView iv;
    TextView textView;
}
