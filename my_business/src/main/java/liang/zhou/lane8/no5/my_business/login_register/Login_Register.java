package liang.zhou.lane8.no5.my_business.login_register;

import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_network.poster.MyPoster;

public abstract class Login_Register {

    protected MyPoster my_poster=new MyPoster();

    /**
     *
     * @param username
     * @param password
     * @param user
     * @return
     */
    public abstract String login(String username,String password,User user);
    protected abstract String register(User user);
}
