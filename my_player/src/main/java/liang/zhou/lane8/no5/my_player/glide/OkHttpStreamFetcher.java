package liang.zhou.lane8.no5.my_player.glide;

import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.Synthetic;

import java.io.IOException;
import java.io.InputStream;

import liang.zhou.lane8.no5.my_player.common.Loader;
import liang.zhou.lane8.no5.my_player.common.ServerResponse;
import liang.zhou.lane8.no5.my_player.okhttp.OkHttpLoader;

public class OkHttpStreamFetcher implements DataFetcher<InputStream> {

    private Loader loader;
    private final GlideUrl url;
    @SuppressWarnings("WeakerAccess")
    @Synthetic
    InputStream stream;


    // Public API.
    @SuppressWarnings("WeakerAccess")
    public OkHttpStreamFetcher(GlideUrl url) {
        this.url = url;
    }

    public void useOkHttp(){
        loader=new OkHttpLoader();
    }

    @Override
    public void loadData(Priority priority, final DataCallback<? super InputStream> callback) {
        loader.load(url.getHeaders(),url.toStringUrl(), new ServerResponse<InputStream>() {
            @Override
            public void onFailure(@NonNull IOException e) {
                callback.onLoadFailed(e);
            }

            @Override
            public void onResponse(InputStream inputStream) {
                callback.onDataReady(inputStream);
            }
        });
    }

    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
    }

    @Override
    public void cancel() {
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
