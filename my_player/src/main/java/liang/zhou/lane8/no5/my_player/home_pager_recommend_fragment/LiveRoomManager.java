package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import liang.zhou.lane8.no5.my_player.dataResolve_model.ResolverContext;
import liang.zhou.lane8.no5.my_player.home_page_general_game_fragment.ListArrivedListener;

public class LiveRoomManager extends DataManager{

    public LiveRoomManager(){
        super();
    }

    @Override
    protected ResolverContext getResolver() {
        return new ResolverContext();
    }

    public void fetchLiveRoomByGameId(int gameId, ListArrivedListener<LiveRoom> listener){
        String sql="select * from liveroom where gameId="+gameId;
        network.sendSQL(sql,"FetchLiveRoomBySQL",new DataResponse(){

            @Override
            public void onResponse(String json) {
                List<LiveRoom> rooms;
                rooms=resolver.resolveToList(json,LiveRoom[].class);
                Log.d("LiveRoomManager",rooms.toString());
                listener.onListArrived(rooms);
            }
        });
    }
}
