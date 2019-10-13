package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.Anchor;
import liang.zhou.lane8.no5.my_player.AnchorEvent;
import liang.zhou.lane8.no5.my_player.EventComment;
import liang.zhou.lane8.no5.my_player.R;

public class MyRecyclerAnchorAdapter extends RecyclerView.Adapter {

    private ArrayList<AnchorEvent> anchorEvents;
    private ArrayList<EventComment> comments;
    private View headerView;
    private final int VIEW_TYPE_HEADER=0;
    private final int VIEW_TYPE_BODY=1;
    private final int VIEW_TYPE_FOOTER=2;

    public MyRecyclerAnchorAdapter(ArrayList<AnchorEvent> anchorEvents,ArrayList<EventComment> comments){
        this.anchorEvents=anchorEvents;
        this.comments=comments;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemRootView=null;

        if(viewType==VIEW_TYPE_HEADER) {
            itemRootView=LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.video_view_main_recyclerview_anchor_top,viewGroup,false);
            headerView=itemRootView;
        }else{
            itemRootView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.video_view_main_recyclerview_anchor_event, viewGroup, false);
            if(comments!=null&&comments.size()!=0) {
                designCommentLayout(itemRootView);
            }

        }
        return new EventHolder(itemRootView,viewType);
    }


    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_BODY;
    }

    private void designCommentLayout(View itemRootView){
        LinearLayout comment_ll = itemRootView.findViewById(R.id.
                video_view_main_recycler_anchor_comment_ll);
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) comment_ll.getLayoutParams();
        for(int index=0;index<comments.size();index++){
            LinearLayout ll=new LinearLayout(comment_ll.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv_comment_username=new TextView(ll.getContext());
            TextView tv_colon=new TextView(ll.getContext());
            TextView tv_comment_content=new TextView(ll.getContext());
            tv_comment_content.setText(comments.get(index).getContent());
            tv_colon.setText(":");
            tv_comment_username.setText(comments.get(index).getUser().getUsername());
            ll.addView(tv_comment_username);
            ll.addView(tv_colon);
            ll.addView(tv_comment_content);
            comment_ll.addView(ll);
            if(index==2){
                TextView tv_more=new TextView(ll.getContext());
                tv_more.setText("查看更多"+(comments.size()-3)+"条评论");
                comment_ll.addView(tv_more);
                break;
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        /*if(i==0){
            return;
        }else {
            AnchorEvent anchorEvent = anchorEvents.get(i-1);
            Anchor anchor = anchorEvent.getAnchor();
            EventHolder eventHolder = (EventHolder) viewHolder;
            eventHolder.anchorEvent_tv.setText(anchorEvent.getContent());
            eventHolder.anchorName_tv.setText(anchor.getAnchorName());
            eventHolder.date_tv.setText(anchorEvent.getDate());
        }*/
    }

    @Override
    public int getItemCount() {
        return headerView!=null?anchorEvents.size()+1:anchorEvents.size();
    }

    class EventHolder extends RecyclerView.ViewHolder{

        ImageView portrait;
        TextView anchorName_tv,anchorEvent_tv,date_tv;

        public EventHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            /*if(viewType==0){

            }else {
                anchorName_tv = itemView.findViewById(R.id.video_view_main_recycler_anchor_anchorName_tv);
                anchorEvent_tv = itemView.findViewById(R.id.video_view_main_recycler_anchor_content_tv);
                date_tv = itemView.findViewById(R.id.video_view_main_recycler_anchor_event_date_tv);
            }*/
        }
    }
}
