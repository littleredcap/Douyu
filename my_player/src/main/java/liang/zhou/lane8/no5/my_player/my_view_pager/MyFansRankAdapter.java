package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.R;

public class MyFansRankAdapter extends RecyclerView.Adapter {

    private LinkedList<User> fans;

    public MyFansRankAdapter(LinkedList<User> fans){
        this.fans=fans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.video_view_main_rank_fans_item,viewGroup,false);
        return new FansRankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        FansRankViewHolder fansRankViewHolder= (FansRankViewHolder) viewHolder;
        fansRankViewHolder.rank_level.setText(fans.get(i).getRank());
        fansRankViewHolder.fans_name.setText(fans.get(i).getUsername());
    }

    @Override
    public int getItemCount() {
        return fans.size();
    }

    class FansRankViewHolder extends RecyclerView.ViewHolder{

        TextView fans_name,rank_level;

        public FansRankViewHolder(@NonNull View itemView) {
            super(itemView);
            fans_name=itemView.findViewById(R.id.video_view_main_rank_fans_recycle_item_rank_fansName);
            rank_level=itemView.findViewById(R.id.video_view_main_rank_fans_recycle_item_rank_level);
        }
    }
}
