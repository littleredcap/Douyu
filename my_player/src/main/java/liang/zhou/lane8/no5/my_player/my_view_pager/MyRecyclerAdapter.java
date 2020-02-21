package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.DanMu;
import liang.zhou.lane8.no5.my_player.R;

public class MyRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<HashMap> danMus;


    public MyRecyclerAdapter(ArrayList<HashMap> danMus){
        this.danMus=danMus;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRootView= LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.video_view_main_recyclerview_chat,viewGroup,false);
        return new CommentHolder(itemRootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CommentHolder holder= (CommentHolder) viewHolder;
        HashMap danMu=danMus.get(i);
        /*User user=danMus.get(i).getTaker();
        if(user.getRank()==null||user.getRank().equals("")){
            holder.video_view_main_recycler_rank.setText("0");
        }else {
            holder.video_view_main_recycler_rank.setText(user.getRank());
        }*/
        holder.video_view_main_recycler_username.setText(danMu.get("userHash").toString());
        holder.video_view_main_recycler_command.setText(danMu.get("text").toString());
    }

    @Override
    public int getItemCount() {
        return danMus.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder{

        private TextView video_view_main_recycler_rank,
                video_view_main_recycler_username,video_view_main_recycler_command;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            video_view_main_recycler_rank=itemView.findViewById(R.id.video_view_main_recycler_rank);
            video_view_main_recycler_username=itemView.findViewById(R.id.video_view_main_recycler_username);
            video_view_main_recycler_command=itemView.findViewById(R.id.video_view_main_recycler_command);
        }
    }
}
