package liang.zhou.lane8.no5.my_player;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import liang.zhou.lane8.no5.my_business.data_model.User;

public class AnchorEventManager {

    private ArrayList<AnchorEvent> anchorEvents;
    private static AnchorEventManager anchorEventManager = null;
    private Random random;

    private String userNames[]={"只系洪辰","陈钰琪_YuKee","赵敏"};
    private String contents[]={"我喜欢看倚天屠龙记，特别是陈钰琪版的",
            "陈钰琪版的赵敏看上去很娇小，脸小，身材也小。主要表现赵敏霸气的一面，很少可爱的一面",
            "赵敏长得漂亮，当然周芷若也不错，可能第一眼看上去不那么漂亮。"};

    private AnchorEventManager() {
        Log.d("AnchorEventManager","access");
        if(anchorEvents==null) {
            anchorEvents = new ArrayList<>();
        }
        initAnchorEvents();
        random=new Random();
    }

    private void initAnchorEvents() {
        Anchor anchor=new Anchor();
        anchor.setAnchorName("陈钰琪_YuKee");
        for(int i=0;i<3;i++){
            AnchorEvent ae=new AnchorEvent();
            ae.setAnchor(anchor);
            ae.setContent("今天倚天屠龙记更新了，大家记得收看敏敏！");
            ae.setDate("04-03");
            anchorEvents.add(ae);
        }
    }

    public static AnchorEventManager getAnchorEventManager() {
        if (anchorEventManager == null) {
            synchronized (AnchorEventManager.class) {
                if (anchorEventManager == null) {
                    anchorEventManager = new AnchorEventManager();
                }
            }
        }
        return anchorEventManager;
    }

    public ArrayList<AnchorEvent> getAnchorEvents() {
        return anchorEvents;
    }



}
