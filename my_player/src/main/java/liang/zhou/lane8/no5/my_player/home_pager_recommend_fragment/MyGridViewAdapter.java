package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;

public class MyGridViewAdapter extends BaseAdapter {

    private ArrayList<Game> s;

    public MyGridViewAdapter(ArrayList<Game> a){
        this.s=a;
    }

    @Override
    public int getCount() {
        return s.size();
    }

    @Override
    public Object getItem(int position) {
        return s.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("getView1", s.size()+"");
        GridViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.home_page_recommend_recycler_view_item_grid_view,null);
            viewHolder=new GridViewHolder();
            viewHolder.gameName=convertView.findViewById(R.id.home_page_grid_view_game_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (GridViewHolder) convertView.getTag();
        }
        viewHolder.gameName.setText("陈钰琪");
        return convertView;
    }

    class GridViewHolder{
        TextView gameName;
    }
}
