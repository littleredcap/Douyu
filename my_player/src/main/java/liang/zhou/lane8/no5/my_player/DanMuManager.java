package liang.zhou.lane8.no5.my_player;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import liang.zhou.lane8.no5.my_business.data_model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class DanMuManager {

    private ArrayList<DanMu> danMus;
    private static DanMuManager danMuManager = null;
    private NewBarrageListener barrageListener;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private OkHttpClient okHttpClient;
    private WebSocket mWebSocket;
    private boolean is_barrage_server_connected = false;

    private String userNames[] = {"只系洪辰", "陈钰琪_YuKee", "赵敏"};
    private String contents[] = {"我喜欢看倚天屠龙记，特别是陈钰琪版的",
            "陈钰琪版的赵敏看上去很娇小，脸小，身材也小。主要表现赵敏霸气的一面，很少可爱的一面",
            "赵敏长得漂亮。"};

    private DanMuManager() {
        danMus = new ArrayList<>();
        //initWebSocket();

        connectToServer();
    }

    private void connectToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.88.107", 60001);
                    socket.setKeepAlive(true);
                    //socket.setSoTimeout(6000);
                    outputStream= new DataOutputStream(socket.getOutputStream());
                    inputStream = new DataInputStream(socket.getInputStream());
                    receiveBarrage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initWebSocket() {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.88.107:8081").build();
        okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                is_barrage_server_connected = true;
                mWebSocket = webSocket;
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                //encapsulateToBarrage(text);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }
        });
        okHttpClient.dispatcher().executorService().shutdown();
    }

    private DanMu encapsulateToBarrage(String text) {
        DanMu danMu = new DanMu();
        try {
            JSONObject jsonObject = new JSONObject(text);

            danMu.setContent(jsonObject.getString("content"));
            User barrage_taker = new User();
            barrage_taker.setUsername(jsonObject.getString("takerName"));
            danMu.setTaker(barrage_taker);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return danMu;
    }

    public static DanMuManager getDanMuManger() {
        if (danMuManager == null) {
            synchronized (DanMuManager.class) {
                if (danMuManager == null) {
                    danMuManager = new DanMuManager();
                }
            }
        }
        return danMuManager;
    }

    public ArrayList<DanMu> getDanMus() {
        synchronized (DanMuManager.class) {
            return danMus;
        }
    }

    public void sendDanMu(DanMu dm) {
        try {
            if (!socket.isConnected()) {
                socket.connect(new InetSocketAddress("192.168.88.107", 60001));
            }
            if (outputStream == null) {
                outputStream = new DataOutputStream(socket.getOutputStream());
            }
            Log.d("sendDanMu", dm.getContent());
            //outputStream=socket.getOutputStream();
            Log.d("sendDanMu", socket + "");
            Log.d("sendDanMu", socket.isClosed() + "");
            Log.d("sendDanMu", socket.isConnected() + "");
            JSONObject jsonObject = putToJson(dm);


            outputStream.writeUTF(jsonObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject putToJson(DanMu dm) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", dm.getContent());
            jsonObject.put("takerName", dm.getTaker().getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private String convertToJson(DanMu dm) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("barrage", dm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private void receiveBarrage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!socket.isConnected()||socket.isClosed()){
                        Log.d("receiveBarrage",socket.isConnected()+"");
                        Log.d("receiveBarrage",socket.isClosed()+"");
                        return;
                    }
                    Log.d("receiveBarrage","弹幕服务器连接成功");
                    String barrageInfo;
                    while ((barrageInfo = inputStream.readUTF()) != null) {
                        DanMu danMu = encapsulateToBarrage(barrageInfo);
                        Log.d("receiveBarrage",barrageInfo);
                        danMus.add(danMu);
                        if (barrageListener != null) {
                            barrageListener.onNewBarrage(danMu);
                        }
                    }
                } catch (IOException e) {

                }
            }
        }).start();

    }

    public void fetchDanMu() {
        synchronized (DanMuManager.class) {
            DanMu danmu = RandomTestData.getRandomDanMu();
            danMus.add(danmu);
            if (barrageListener != null) {
                barrageListener.onNewBarrage(danmu);
            }
        }
    }

    public void setBarrageListener(NewBarrageListener listener) {
        this.barrageListener = listener;
    }
}
