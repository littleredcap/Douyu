package liang.zhou.lane8.no5.my_player;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;

public class PostManager {

    public static PostManager postManager=null;
    private PostManager(){
        posts=new ArrayList<>();
        initPosts();
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
}
