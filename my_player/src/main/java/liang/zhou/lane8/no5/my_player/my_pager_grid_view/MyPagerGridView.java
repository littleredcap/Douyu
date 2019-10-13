package liang.zhou.lane8.no5.my_player.my_pager_grid_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment.Game;
import liang.zhou.lane8.no5.my_player.ui.GameFetcher;
import liang.zhou.lane8.no5.my_player.ui.Utils;
import okhttp3.OkHttpClient;

public class MyPagerGridView extends RelativeLayout {

    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    private int how_many_rows;
    private int how_many_rows_total = 7;//所有page加起来总行数
    private int how_many_columns = 4;
    private int how_many_pages;
    private static final int INIT_VIEW_PAGER=0;



    private int classId;

    private int currentPage = 1;//代表当前第几页
    private int showingRows = 0;//已显示的行数
    private GameFetcher gameFetcher;

    private ArrayList<Game> itemList;
    private ArrayList<GridView> gridViews;

    private ArrayList<LinearLayout> gridViewLayouts;

    private Context ctx;

    private LinearLayout.LayoutParams my_params;

    private HandlerThread handlerThread;
    private Handler fetchGameHandler;
    private MyHandler myHandler;


    private ArrayList<View> indicators;
    private boolean isViewPagerFirstLoad = true;

    public MyPagerGridView(Context context) {
        this(context, null);
    }

