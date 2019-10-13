package liang.zhou.lane8.no5.my_player.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import cn.smssdk.gui.util.Const;
import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.Game;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class GameFetcher {

    private static GameFetcher fetcher=null;
    private GameFetcher(){}
    public static GameFetcher getFetcher(){
        if(fetcher==null){
            synchronized (GameFetcher.class){
                if(fetcher==null){
                    fetcher=new GameFetcher();
                }
            }
        }
        return fetcher;
    }

    public void setListener(GotGameListener listener) {
        this.listener = listener;
    }

    private GotGameListener listener;
    public void fetchGame(int classId,int howMany,GotGameListener listener){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("classId",classId);
            jsonObject.put("howMany",howMany);
        }catch (JSONException e){

        }
        OKHttpUtil.uploadJSONs(Constant.HOST + "FetchGameServlet", jsonObject.toString(), new ServerResponse() {
            @Override
            public void response(Call call, Response response) {
                try {
                    String gameJson=response.body().string();
                    Gson gson=new Gson();
                    ArrayList<Game> games=gson.fromJson(gameJson, new TypeToken<ArrayList<Game>>() {}.getType());
                    if(listener!=null) {
                        listener.gotGame(games);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void gotGamesCount(int classId,GotGameCountListener listener){

        OKHttpUtil.uploadJson(Constant.HOST + "FetchGameCount", -1,
                "classId", classId + "", new ServerResponse() {
            @Override
            public void response(Call call, Response response) {
                try {
                    String json=response.body().string();
                    int gameCount=0;
                    try {
                        JSONObject jsonObject=new JSONObject(json);
                        gameCount=jsonObject.getInt("gameCount");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(listener!=null){
                        listener.gameCount(gameCount);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public interface GotGameCountListener{
        public void gameCount(int howManyGames);
    }
    public interface GotGameListener{
        public void gotGame(ArrayList<Game> games);
    }
}
