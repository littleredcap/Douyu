package liang.zhou.lane8.no5.my_network.poster;

import org.json.JSONObject;

import liang.zhou.lane8.no5.my_network.Callback;

public abstract class Poster {

    protected abstract void post(byte postData[], Callback callback,String serverURL);

}
