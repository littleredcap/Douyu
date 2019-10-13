package liang.zhou.lane8.no5.my_player.home_page_general_game_fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.GameClass;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.LiveRoom;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.DividerItemDecoration;
import liang.zhou.lane8.no5.my_player.ui.Utils;
import okhttp3.Call;
import okhttp3.Response;

public class FragmentMatchPreview extends Fragment {

    private RecyclerView recyclerView;
    private MyHandler myHandler;
    private final int INIT_RECY_VIEW=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myHandler=new MyHandler();
        ViewGroup viewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_rec_general_game_live_fragment,null);
        initRecyclerView(viewGroup);
        return viewGroup;
    }

    private void initRecyclerView(ViewGroup viewGroup) {
        recyclerView=viewGroup.findViewById(R.id.home_page_live_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        OKHttpUtil.uploadJson(Constant.HOST+"FetchMatchServlet",-1,"howMany",
                6+"",new ServerResponse(){

                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer=response.body().string();
                            Gson gson=new Gson();
                            ArrayList<Match> matches = gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<Match>>() {}.getType());
                            Message message=myHandler.obtainMessage();
                            message.what=INIT_RECY_VIEW;
                            message.obj=matches;
                            myHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==INIT_RECY_VIEW){
                ArrayList<Match> matches= (ArrayList<Match>) msg.obj;
                //matches.add((Match) msg.obj);
                recyclerView.setAdapter(new MyAdapter(matches));
            }
        }
    }
    private class MyAdapter extends RecyclerView.Adapter{

        private ArrayList<Match> matches;
        private Calendar calendar;
        private final int ITEM_TYPE_BOTTOM_EMPTY=0;
        private final int ITEM_TYPE_NORMAL=1;

        public MyAdapter(ArrayList<Match> matches){
            Log.d("myAdapter constructor",matches.size()+"");
            this.matches=matches;
            calendar=Calendar.getInstance();
        }

        private ViewGroup createEmptyView(ViewGroup viewGroup) {
            LinearLayout l=new LinearLayout(viewGroup.getContext());
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.dp2px(viewGroup.getContext(),240));
            l.setLayoutParams(ll);
            return l;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ViewGroup itemView=null;
            if(i==ITEM_TYPE_BOTTOM_EMPTY){
                itemView=createEmptyView(viewGroup);
            }else {
                itemView = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.home_page_general_game_match_info, viewGroup, false);
            }

            return new MyViewHolder(itemView);
        }
        @Override
        public int getItemViewType(int position) {

            if(position==getItemCount()-1){
                return ITEM_TYPE_BOTTOM_EMPTY;
            }
            return ITEM_TYPE_NORMAL;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if(i==getItemCount()-1){
                return;
            }
            MyViewHolder myViewHolder= (MyViewHolder) viewHolder;
            Match m=matches.get(i);
            myViewHolder.tv_team_a.setText(m.getTeamA());
            myViewHolder.tv_team_b.setText(m.getTeamB());
            myViewHolder.tv_result.setText(m.getTeamAWin()+":"+m.getTeamBWin());
            calendar.setTime(m.getMatchDate());
            StringBuffer dateInSB=new StringBuffer();
            dateInSB.append(getDateTime(calendar.get(Calendar.MONTH)+1)).append("/").
                    append(getDateTime(calendar.get(Calendar.DAY_OF_MONTH))).
                    append(" ").append(getDateTime(m.getMatchTime().getHours())).append(":").
                    append(getDateTime(m.getMatchTime().getMinutes())).append(" ").append(m.getMatchName());
            myViewHolder.tv_date_time_name.setText(dateInSB.toString());
        }

        private String getDateTime(int param){
            if(param<10){
                return "0"+param;
            }else{
                return ""+param;
            }
        }

        @Override
        public int getItemCount() {
            return matches.size()+1;
        }

        private class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tv_team_a,tv_team_b,tv_result,tv_date_time_name,tv_play_record;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_date_time_name=itemView.findViewById(R.id.home_page_general_game_match_info_date_time_name);
                tv_play_record=itemView.findViewById(R.id.home_page_general_game_match_info_play_record);
                tv_result=itemView.findViewById(R.id.home_page_general_game_match_info_result);
                tv_team_a=itemView.findViewById(R.id.home_page_general_game_match_info_teamA);
                tv_team_b=itemView.findViewById(R.id.home_page_general_game_match_info_teamB);
            }
        }
    }
}
