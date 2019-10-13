package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.my_pager_grid_view.MyPagerGridView;
import liang.zhou.lane8.no5.my_player.ui.MyGridCardView;

public class RecommendAdapter extends RecyclerView.Adapter {

    private ArrayList<GameClass> classifies;
    private GridView gridView;
    private int currentPosition;
    private MyGridViewAdapter gridViewAdapter;
    private MyPagerGridView myPagerGridView;
    private RecyclerView recyclerView;
    private LinearLayoutManager lm;

    public RecommendAdapter(ArrayList<GameClass> classifies){
        this.classifies=new ArrayList<>();
        this.classifies.addAll(classifies);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        recyclerView= (RecyclerView) viewGroup;
        lm= (LinearLayoutManager) recyclerView.getLayoutManager();
        ViewGroup itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.home_page_recommend_recycler_view_item,
                        viewGroup,false);

        Log.d("view的实例化","MyPagerGridView");
        //initGridView(itemView);
        Log.d("onCreateViewHolder","access");
        return new MyViewHolder(itemView);
    }

    private void initGridView(ViewGroup recyclerViewGroup) {
        //gridView=recyclerViewGroup.findViewById(R.id.home_page_recommend_recycler_view_item_grid_view);
        Log.d("RecommendAdapter","currentPosition"+currentPosition);
        gridViewAdapter=new MyGridViewAdapter(classifies.get(currentPosition).getItems());
        //gridView.setAdapter(gridViewAdapter);
    }

    @Override
    public int getItemViewType(int position) {
        currentPosition=position;
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder myViewHolder= (MyViewHolder) viewHolder;
        myViewHolder.tv_class_label.setText(classifies.get(i).getClassName());

        ArrayList<Game> items=classifies.get(i).getItems();
        //myPagerGridView=new MyPagerGridView(,classifies.get(i).getItems());
        //myViewHolder.myPagerGridView.setHow_many_rows(1);
        Log.d("onBindViewInRecAdapter",items.size()+"into"+i);
        if(items.size()==0){
            myViewHolder.myPagerGridView.clear();
        }else{
            myViewHolder.myPagerGridView.setClassId(classifies.get(i).getClassId());
            myViewHolder.myPagerGridView.setGridViewItems(items);
        }
    }

    @Override
    public int getItemCount() {
        return classifies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_class_label;
        MyPagerGridView myPagerGridView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_class_label=itemView.findViewById(R.id.home_page_recommend_recycler_view_item_class_label);
            myPagerGridView=itemView.findViewById(R.id.home_page_recommend_recycler_view_item_class_game);
            Log.d("view的实例化","findViewById");
        }
    }
}
