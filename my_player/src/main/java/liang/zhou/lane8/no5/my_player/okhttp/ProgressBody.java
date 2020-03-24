package liang.zhou.lane8.no5.my_player.okhttp;

import java.io.IOException;

import liang.zhou.lane8.no5.my_player.common.ProgressListener;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressBody extends ResponseBody {

    private ResponseBody body;
    private BufferedSource source;
    private ProgressListener listener;

    public ProgressBody(String url, ResponseBody body){
        this.body=body;
        listener= ProgressInterceptor.LISTENER_MAP.get(url);
    }

    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    @Override
    public long contentLength() {
        return body.contentLength();
    }

    @Override
    public BufferedSource source() {
        if(source==null){
            source= Okio.buffer(new ProgressSource(body.source()));
        }
        return source;
    }

    private class ProgressSource extends ForwardingSource {

        long totalBytesRead = 0;

        int currentProgress;

        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            long fullLength = body.contentLength();
            if (bytesRead == -1) {
                totalBytesRead = fullLength;
            } else {
                totalBytesRead += bytesRead;
            }
            int progress = (int) (100f * totalBytesRead / fullLength);
            if (listener != null && progress != currentProgress) {
                listener.onProgressUpdate(progress);
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null;
            }

            currentProgress = progress;
            return bytesRead;
        }
    }
}
