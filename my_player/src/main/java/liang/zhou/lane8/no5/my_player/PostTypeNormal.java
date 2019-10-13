package liang.zhou.lane8.no5.my_player;

import android.view.View;
import android.view.ViewGroup;

public class PostTypeNormal extends PostType {

    private ViewGroup viewGroup;

    public PostTypeNormal(ViewGroup viewGroup) {
        super(viewGroup);
    }


    private void initContentZone() {
    }

    private void initTitleZone() {
        viewGroup.findViewById(R.id.video_view_main_forum_delicacy_tv).setVisibility(View.GONE);
        viewGroup.findViewById(R.id.video_view_main_forum_vote_tv).setVisibility(View.GONE);
        viewGroup.findViewById(R.id.video_view_main_forum_award_tv).setVisibility(View.GONE);
    }

    @Override
    public void setupBaseInfoZone() {
        initTitleZone();
        initContentZone();
    }

    @Override
    public void setContentBody() {

    }

    @Override
    public void setInteractZone() {

    }
}
