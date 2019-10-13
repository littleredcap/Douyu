package liang.zhou.lane8.no5.my_player;

import okhttp3.Call;
import okhttp3.Response;

public interface ServerResponse {
    public void response(Call call,Response response);
}
