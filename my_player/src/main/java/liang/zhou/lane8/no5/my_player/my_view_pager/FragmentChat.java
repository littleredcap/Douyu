package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.DoubleAccumulator;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.DanMu;
import liang.zhou.lane8.no5.my_player.DanMuManager;
import liang.zhou.lane8.no5.my_player.Global;
import liang.zhou.lane8.no5.my_player.MyApplication;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.barrage.BarrageConfig;
import liang.zhou.lane8.no5.my_player.barrage.BarrageManager;
import liang.zhou.lane8.no5.my_player.barrage.BarrageType;
import liang.zhou.lane8.no5.my_player.barrage.IBarrageServerResponse;
import liang.zhou.lane8.no5.my_player.ui.Utils;
import master.flame.danmaku.danmaku.model.Danmaku;
import master.flame.danmaku.ui.widget.DanmakuView;

public class FragmentChat extends FragmentBase {

    private RecyclerView recyclerView;
    private DanMuManager danMuManager;
    private EditText sendText;
    private User myself;
    private UpdateHandler updateHandler;
    private final int UPDATE_MSG=0;
    private HandlerThread handlerThread;
    private Handler sendDanMuHandler;
    private MyApplication ma;
    private BarrageManager barrageManager;
    private DanmakuView danmakuView;
    private BarrageConfig barrageConfig;
    private Gson gson;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private JsonObject jsonObject;
    private JsonParser parser;
    private ArrayList<HashMap> barrageMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ma= (MyApplication) getActivity().getApplication();
        ViewGroup viewGroup= (ViewGroup) inflater.inflate(R.layout.video_view_main_fragment,null);
        View view=inflater.inflate(R.layout.video_view_main_bottom_bar,viewGroup);
        myself=ma.currentUser;
        //findViewInBottomBar(view);
        danMuManager=DanMuManager.getDanMuManger();
        gson=new Gson();
        parser=new JsonParser();
        barrageMap=new ArrayList<>();
        //jsonReader=new JsonReader(new StringReader());

        danmakuView=getActivity().findViewById(R.id.danmakuView);
        barrageConfig= BarrageConfig.getConfig(danmakuView);
        barrageManager = new BarrageManager();
        barrageManager.useCustomBarrageImpl();
        barrageManager.connectToServer(Constant.barrage_server_ip,
                Constant.barrage_server_port, new IBarrageServerResponse() {
                    @Override
                    public void onServerConnectFail() {

                    }

                    @Override
                    public void onServerConnected() {
                        findViewInBottomBar(view);
                        /*send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String msg = "hello";
                                String json_str="userId,"+ma.currentUser.getUserId()+";barrage,"+msg+
                                        ";icon_url,https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578669749477&di=1d0c6f3ec1d16dfb28c4355669339a10&imgtype=0&src=http%3A%2F%2Fi1.sinaimg.cn%2Fent%2Fd%2F2008-06-04%2FU105P28T3D2048907F326DT20080604225106.jpg";
                                barrageManager.sendAsJson(JSONUtil.toJson(json_str));
                            }
                        });*/
                    }

                    @Override
                    public void onBarrageArrived(final String barrage, final BarrageType barrage_type) {
                        HashMap map=gson.fromJson(barrage,HashMap.class);
                        barrageMap.add(map);
                        updateHandler.sendEmptyMessage(UPDATE_MSG);
                        double userId= (double) map.get("userId");
                        String username= (String) map.get("userHash");
                        String content= (String) map.get("text");
                        String portraitUrl= (String) map.get("portraitUrl");
                        if(userId==Global.myself.getUserId()){
                            barrageConfig.drawMine(content, (int) userId,username).
                                    withTextColor(0xFFC0CB).withIcon(portraitUrl);
                        }else{
                            barrageConfig.drawOthers(content).withTextColor(0xffffff).
                                    withIcon(portraitUrl);
                        }

                        Log.d("onBarrageArrived",map.get("text").toString());
                    }

                    @Override
                    public void onServerDisconnected() {

                    }
                });

        handlerThread=new HandlerThread("sendMessage");
        handlerThread.start();
        sendDanMuHandler=new Handler(handlerThread.getLooper(),new MessageCallBack());
        //final MyRecyclerAdapter recyclerAdapter=new MyRecyclerAdapter(danMuManager.getDanMus());
        MyRecyclerAdapter recyclerAdapter=new MyRecyclerAdapter(barrageMap);
        recyclerView=viewGroup.findViewById(R.id.video_view_main_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(recyclerAdapter);
        updateHandler=new UpdateHandler(recyclerAdapter);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    danMuManager.fetchDanMu();
                    updateHandler.sendEmptyMessage(UPDATE_MSG);
                }
            }
        }).start();*/
        return viewGroup;
    }

    private void findViewInBottomBar(View view) {
        sendText=view.findViewById(R.id.video_view_main_bottom_bar_send_et);
        sendText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(myself==null){
                    //test();
                    popupLoginWindow();
                }else {
                    Drawable drawable = sendText.getCompoundDrawables()[2];
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getX() > sendText.getWidth() - sendText.getPaddingRight()
                                - drawable.getIntrinsicWidth()) {
                            drawable.setTint(0xFFBBBBBB);
                            return true;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (event.getX() > sendText.getWidth() - sendText.getPaddingRight()
                                - drawable.getIntrinsicWidth()) {
                            drawable.setTint(Color.RED);
                            //sendText(sendText.getText().toString());
                            String barrageContent=sendText.getText().toString();

                            jsonObject= (JsonObject) parser.parse(gson.toJson(barrageConfig.
                                    drawMine(barrageContent, Global.myself.getUserId(),
                                            Global.myself.getUsername()).current()));
                            jsonObject.addProperty("portraitUrl",Global.myself.getPortrait());

                            barrageManager.sendAsString(jsonObject.toString());
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    private void test() {
        myself=new User();
        myself.setUsername("倩哥");
        myself.setRank("皇");
    }

    private void popupLoginWindow() {
    }

    private void sendText(String content) {
        if(content!=null&&!content.equals("")) {
            DanMu dm = new DanMu();
            dm.setContent(content);
            dm.setTaker(myself);
            Message messageAboutDanMu=sendDanMuHandler.obtainMessage();
            messageAboutDanMu.obj=dm;
            sendDanMuHandler.sendMessage(messageAboutDanMu);
        }
        sendText.setText("");
        //updateHandler.sendEmptyMessage(UPDATE_MSG);
    }

    public class UpdateHandler extends Handler{

        private MyRecyclerAdapter adapter;

        public UpdateHandler(MyRecyclerAdapter adapter){
            this.adapter=adapter;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==UPDATE_MSG) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    class MessageCallBack implements Handler.Callback{

        @Override
        public boolean handleMessage(Message msg) {
            DanMu dm= (DanMu) msg.obj;
            Log.d("messageCallback",dm.getContent());
            danMuManager.sendDanMu(dm);
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (danmakuView != null) {
            // dont forget release!
            danmakuView.release();
            danmakuView = null;
        }
        barrageManager.disconnect();
    }
}
