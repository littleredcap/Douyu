package liang.zhou.lane8.no5.my_player.my_glide;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import liang.zhou.lane8.no5.my_player.common.ProgressListener;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ProgressInterceptor implements Interceptor {

    public static final Map<String, ProgressListener> LISTENER_MAP = new HashMap<>();

    public static void addListener(String url, ProgressListener listener) {
        LISTENER_MAP.put(url, listener);
    }

    public static void removeListener(String url) {
        LISTENER_MAP.remove(url);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request rq=chain.request();
        Response rp=chain.proceed(rq);
        ResponseBody body=rp.body();
        return rp.newBuilder().body(new ProgressBody(rq.url().toString(),body)).build();
    }
}
