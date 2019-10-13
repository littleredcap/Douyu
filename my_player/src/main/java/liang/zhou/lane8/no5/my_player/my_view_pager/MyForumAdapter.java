package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.ActivityPostDetails;
import liang.zhou.lane8.no5.my_player.Post;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.MyCircleImageView;
import liang.zhou.lane8.no5.my_player.ui.Utils;
import okhttp3.Call;
import okhttp3.Response;

public class MyForumAdapter extends RecyclerView.Adapter {

    private final int ITEM_VIEW_TYPE_TOP_BAR=0;
    private final int ITEM_VIEW_TYPE_ALWAYS_TOP=1;
    private final int ITEM_VIEW_TYPE_BODY=2;

    private ArrayList<Post> posts;
    private ArrayList<Post> alwaysTop;
    private Context ctx;
    private Calendar c;
    private MyHandler handler;
    private final int UPDATE_LIKE=0;
    private String roomName;

    public MyForumAdapter(ArrayList<Post> posts,String roomName){
        this.posts=posts;
        alwaysTop=new ArrayList<>();
        alwaysTop.add(posts.get(posts.size()-1));
        alwaysTop.add(posts.get(posts.size()-2));
        alwaysTop.add(posts.get(posts.size()-3));
        /*posts.remove(posts.size()-1);
        posts.remove(posts.size()-2);
        posts.remove(posts.size()-3);*/
        handler=new MyHandler();
        this.roomName=roomName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ViewGroup itemView=null;
        ctx=viewGroup.getContext();
        if(viewType==ITEM_VIEW_TYPE_TOP_BAR){
            itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.video_view_main_forum_recyclerview_top,viewGroup,false);
        }else if(viewType==ITEM_VIEW_TYPE_ALWAYS_TOP){
            itemView=createAlwaysTopLinearLayout(viewGroup);
            for(int i=0;i<3;i++){
                /*ViewGroup viewGroupAlwaysTop= (ViewGroup) LayoutInflater.from(itemView.getContext()).
                        inflate(R.layout.video_view_main_forum_post,null);
                viewGroupAlwaysTop.findViewById(R.id.video_view_main_forum_base_info_rl).setVisibility(View.GONE);
                viewGroupAlwaysTop.findViewById(R.id.video_view_main_forum_always_top_tv).setVisibility(View.VISIBLE);
                viewGroupAlwaysTop.findViewById(R.id.video_view_main_forum_interact_rl).setVisibility(View.GONE);
                itemView.addView(viewGroupAlwaysTop);*/
                ViewGroup viewGroupAlwaysTop= (ViewGroup) LayoutInflater.from(itemView.getContext()).
                        inflate(R.layout.video_view_main_forum_recyclerview_always_top,itemView);
            }
        }else if(viewType==ITEM_VIEW_TYPE_BODY){
            itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.video_view_main_forum_post,viewGroup,false);

        }
        return new ForumViewHolder(itemView);
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==UPDATE_LIKE){
                int howManyLike=msg.arg1;
                int position=msg.arg2;
                update(howManyLike,position, (TextView) msg.obj);
            }
        }
    }
    private void update(int like,int position,TextView like_tv){
        Log.d("onLikeTouch",like+"howManyLike");
        //like_tv.setText(like+"");
        posts.get(position).setHowManyLike(like);
        notifyItemChanged(position,"1");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        ForumViewHolder forumViewHolder= (ForumViewHolder) holder;
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else{
            forumViewHolder.like.setText(posts.get(position).getHowManyLike()+"");
        }
    }

    private ViewGroup createAlwaysTopLinearLayout(ViewGroup viewGroup) {
        LinearLayout linearLayout=new LinearLayout(viewGroup.getContext());
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(0,Utils.dp2px(viewGroup.getContext(),0),0,
                Utils.dp2px(viewGroup.getContext(),5));
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ForumViewHolder fvh= (ForumViewHolder) viewHolder;
        Post post=posts.get(i);
        User postHolder=post.getUser();
        fvh.video_view_main_forum_post_title_tv.setText(post.getTitle());
        fvh.video_view_main_forum_post_title_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(ctx, ActivityPostDetails.class);
                Gson gson=new Gson();
                String json=gson.toJson(post);
                intent.putExtra("topic_post",json);
                intent.putExtra("roomName",roomName);
                ctx.startActivity(intent);
                return false;
            }
        });
        Glide.with(ctx).load(postHolder.getPortrait()).into(fvh.portrait);
        fvh.username.setText(postHolder.getUsername());
        if(c==null){
           c = Calendar.getInstance();
        }
        c.setTime(post.getPostDate());
        fvh.time.setText(c.get(Calendar.MONTH)+1 + "月" + c.get(Calendar.DAY_OF_MONTH) + "日");
        fvh.read.setText(post.getHowManyRead()+"");
        fvh.like.setText(post.getHowManyLike()+"");
        fvh.like.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                OKHttpUtil.uploadJSONs(Constant.HOST + "PostLikeServlet",
                        getJson(post.getPostId()), new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer=response.body().string();
                            JSONObject jsonObject=new JSONObject(jsonFromServer);
                            int howManyLike=jsonObject.getInt("howManyLike");

                            Message message= Message.obtain();
                            message.what=UPDATE_LIKE;
                            message.arg1=howManyLike;
                            message.arg2=i;
                            message.obj=fvh.like;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e){

                        }
                    }
                });
                return false;
            }


        });
    }



    private String getJson(int postId) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("postId",postId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public int getItemViewType(int position) {
        /*if(position==0){
            return ITEM_VIEW_TYPE_TOP_BAR;
        }else if(position==1){
            return ITEM_VIEW_TYPE_ALWAYS_TOP;
        }*/
        return ITEM_VIEW_TYPE_BODY;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ForumViewHolder extends RecyclerView.ViewHolder{

        private TextView video_view_main_forum_post_title_tv,username,time,read,like;
        private MyCircleImageView portrait;

        public ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            video_view_main_forum_post_title_tv=itemView.
                    findViewById(R.id.video_view_main_forum_post_title_tv);
            portrait=itemView.findViewById(R.id.video_view_main_forum_post_portrait);
            username=itemView.findViewById(R.id.video_view_main_forum_post_username);
            time=itemView.findViewById(R.id.video_view_main_forum_post_time);
            read=itemView.findViewById(R.id.video_view_main_forum_post_read);
            like=itemView.findViewById(R.id.video_view_main_forum_post_like);
        }
    }
}
