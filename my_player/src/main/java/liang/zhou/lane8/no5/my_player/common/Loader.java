package liang.zhou.lane8.no5.my_player.common;

import java.util.Map;

public interface Loader<T> {
    void load(Map<String,String> header,String url,ServerResponse<T> listener);
}
