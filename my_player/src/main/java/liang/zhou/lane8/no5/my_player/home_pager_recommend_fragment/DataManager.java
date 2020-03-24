package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import liang.zhou.lane8.no5.my_player.dataResolve_model.ResolverContext;
import liang.zhou.lane8.no5.my_player.network_model.NetworkModelContext;

public abstract class DataManager {

    public interface DataResponse{
        void onResponse(String jsonInString);
    }
    protected NetworkModelContext network;
    protected ResolverContext resolver;

    public DataManager(){
        network=new NetworkModelContext();
        network.useOKHttp();
        resolver=getResolver();
        resolver.useGSONResolver();
    }
    protected abstract ResolverContext getResolver();
}
