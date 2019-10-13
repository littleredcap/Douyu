package liang.zhou.lane8.no5.my_player;

import java.util.LinkedList;

import liang.zhou.lane8.no5.my_business.data_model.User;

public class FansManager {

    private LinkedList<User> contributors;
    private static FansManager fansManager;
    private FansManager(){
        contributors=new LinkedList<>();
        createContributors();
    }

    public static FansManager getDanMuManger() {
        if (fansManager == null) {
            synchronized (DanMuManager.class) {
                if (fansManager == null) {
                    fansManager = new FansManager();
                }
            }
        }
        return fansManager;
    }

    public void update_daily_contribute_value(){
        int daily_con_value=0;
        for (User contributor:contributors) {
            contributor.setLast_daily_contribute_value(contributor.getDaily_contribute_value());
            daily_con_value=contributor.getDaily_contribute_value()+RandomTestData.updateContributeValue();
            if(daily_con_value<0){
                contributor.setDaily_contribute_value(0);
            }else {
                contributor.setDaily_contribute_value(daily_con_value);
            }
        }
    }

    private void createContributors(){
        for(int i=0;i<8;i++){
            User user=RandomTestData.getRandomUser();
            contributors.add(user);
        }
    }

    public LinkedList<User> getContributors(){
        return contributors;
    }
}
