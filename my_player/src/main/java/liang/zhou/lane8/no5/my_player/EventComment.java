package liang.zhou.lane8.no5.my_player;

import liang.zhou.lane8.no5.my_business.data_model.User;

public class EventComment {

    private User user;
    private String content;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
