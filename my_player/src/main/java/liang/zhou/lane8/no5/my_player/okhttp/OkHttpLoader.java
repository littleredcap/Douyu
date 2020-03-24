package liang.zhou.lane8.no5.my_player.okhttp;

import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import liang.zhou.lane8.no5.my_player.common.Loader;
import liang.zhou.lane8.no5.my_player.common.ServerResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpLoader implements Loader<InputStream>,Callback {

    private OkHttpClient client;
    private ResponseBody responseBody;
    private volatile Call call;
    private ServerResponse<InputStream> listener;
    private InputStream stream;

    public OkHttpLoader(){
        if(client==null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            client = builder.addInterceptor(new ProgressInterceptor()).build();
        }
    }

    @Override
    public void load(Map<String, String> header, String url, ServerResponse<InputStream> listener) {
        this.listener=listener;
        Request.Builder requestBuilder = new Request.Builder().url(url);
        for (Map.Entry<String, String> headerEntry : header.entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();

        call = client.newCall(request);

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            call.enqueue(this);
        } else {
            try {
                // Calling execute instead of enqueue is a workaround for #2355, where okhttp throws a
                // ClassCastException on O.
                onResponse(call, call.execute());
            } catch (IOException e) {
                onFailure(call, e);
            } catch (ClassCastException e) {
                // It's not clear that this catch is necessary, the error may only occur even on O if
                // enqueue is used.
                onFailure(call, new IOException("Workaround for framework bug on O", e));
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if(listener!=null){
            listener.onFailure(e);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(listener!=null){
            responseBody = response.body();
            if (response.isSuccessful()) {
                //long contentLength = Preconditions.checkNotNull(responseBody).contentLength();
                //long contentLength=responseBody.contentLength();
                //stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
                stream=responseBody.byteStream();
                listener.onResponse(stream);
            }
        }
    }
}
