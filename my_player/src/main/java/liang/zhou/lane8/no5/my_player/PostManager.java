package liang.zhou.lane8.no5.my_player;

import android.util.Log;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.dataResolve_model.ResolverContext;
import liang.zhou.lane8.no5.my_player.home_page_general_game_fragment.ListArrivedListener;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.DataManager;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;

public class PostManager extends DataManager {

    public static PostManager postManager=null;
    private PostManager(){
        posts=new ArrayList<>();
        initPosts();
    }

    @Override
    protected ResolverContext getResolver() {
        return new ResolverContext<Post>();
    }

    private void initPosts() {
        for(int i=0;i<8;i++){
            Post post=new Post();
            posts.add(post);
        }
    }

    private ArrayList<Post> posts;

    public static PostManager newPostManager(){
        if(postManager==null){
            synchronized (PostManager.class){
                if(postManager==null){
                    postManager=new PostManager();
                }
            }
        }
        return postManager;
    }

    public ArrayList<Post> getPosts(){
        return posts;
    }

    public void fetchPostByRoomId(int roomId, ListArrivedListener<Post> listener){
        String sql="select * from mypost where liveRoomId="+roomId;
        Log.d("PostManager",roomId+"");
        network.sendSQL(sql, "FetchPostServlet", new DataResponse() {
            @Override
            public void onResponse(String jsonInString) {
                listener.onListArrived(resolver.resolveToList(jsonInString,Post[].class));
            }
        });
    }
}
