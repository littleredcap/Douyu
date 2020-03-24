package liang.zhou.lane8.no5.my_player.gson;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import liang.zhou.lane8.no5.my_player.dataResolve_model.BaseResolver;

public class GSONConfig<T> extends BaseResolver<T> {

    private Gson gson;
    public GSONConfig(){
        gson=new Gson();
    }

    @Override
    public <R> List<R> resolveToList(String jsonInString,Class<R[]> type) {
        /*List<T> data = gson.fromJson(jsonInString,
                new TypeToken<ArrayList<T>>() {}.getType());*/
        R[] arr = gson.fromJson(jsonInString, type);
        List<R> l= Arrays.asList(arr);
        //Log.d("resolveToList",games.get(0).getGameName() );
        Log.d("resolveToList",jsonInString );
        //Log.d("resolveToList",l.get(0).getGameName());
        ArrayList<R> temp=new ArrayList<>(l);
        return temp;
    }
}
