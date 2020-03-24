package liang.zhou.lane8.no5.my_player.dataResolve_model;

import java.util.List;

public class BaseResolver<T> implements Resolver<T> {
    @Override
    public <R> List<R> resolveToList(String jsonInString,Class<R[]> type) {
        return null;
    }
}
