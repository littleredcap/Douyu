package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.R;

public class MyAristocratAdapter extends RecyclerView.Adapter {

    private LinkedList<User> aristocrats;
    private final int ITEM_TYPE_FOOTER=1;
    private final int ITEM_TYPE_NORMAL=0;
    private View footerView=null;

    public MyAristocratAdapter(LinkedList<User> aristocrats){
        this.aristocrats=aristocrats;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView=null;

        if(viewType==ITEM_TYPE_NORMAL) {
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.video_view_main_aristocrat_recycler_item, viewGroup, false);
        }
        if(viewType==ITEM_TYPE_FOOTER){

            itemView=  LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.video_view_main_aristocrat_item_footer,viewGroup,false);
            footerView=itemView;
        }
        return new AristocratViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemViewType(int position) {

        if(position==aristocrats.size()){
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return aristocrats.size()+1;
    }

    class AristocratViewHolder extends RecyclerView.ViewHolder{

        ImageView rank_name,portrait;
        TextView fansName;

        public AristocratViewHolder(@NonNull View itemView) {
            super(itemView);
            /*rank_name=itemView.findViewById(R.id.video_view_main_aristocrat_item_rank_name);
            portrait=itemView.findViewById(R.id.video_view_main_aristocrat_item_portrait);
            fansName=itemView.findViewById(R.id.video_view_main_aristocrat_item_fansName);*/
        }
    }
}
