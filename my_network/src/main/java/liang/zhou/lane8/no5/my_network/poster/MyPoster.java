package liang.zhou.lane8.no5.my_network.poster;

import android.util.Log;

import liang.zhou.lane8.no5.my_network.Callback;

public class MyPoster {

    private Poster poster=new DefaultPoster();
    private String serverURL="";

    /**
     * This method has been in thread
     * @param data
     * @param callback
     */
    public void posterData(final byte data[], final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("MyPoster","onRun()");
                poster.post(data,callback,serverURL);
            }
        }).start();
    }
    public void setServerURL(String url){
        serverURL=url;
    }

}
