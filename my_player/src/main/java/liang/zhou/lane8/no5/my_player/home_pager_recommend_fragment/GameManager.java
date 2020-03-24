package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import java.util.List;

import liang.zhou.lane8.no5.my_player.dataResolve_model.ResolverContext;
import liang.zhou.lane8.no5.my_player.home_page_general_game_fragment.ListArrivedListener;

public class GameManager extends DataManager {

    public void fetchAllGames(ListArrivedListener<Game> listener){
        network.fetAllGames("FetchGameServlet",new DataResponse() {
            @Override
            public void onResponse(String jsonInString) {
                List<Game> games=null;
                listener.onListArrived(resolver.resolveToList(jsonInString,Game[].class));
            }
        });
    }

    @Override
    protected ResolverContext getResolver() {
        return new ResolverContext<Game>();
    }
}
