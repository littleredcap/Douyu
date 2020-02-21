package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.Post;
import liang.zhou.lane8.no5.my_player.PostManager;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.business_utils.JSONUtil;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.RecommendInRecAdapter;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Response;

public class FragmentForum extends FragmentBase {

    private RecyclerView recyclerView;
    private MyForumAdapter forumAdapter;
    private PostManager postManager;
    private int currentPage=1;
    private int pageSize=5;
    private MyHandler myHandler;
    private String roomName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        roomName=bundle.getString("roomName");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) LayoutInflater.from(FragmentForum.this.getContext()).
                inflate(R.layout.video_view_main_fragment,null);
        postManager=PostManager.newPostManager();
        myHandler=new MyHandler();
        initRecyclerView(viewGroup);
        return viewGroup;
    }

    private void initRecyclerView(ViewGroup viewGroup) {
        recyclerView=viewGroup.findViewById(R.id.video_view_main_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(FragmentForum.this.getContext()));
        loadPost();
    }

    private void loadPost() {
        OKHttpUtil.uploadJSONs(Constant.HOST + "PoserServlet", getJson(), new ServerResponse() {
            @Override
            public void response(Call call, Response response) {
                try {
                    String jsonFromServer=response.body().string();
                    Gson gson= JSONUtil.getGSON();
                    ArrayList<Post> posts=gson.fromJson(jsonFromServer,new TypeToken<ArrayList<Post>>(){}.getType());
                    Message message=new Message();
                    message.obj=posts;
                    message.what=INIT_RECYCLER_VIEW;
                    myHandler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final int INIT_RECYCLER_VIEW=0;

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==INIT_RECYCLER_VIEW){
                forumAdapter=new MyForumAdapter((ArrayList<Post>) msg.obj,roomName);
                recyclerView.setAdapter(forumAdapter);
            }
        }
    }

    private String getJson() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("pageNo",currentPage);
            jsonObject.put("pageSize",pageSize);
            jsonObject.put("replyToWhom",0);//代表这个帖子是回复哪个帖子的。0代表主题帖
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
