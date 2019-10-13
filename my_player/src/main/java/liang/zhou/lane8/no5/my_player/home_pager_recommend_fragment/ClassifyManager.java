package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.RandomTestData;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.Response;

public class ClassifyManager {

    private static ClassifyManager manager;
    private ClassifyManager(){
        gameClasses=new ArrayList<>();
    }

    public  void fetchClassify(GameClassFetchListener gameClassFetchListener) {
        //gameClasses=RandomTestData.createClassify();
        OKHttpUtil.uploadJson(Constant.HOST + "ClassifyServlet",
                -1, "", "", new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String json=response.body().string();

                            Gson gson=new Gson();
                            gameClasses = gson.fromJson(json,
                                    new TypeToken<ArrayList<GameClass>>() {}.getType());
                            //ArrayList<GameClass> gameClasses=gson.fromJson(json,ArrayList.class);
                            Log.d("initClassify",gameClasses.size()+"");
                            if(gameClassFetchListener!=null) {
                                gameClassFetchListener.onSuccess(gameClasses);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public interface GameClassFetchListener{
        public void onSuccess(ArrayList<GameClass> gameClasses);
    }

    private ArrayList<GameClass> gameClasses;

    public static ClassifyManager newInstance(){
        if(manager==null){
            synchronized (ClassifyManager.class){
                if(manager==null){
                    return manager=new ClassifyManager();
                }
            }
        }
        return manager;
    }

    public ArrayList<GameClass> getClassifies(){
        return gameClasses;
    }
}
