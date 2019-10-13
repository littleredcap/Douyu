package liang.zhou.lane8.no5.my_player;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.Classify;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.Game;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.GameClass;

public class RandomTestData {

    private static String userNames[]={"只系洪辰","陈钰琪_YuKee","赵敏"};
    private static String contents[]={"我喜欢看倚天屠龙记，特别是陈钰琪版的",
            "陈钰琪版的赵敏看上去很娇小，脸小，身材也小。主要表现赵敏霸气的一面，很少可爱的一面",
            "赵敏长得漂亮，当然周芷若也不错，可能第一眼看上去不那么漂亮。"};

    public static User getRandomUser(){
        User user = new User();
        user.setRank("" +(int)(Math.random()*50));
        user.setUsername(userNames[(int)(Math.random()*3)]);
        user.setDaily_contribute_value((int)(Math.random()*6000));
        return user;
    }

    public static DanMu getRandomDanMu(){
        DanMu danmu = new DanMu();
        danmu.setContent(contents[(int)(Math.random()*3)]);
        danmu.setTaker(getRandomUser());
        return danmu;
    }

    public static int updateContributeValue(){
        return (int)(6000-Math.random()*12000);
    }

    public static ArrayList<GameClass> createClassify(){
        ArrayList<GameClass> classifies=new ArrayList<>();
        for(int i=0;i<6;i++) {
            ArrayList<Game> games=new ArrayList<>();
            GameClass classify = new GameClass();
            classify.setClassName("类别"+i);
            classifies.add(classify);
            classify.setItems(games);
            for(int j=0;j<13;j++) {
                Game game=new Game();
                game.setGameName("游戏"+j);
                games.add(game);
            }
        }
        return classifies;
    }

    public static int barrageMoveSpeed(){
        return 0;
    }
}
