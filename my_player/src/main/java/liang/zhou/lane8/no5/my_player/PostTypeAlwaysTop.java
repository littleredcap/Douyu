package liang.zhou.lane8.no5.my_player;

import android.view.View;
import android.view.ViewGroup;

public class PostTypeAlwaysTop extends PostType {


    public PostTypeAlwaysTop(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    public void setupBaseInfoZone() {
        viewGroup.findViewById(R.id.video_view_main_forum_base_info_rl).setVisibility(View.GONE);
    }

    @Override
    public void setContentBody() {
        viewGroup.findViewById(R.id.video_view_main_forum_always_top_tv).setVisibility(View.VISIBLE);
    }

    @Override
    public void setInteractZone() {
        viewGroup.findViewById(R.id.video_view_main_forum_interact_rl).setVisibility(View.GONE);
    }
}
