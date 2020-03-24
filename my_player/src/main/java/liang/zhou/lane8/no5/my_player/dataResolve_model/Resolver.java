package liang.zhou.lane8.no5.my_player.dataResolve_model;

import java.util.List;

interface Resolver<T>{
    <R> List<R> resolveToList(String jsonInString,Class<R[]> type);
}
