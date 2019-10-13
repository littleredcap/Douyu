package liang.zhou.lane8.no5.my_player;

import android.view.ViewGroup;

public abstract class PostType {

    protected ViewGroup viewGroup;

    public PostType(ViewGroup viewGroup){
        this.viewGroup=viewGroup;
    }

    public abstract void setupBaseInfoZone();
    public abstract void setContentBody();
    public abstract void setInteractZone();
}
