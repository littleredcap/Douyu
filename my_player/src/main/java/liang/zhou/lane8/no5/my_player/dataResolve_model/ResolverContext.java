package liang.zhou.lane8.no5.my_player.dataResolve_model;

import java.util.List;

import liang.zhou.lane8.no5.my_player.gson.GSONConfig;

public class ResolverContext<T> {

    private Resolver<T> iResolver;

    public void useGSONResolver(){
        iResolver=new GSONConfig<T>();
    }
    public <R> List<R> resolveToList(String jsonInStr,Class<R[]> type){
        return iResolver.resolveToList(jsonInStr,type);
    }
}