    public MyPagerGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPagerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ctx = context;
        gridViews = new ArrayList<>();
        gridViewLayouts = new ArrayList<>();
        indicators = new ArrayList<>();
        gameFetcher = GameFetcher.getFetcher();
        myHandler=new MyHandler();
        setPadding(0,0,0,Utils.dp2px(ctx,6));
        //initData();

    }

    public void clear(){
        removeAllViews();
        gridViewLayouts.clear();
    }

    private void init() {
        totalRows(itemList.size());
        //initMyself();
        if (itemList.size() > 3 * 4) {
            calculateRows(itemList.subList(0, 12).size());
        } else {
            calculateRows(itemList.size());
        }

        Log.d("myPagerGridView", how_many_rows + "");
        createViewPagerLayoutInner();
        initViewPager();
        //initGridView();
        //createIndicator();
        addToMe();
    }

    public void setGridViewItems(ArrayList list) {
        this.itemList = list;
        if (itemList == null || itemList.size() == 0) {
            return;
        }

        init();
    }

    public void setHow_many_rows(int rows) {
        this.how_many_rows = rows;
        refresh();
    }

    public void setHow_many_columns(int columns) {
        this.how_many_columns = columns;
        refresh();
    }

    private void calculateRows(int size) {
        Log.d("calculateRows", size + "");
        if (size % how_many_columns > 0) {
            how_many_rows = size / how_many_columns + 1;
            if (how_many_rows > 3) {
                how_many_rows = 3;
            }
        } else {
            how_many_rows = size / how_many_columns;
        }
    }

    private void refresh() {
        viewPager.removeAllViews();
        viewPager.setAdapter(null);
        gridViews.clear();
        initViewPager();
        //initGridView(how_many_rows,how_many_columns);
    }

    private void initMyself() {
        my_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                how_many_rows*Utils.dp2px(ctx,80));
        setLayoutParams(my_params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                if(itemList!=null){
                    height=how_many_rows*Utils.dp2px(ctx,85);
                }
                break;
            }
        }
        //height=Utils.dp2px(ctx,250);
        setMeasuredDimension(width,height);
    }

    private void initData() {
        itemList = new ArrayList();
        for (int i = 0; i < 18; i++) {
            Integer integer = new Integer(i);
            //itemList.add(integer);
        }
    }
    public void setClassId(int classId) {
        this.classId = classId;
        //initViewPager();
    }

    private void totalRows(int size) {
        if (size % how_many_columns > 0) {
            how_many_rows_total = size / how_many_columns + 1;
        } else {
            how_many_rows = size / how_many_columns;
        }
    }

    private void addToMe() {
        addView(viewPager);
    }

    /**
     * 每一行代表一个gridView，有多少行就有多少个girdView。
     * 翻到下一页才初始化下一页的gridView
     */
    //private ArrayList<LinearLayout> gridViewLayouts=new ArrayList<>();
    public void initGridView(LinearLayout gridViewContainer, int how_many_rows, int how_many_columns) {
        for (int i = 0; i < how_many_rows; i++) {
            GridView gridView = new GridView(this.getContext());
            GridViewAdapter gridViewAdapter = new GridViewAdapter(i);//每一行都创建一个adapter，也即意味着i为行数
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(0, Utils.dp2px(ctx, 20), 0, 0);
            gridView.setLayoutParams(lp);
            /*if(i==how_many_rows-1){
                how_many_columns=itemList.size()-how_many_columns*i;
            }*/
            gridView.setNumColumns(how_many_columns);
            gridView.setAdapter(gridViewAdapter);
            gridViewContainer.addView(gridView);
            gridViews.add(gridView);
        }
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        *//*if(heightMode==MeasureSpec.AT_MOST) {
            height=Utils.dp2px(ctx, 500);
        }*//*
        setMeasuredDimension(width,Utils.dp2px(ctx, 500));
    }*/

    public void initGridView(LinearLayout gridViewContainer, int how_many_columns, ArrayList<Game> gamesPerPage) {
            GridView gridView = new GridView(this.getContext());
            GridViewAdapter gridViewAdapter = new GridViewAdapter(gamesPerPage);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setMargins(0, Utils.dp2px(ctx, 15), 0, 0);
            gridView.setLayoutParams(lp);
            gridView.setNumColumns(how_many_columns);
            gridView.setVerticalSpacing(Utils.dp2px(ctx,15));
            gridView.setAdapter(gridViewAdapter);
            gridViewContainer.addView(gridView);
            gridViews.add(gridView);

    }

    private void createIndicator() {
        Log.d("createIndicator", "access");
        LinearLayout indicator_ll = new LinearLayout(ctx);
        indicator_ll.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams indicator_rl_lp = new RelativeLayout.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        indicator_ll.setLayoutParams(indicator_rl_lp);
        for (int i = 0; i < how_many_pages; i++) {
            View indicator = new View(ctx);
            indicators.add(indicator);
            indicator.setBackgroundColor(0xffC4C4C4);
            ViewGroup.LayoutParams lp = new LayoutParams(Utils.dp2px(ctx, 7), Utils.dp2px(ctx, 4));
            ((LayoutParams) lp).setMargins(0, 0, Utils.dp2px(ctx, 5), 0);
            indicator.setLayoutParams(lp);
            indicator_ll.addView(indicator);
        }
        indicator_rl_lp.setMargins(0,how_many_rows*(Utils.dp2px(ctx,60)+
                Utils.dp2px(ctx,15)), 0,0);
        indicator_rl_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        indicator_rl_lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        addView(indicator_ll);
    }

    private void setColorOfSelectedIndicator(View view) {
        view.setBackgroundColor(0xff8B8878);
    }

    private void setColorOfUnselectedIndicator(View view) {
        view.setBackgroundColor(0xffC4C4C4);
    }

    private void calculatePages() {
        gameFetcher.gotGamesCount(classId, new GameFetcher.GotGameCountListener() {
            @Override
            public void gameCount(int howManyGames) {
                Log.d("gotGamesCount", howManyGames + "");

                if(howManyGames==0){
                    return;
                }
                int reminder = howManyGames % (4 * 3);
                int quotient = howManyGames / (4 * 3);
                Log.d("initViewPager", reminder+","+quotient);
                if (reminder > 0) {
                    how_many_pages = quotient + 1;
                } else {
                    how_many_pages = quotient;
                }
                myHandler.sendEmptyMessage(INIT_VIEW_PAGER);

            }
        });
    }
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==INIT_VIEW_PAGER){
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                        /*if (isViewPagerFirstLoad) {
                            isViewPagerFirstLoad = false;
                            gameFetcher.fetchGame(classId, 12, new GameFetcher.GotGameListener() {

                                @Override
                                public void gotGame(ArrayList<Game> games) {
                                    Log.d("initViewPager", games.size() + "");
                                }
                            });
                        }*/
                    }

                    @Override
                    public void onPageSelected(int i) {
                        for (int index = 0; index < indicators.size(); index++) {
                            if (index == i) {
                                indicators.get(index).setBackgroundColor(0xff8B8878);
                            } else {
                                indicators.get(index).setBackgroundColor(0xffC4C4C4);
                            }
                        }
                        /*Message message = fetchGameHandler.obtainMessage();
                        message.arg1 = i;
                        fetchGameHandler.sendMessage(message);*/
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });
                createIndicator();
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(0);
                setColorOfSelectedIndicator(indicators.get(0));
            }
        }
    }

    private LinearLayout createViewPagerLayoutInner() {
        LinearLayout gridViewLayout = new LinearLayout(ctx);
        gridViewLayout.setOrientation(LinearLayout.VERTICAL);
        //gridViewLayout.setBackgroundColor(Color.BLUE);
        LinearLayout.LayoutParams llParams = new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        gridViewLayout.setLayoutParams(llParams);
        return gridViewLayout;
    }

    private void initViewPager() {
        viewPager = new ViewPager(ctx);
        pagerAdapter = new ViewPagerAdapter();
        calculatePages();
    }

    public class FetchGameCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    }

    private class GridViewAdapter extends BaseAdapter {

        private int index;//代表第几行
        private ArrayList<Game> gamesPerPage;
        private RequestOptions options;

        public GridViewAdapter(int index) {
            this.index = index;
        }

        public GridViewAdapter(ArrayList<Game> gamesPerPage){
            this.gamesPerPage=gamesPerPage;
            options=RequestOptions.circleCropTransform();
        }

        @Override
        public int getCount() {
            return gamesPerPage.size();
        }

        @Override
        public Object getItem(int position) {
            Log.d("getItem", index * how_many_columns + position + "");
            return gamesPerPage.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * @param position    代表每行有多少个
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            position = index * 4 + position;
            GridViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).
                        inflate(R.layout.home_page_recommend_recycler_view_item_grid_view, null);
                viewHolder = new GridViewHolder();
                viewHolder.tv_game_name = convertView.findViewById(R.id.home_page_grid_view_game_name);
                viewHolder.iv_icon=convertView.findViewById(R.id.home_page_grid_view_game_icon);
                convertView.setTag(viewHolder);
            }
            viewHolder = (GridViewHolder) convertView.getTag();
            viewHolder.tv_game_name.setVisibility(VISIBLE);
            viewHolder.iv_icon.setVisibility(VISIBLE);
            Glide.with(ctx).load(gamesPerPage.get(position).getGameIconUrl()).
                    apply(options).into(viewHolder.iv_icon);
            Log.d("getViewInGridView", gamesPerPage.get(position).getGameName());
            viewHolder.tv_game_name.setText(gamesPerPage.get(position).getGameName());
            return convertView;
        }


        class GridViewHolder {
            TextView tv_game_name;
            ImageView iv_icon;
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private LinkedList<Game> items_in_one_grid;
        private ArrayList<Game> items_last;


        public ViewPagerAdapter() {
            items_last = new ArrayList<>();
            //items_in_one_grid = new ArrayList<>();

            items_last.addAll(itemList);
            //items_in_one_grid.addAll(itemList);
        }

        private ArrayList<Game> subList(ArrayList<Game> original,int start,int end){
            ArrayList<Game> sub=new ArrayList<>();
            for(int i=start;i<end;i++){
                sub.add(original.get(i));
            }
            return sub;
        }
        private void last(ArrayList<Game> original,int start,int end){
            items_last.clear();
            for(int i=end;i<original.size();i++){
                items_last.add(original.get(i));
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //GridView gridView=gridViews.get(position);
            Log.d("instantiateItem", "position," + position);
            Log.d("instantiateItem", "size," + items_last.size());

            if(gridViewLayouts.size()<getCount()){
                LinearLayout gridViewContainer=createViewPagerLayoutInner();
                gridViewLayouts.add(position,gridViewContainer);
                int size=items_last.size();
                if(size<=12){
                    initGridView(gridViewContainer,4, items_last);
                }else{
                    ArrayList<Game> onePage= subList(itemList,position*12,position*12+12);
                    initGridView(gridViewContainer,4,onePage);
                    last(itemList,position*12,position*12+12);
                }
                if (container.getChildAt(position)==null) {
                    container.addView(gridViewContainer);
                }
            }
            return gridViewLayouts.get(position);
        }

        @Override
        public int getCount() {
            return how_many_pages;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            gridViewLayouts.clear();
            gridViewLayouts.remove(position);
            container.removeViewAt(position);
        }
    }
}
