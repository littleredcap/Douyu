package liang.zhou.lane8.no5.my_player.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;

public class MyGridCardView extends RelativeLayout {

    private GridView gridView;
    private ArrayList<GridView> gridViews;

    private int how_many_columns=2;
    private int how_many_rows;

    public MyGridCardView(Context context) {
        this(context,null);
    }

    public MyGridCardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyGridCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gridViews=new ArrayList<>();
        initGridViews();
        //initGridView();
        addView(gridView);
    }

    private void initGridViews() {
        for(int i=0;i<how_many_rows;i++){
            GridView gw=new GridView(this.getContext());
            MyGridCardViewAdapter adapter=new MyGridCardViewAdapter();
            gw.setAdapter(adapter);
            gw.setNumColumns(how_many_columns);
        }
    }

    private void how_many_rows(){

    }

    private void initGridView(){
        gridView=new GridView(this.getContext());
        gridView.setNumColumns(how_many_columns);
        gridView.setAdapter(new MyGridCardViewAdapter());
    }

    private class MyGridCardViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridCardViewHolder viewHolder;
            if(convertView==null){
                convertView= LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.home_page_recommend_in_recommend_card_view,null);
                viewHolder=new GridCardViewHolder();
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (GridCardViewHolder) convertView.getTag();
            }
            return convertView;
        }

        class GridCardViewHolder{
            TextView gameName;
        }
    }
}
