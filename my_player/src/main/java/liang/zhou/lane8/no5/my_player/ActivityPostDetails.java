package liang.zhou.lane8.no5.my_player;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.business_utils.JSONUtil;
import liang.zhou.lane8.no5.my_player.network_model.DataArrivedListener;
import liang.zhou.lane8.no5.my_player.network_model.NetworkModelContext;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.MyCircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class ActivityPostDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter forumAdapter;
    private MyHandler myHandler;
    private int currentPage=0;
    private int pageSize=5;
    private int replyToWhom;
    private Post topic_post;
    private String post_str;
    private String roomName;
    private EditText sendText;
    private MyApplication app;
    private Calendar c;
    private NetworkModelContext networkModelContext;
    private DisplayCutout cutout;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_post_details);
        app= (MyApplication) getApplication();
        networkModelContext=new NetworkModelContext();
        networkModelContext.useRxJava();
        recyclerView=findViewById(R.id.forum_post_details_post);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==SCROLL_STATE_IDLE){
                    LinearLayoutManager manager= (LinearLayoutManager) recyclerView.getLayoutManager();
                    Log.d("onScrollStateChanged", manager.findLastVisibleItemPosition()+","+currentPage);
                    if(manager.findLastVisibleItemPosition()==recyclerView.getAdapter().getItemCount()-1){
                        MyAdapter adapter= (MyAdapter) recyclerView.getAdapter();
                        adapter.loadMore();
                    }
                }
            }
        });
        findViewInBottomBar();
        myHandler=new MyHandler();
        json_to_post(getIntent());
        loadPost();
    }

    private void findViewInBottomBar() {
        sendText=findViewById(R.id.forum_post_details_send_et);
        sendText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawable = sendText.getCompoundDrawables()[2];
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() > sendText.getWidth() - sendText.getPaddingRight()
                            - drawable.getIntrinsicWidth()) {
                        drawable.setTint(0xFFBBBBBB);
                        return true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getX() > sendText.getWidth() - sendText.getPaddingRight()
                            - drawable.getIntrinsicWidth()) {
                        drawable.setTint(Color.RED);

                        sendText(sendText.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void sendText(String content) {
        /*OKHttpUtil.uploadJSONs(Constant.HOST + "PostServlet", getPostJson(content), new ServerResponse() {
            @Override
            public void response(Call call, Response response) {
                try {
                    Log.d("post_response",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
        networkModelContext.post(null,getPostJson(content));
    }

    private String getPostJson(String content) {
        Post sendPost=new Post();
        sendPost.setUserId(app.currentUser.getUserId());
        sendPost.setReplyToWhom(topic_post.getPostId());
        sendPost.setContent(content);
        sendPost.setTitle("");
        sendPost.setLiveRoomId(topic_post.getLiveRoomId());
        if(c==null){
            c=Calendar.getInstance();
        }
        //Date date=new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH+1),c.get(Calendar.DAY_OF_MONTH));
        //Time time=new Time(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
        Date date=new Date(System.currentTimeMillis());
        Time time=new Time(System.currentTimeMillis());
        sendPost.setPostDate(date);
        sendPost.setPostTime(time);
        Gson gson=new Gson();
        return gson.toJson(sendPost);
    }

    private void json_to_post(Intent intent){
        post_str=intent.getStringExtra("topic_post");
        Gson gson=new Gson();
        topic_post=gson.fromJson(post_str,Post.class);
        roomName=intent.getStringExtra("roomName");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        json_to_post(intent);
    }

    private final int INIT_RECYCLER_VIEW=0;
    private final int LOAD_MORE=1;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==INIT_RECYCLER_VIEW){
                forumAdapter=new MyAdapter((ArrayList<Post>) msg.obj);
                recyclerView.setLayoutManager(new LinearLayoutManager(ActivityPostDetails.this));
                recyclerView.setAdapter(forumAdapter);
            }else if(msg.what==LOAD_MORE){
                ArrayList<Post> posts= (ArrayList<Post>) msg.obj;
                forumAdapter.getComment_topic_post().addAll(posts);
                forumAdapter.notifyItemRangeChanged(
                        forumAdapter.getItemCount()+1,posts.size(),"1");
            }
        }
    }

    private String getJson() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("pageNo",currentPage);
            jsonObject.put("pageSize",pageSize);
            jsonObject.put("replyToWhom",topic_post.getPostId());//代表这个帖子是回复哪个帖子的。0代表主题帖
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    private void loadPost() {
        OKHttpUtil.uploadJSONs(Constant.HOST + "PoserServlet", getJson(), new ServerResponse() {
            @Override
            public void response(Call call, Response response) {
                try {
                    String jsonFromServer=response.body().string();
                    Gson gson= JSONUtil.getGSON();
                    ArrayList<Post> posts=gson.fromJson(jsonFromServer,new TypeToken<ArrayList<Post>>(){}.getType());
                    if(posts!=null){
                        currentPage=currentPage+posts.size();
                    }
                    Message message=new Message();
                    message.obj=posts;
                    message.what=INIT_RECYCLER_VIEW;
                    Thread.sleep(1000);
                    myHandler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        /*networkModelContext.loadPost(new DataArrivedListener<Post>(){

            @Override
            public void dataArrived(List<Post> data_collection) {
                Log.d("onDataArrived",data_collection.size()+"");
            }

            @Override
            public void jsonArrived(JSONObject response) {

            }
        },getJson());*/
    }

    class MyAdapter extends RecyclerView.Adapter{

        private ArrayList<Post> comment_topic_post;
        private final int POSTER=0;
        private final int POST_COMMENTER=1;
        private RequestOptions options;


        public MyAdapter(ArrayList<Post> comment_topic_post){
            Log.d("MyAdapter",comment_topic_post.size()+"");
            this.comment_topic_post=comment_topic_post;
            options=RequestOptions.errorOf(R.mipmap.yukee);
        }

        public ArrayList<Post> getComment_topic_post(){
            return comment_topic_post;
        }

        @Override
        public int getItemViewType(int position) {
            if(position==0){
                return POSTER;
            }else{
                return POST_COMMENTER;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            ViewGroup itemView=null;
            if (viewType==POSTER){
                itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.activity_forum_post_details_poster,viewGroup,false);
            }else{
                itemView=(ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.video_view_main_forum_post,viewGroup,false);
            }
            return new MyViewHolder(itemView,viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            MyViewHolder myViewHolder= (MyViewHolder) viewHolder;
            if(i==0){
                if(c==null){
                    c = Calendar.getInstance();
                }
                c.setTime(topic_post.getPostDate());
                myViewHolder.time_poster.setText(c.get(Calendar.MONTH)+1 + "月" + c.get(Calendar.DAY_OF_MONTH) + "日");
                Glide.with(ActivityPostDetails.this).load(topic_post.getUser().
                        getPortrait()).into(myViewHolder.portrait_poster);
                myViewHolder.username_poster.setText(topic_post.getUser().getUsername());
                myViewHolder.content_poster.setText(topic_post.getContent());
                myViewHolder.title_poster.setText(topic_post.getTitle());
                myViewHolder.roomName_poster.setText("发布于"+roomName+"吧");
            }else{
                if(c==null){
                    c = Calendar.getInstance();
                }
                Post comment=comment_topic_post.get(i-1);
                c.setTime(comment.getPostDate());
                myViewHolder.time_poster.setText(c.get(Calendar.MONTH)+1 +
                        "月" + c.get(Calendar.DAY_OF_MONTH) + "日");
                if(comment.getUser().getPortrait()==null){
                    Glide.with(ActivityPostDetails.this).
                            load("").apply(options).into(myViewHolder.portrait_poster);
                }else {
                    Glide.with(ActivityPostDetails.this).
                            load(comment.getUser().getPortrait()).into(myViewHolder.portrait_poster);
                }
                myViewHolder.username_poster.setText(comment.getUser().getUsername());
                myViewHolder.content_poster.setText(comment.getContent());
            }
        }

        @Override
        public int getItemCount() {
            return comment_topic_post.size()+1;
        }

        public void loadMore() {
            OKHttpUtil.uploadJSONs(Constant.HOST + "PoserServlet", getJson(), new ServerResponse() {

                @Override
                public void response(Call call, Response response) {
                    try {
                        String jsonFromServer=response.body().string();
                        Gson gson=JSONUtil.getGSON();
                        ArrayList<Post> posts=gson.fromJson(jsonFromServer,
                                new TypeToken<ArrayList<Post>>(){}.getType());
                        if(posts!=null){
                            currentPage=currentPage+posts.size();
                        }
                        Message message=new Message();
                        message.obj=posts;
                        message.what=LOAD_MORE;
                        myHandler.sendMessage(message);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView username_poster,time_poster,content_poster,title_poster,roomName_poster;
            MyCircleImageView portrait_poster;

            public MyViewHolder(@NonNull View itemView,int viewType) {
                super(itemView);
                if(viewType==POSTER){
                    username_poster=itemView.findViewById(R.id.activity_forum_post_details_poster_username);
                    time_poster=itemView.findViewById(R.id.activity_forum_post_details_poster_time);
                    content_poster=itemView.findViewById(R.id.activity_forum_post_details_poster_content);
                    title_poster=itemView.findViewById(R.id.activity_forum_post_details_poster_title);
                    portrait_poster=itemView.findViewById(R.id.activity_forum_post_details_poster_portrait);
                    roomName_poster=itemView.findViewById(R.id.activity_forum_post_details_poster_room_name);
                }else{
                    content_poster=itemView.
                            findViewById(R.id.video_view_main_forum_post_title_tv);
                    portrait_poster=itemView.findViewById(R.id.video_view_main_forum_post_portrait);
                    username_poster=itemView.findViewById(R.id.video_view_main_forum_post_username);
                    time_poster=itemView.findViewById(R.id.video_view_main_forum_post_time);
                }
            }
        }
    }
}
