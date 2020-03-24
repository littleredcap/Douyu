package liang.zhou.lane8.no5.my_player.glide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

public class BaseNetworkLoader implements ModelLoader<GlideUrl, InputStream> {

    private OkHttpStreamFetcher fetcher;

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl glideUrl,
                                               int width, int height, @NonNull Options options) {
        if(fetcher==null){
            fetcher=new OkHttpStreamFetcher(glideUrl);
            fetcher.useOkHttp();
        }
        return new LoadData<>(glideUrl, fetcher);
    }

    @Override
    public boolean handles(@NonNull GlideUrl glideUrl) {
        return true;
    }

    @SuppressWarnings("WeakerAccess")
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {

        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new BaseNetworkLoader();
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}
