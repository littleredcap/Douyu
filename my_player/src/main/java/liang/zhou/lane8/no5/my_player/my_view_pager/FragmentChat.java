package liang.zhou.lane8.no5.my_player.my_view_pager;

import android.app.Activity;
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

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.DanMu;
import liang.zhou.lane8.no5.my_player.DanMuManager;
import liang.zhou.lane8.no5.my_player.MyApplication;
import liang.zhou.lane8.no5.my_player.R;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ma= (MyApplication) getActivity().getApplication();
        ViewGroup viewGroup= (ViewGroup) inflater.inflate(R.layout.video_view_main_fragment,null);
        View view=inflater.inflate(R.layout.video_view_main_bottom_bar,viewGroup);
        myself=ma.currentUser;
        findViewInBottomBar(view);

        danMuManager=DanMuManager.getDanMuManger();
        handlerThread=new HandlerThread("sendMessage");
        handlerThread.start();
        sendDanMuHandler=new Handler(handlerThread.getLooper(),new MessageCallBack());
        final MyRecyclerAdapter recyclerAdapter=new MyRecyclerAdapter(danMuManager.getDanMus());
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
                            sendText(sendText.getText().toString());
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
}
