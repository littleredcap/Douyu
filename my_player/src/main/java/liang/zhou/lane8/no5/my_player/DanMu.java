package liang.zhou.lane8.no5.my_player;

import liang.zhou.lane8.no5.my_business.data_model.User;

public class DanMu {
    private String content;
    private User taker;
    public BarrageCharacteristic barrageChar;

    public DanMu(){
        barrageChar=new BarrageCharacteristic();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getTaker() {
        return taker;
    }

    public void setTaker(User taker) {
        this.taker = taker;
    }

    public class BarrageCharacteristic{
        public int width;
        public int height;
        public int moveSpeed;
        public int current_x;
        public int current_y;
    }
}
