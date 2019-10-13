package liang.zhou.lane8.no5.my_player;

import java.util.ArrayList;
import java.util.Random;

import liang.zhou.lane8.no5.my_business.data_model.User;

public class EventCommentManager {

    private ArrayList<EventComment> eventComments;
    private static EventCommentManager eventCommentManager = null;
    private Random random;

    private String userNames[]={"只系洪辰","陈钰琪_YuKee","赵敏"};
    private String contents[]={"我喜欢看倚天屠龙记，特别是陈钰琪版的",
            "陈钰琪版的赵敏看上去很娇小，脸小，身材也小。主要表现赵敏霸气的一面，很少可爱的一面",
            "赵敏长得漂亮，当然周芷若也不错，可能第一眼看上去不那么漂亮。"};

    private EventCommentManager() {
        eventComments = new ArrayList<>();
        initEventComment();
        random=new Random();
    }

    private void initEventComment() {
        for(int i=0;i<5;i++) {
            EventComment comment=new EventComment();
            comment.setContent("我是陈钰琪，我为自己代言");
            User user=new User();
            user.setUsername(userNames[(int)(Math.random()*3)]);
            user.setRank("");
            comment.setUser(user);
            eventComments.add(comment);
        }
    }

    public static EventCommentManager getEventCommentManager() {
        if (eventCommentManager == null) {
            synchronized (EventCommentManager.class) {
                if (eventCommentManager == null) {
                    eventCommentManager = new EventCommentManager();
                }
            }
        }
        return eventCommentManager;
    }

    public ArrayList<EventComment> getEventComments() {
        return eventComments;
    }

}
