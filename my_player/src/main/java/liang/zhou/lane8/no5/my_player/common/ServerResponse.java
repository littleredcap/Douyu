package liang.zhou.lane8.no5.my_player.common;

import android.support.annotation.NonNull;

import java.io.IOException;

public interface ServerResponse<T> {

    void onFailure(@NonNull IOException e);
    void onResponse(T t);
}
