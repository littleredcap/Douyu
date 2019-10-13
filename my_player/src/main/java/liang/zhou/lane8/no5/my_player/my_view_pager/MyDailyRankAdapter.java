package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.R;

public class MyDailyRankAdapter extends RecyclerView.Adapter {

    private LinkedList<User> contributors;

    public MyDailyRankAdapter(LinkedList<User> contributors){
        this.contributors=contributors;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.video_view_main_rank_daily_recyclerview_item2,viewGroup,false);
        return new RankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RankViewHolder rankViewHolder= (RankViewHolder) viewHolder;
        rankViewHolder.fansName.setText(contributors.get(i).getUsername());
        rankViewHolder.contribute_value.setText(contributors.get(i).getDaily_contribute_value()+"");
        rankViewHolder.rankLevel.setText(contributors.get(i).getRank());
        if(contributors.get(i).getDaily_contribute_value()>contributors.get(i).getLast_daily_contribute_value()){
            rankViewHolder.trend.setBackgroundResource(R.drawable.video_view_main_rank_trend_up);
        }else{
            rankViewHolder.trend.setBackgroundResource(R.drawable.video_view_main_rank_trend_down);
        }
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }
    class RankViewHolder extends RecyclerView.ViewHolder{

        TextView fansName,contribute_value,rankLevel;
        ImageView trend;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            fansName=itemView.findViewById(R.id.video_view_main_rank_daily_recycle_item_fansName);
            contribute_value=itemView.findViewById(R.id.video_view_main_rank_daily_recycle_item_contribute);
            rankLevel=itemView.findViewById(R.id.video_view_main_rank_daily_recycle_item_rank_level);
            trend=itemView.findViewById(R.id.video_view_main_rank_daily_recycle_item_trend);
        }
    }
}
