package liang.zhou.lane8.no5.my_player;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import liang.zhou.lane8.no5.my_player.ui.Utils;

public class FragmentDomestic extends Fragment {


    private RecyclerView recyclerView;
    private Country china;

    public void setListener(AddressSelectedListener listener) {
        this.listener = listener;
    }

    private AddressSelectedListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final ViewGroup viewGroup = (ViewGroup) inflater.
                inflate(R.layout.home_page_view_pager_recommend_recycler_view, null);
        recyclerView = viewGroup.findViewById(R.id.home_page_view_pager_recommend_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(new MyAdapter(china.provinces));
        Paint paint = new Paint();
        Paint header_paint = new Paint();
        header_paint.setColor(0xffEAEAEA);
        //header_paint.setColor(Color.GRAY);
        header_paint.setStyle(Paint.Style.FILL);
        Rect head_rect = new Rect();
        paint.setTextSize(Utils.sp2px(this.getContext(), 10));
        paint.setAntiAlias(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int centerY;

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                /*if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = Utils.dp2px(viewGroup.getContext(), 20);
                } else if (china.provinces[parent.getChildAdapterPosition(view)].type
                        != china.provinces[parent.getChildAdapterPosition(view) - 1].type) {
                    outRect.top = Utils.dp2px(viewGroup.getContext(), 20);
                }*/
                if (parent.getChildAdapterPosition(view) == 0 || china.provinces[parent.getChildAdapterPosition(view)].type
                        != china.provinces[parent.getChildAdapterPosition(view) - 1].type) {
                    outRect.top = Utils.dp2px(viewGroup.getContext(), 20);
                }
                centerY = outRect.centerY();
                outRect.bottom = 1;
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);

                LinearLayoutManager ll = (LinearLayoutManager) parent.getLayoutManager();
                int position=ll.findFirstVisibleItemPosition();
                View item = parent.findViewHolderForAdapterPosition(position).itemView;
                boolean flag = false;
                head_rect.set(0, 0, parent.getRight(), Utils.dp2px(viewGroup.getContext(), 20));
                if(china.provinces[position].type!=china.provinces[position+1].type) {
                    if (item.getBottom()< head_rect.bottom) {
                        c.save();
                        flag = true;
                        //随着view的向上滚动，top会越来越小
                        c.translate(0, item.getHeight() + item.getTop() - head_rect.height());
                    }
                }
                c.drawRect(head_rect, header_paint);
                c.drawText(china.provinces[position].typeName,Utils.dp2px(parent.getContext(), 12),
                        head_rect.centerY() + Utils.dp2px(viewGroup.getContext(), 4), paint);
                if (flag) {
                    c.restore();
                }


            }

            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(view);
                    if (position == 0 || china.provinces[position].type != china.provinces[position - 1].type) {
                        c.drawText(china.provinces[position].typeName, Utils.dp2px(parent.getContext(),
                                12), view.getTop() - Utils.dp2px(parent.getContext(), 6), paint);
                    }
                }
            }
        });
        return viewGroup;
    }


    private String provinces_str[] = {"北京", "上海", "天津", "重庆", "香港", "澳门", "广东", "湖南", "黑龙江",
            "吉林", "山东", "海南", "四川", "福建", "河北", "浙江", "辽宁", "贵州", "青海", "台湾", "安徽"};
    private String gd_city[] = {"广州", "深圳", "珠海", "汕头", "佛山"};
    private final int PROVINCE_TYPE_municipality = 0;
    private final int PROVINCE_TYPE_province = 1;
    private final int PROVINCE_TYPE_special_dis = 2;

    private void initList() {
        china = new Country(provinces_str.length);
        for (int i = 0; i < provinces_str.length; i++) {
            Country.Province province = null;
            if (provinces_str[i].equals("北京") || provinces_str[i].equals("上海") || provinces_str[i].equals("天津") ||
                    provinces_str[i].equals("重庆")) {
                province = china.new Province(0);
                province.type = PROVINCE_TYPE_municipality;//直辖市
                province.typeName = "直辖市";
                province.name = provinces_str[i];

            } else if (provinces_str[i].equals("香港") || provinces_str[i].equals("澳门")) {
                province = china.new Province(0);
                province.type = PROVINCE_TYPE_special_dis;//特别行政区
                province.typeName = "特别行政区";
                province.name = provinces_str[i];
            } else {
                province = china.new Province(0);

                if (provinces_str[i].equals("广东")) {
                    province = china.new Province(gd_city.length);
                    for (int j = 0; j < gd_city.length; j++) {
                        province.city = gd_city;
                    }
                }
                province.name = provinces_str[i];
                province.typeName = "省";
                province.type = PROVINCE_TYPE_province;//省
            }
            china.provinces[i] = province;
        }
        Log.d("initList", china.provinces[4].name);
    }


    class MyAdapter extends RecyclerView.Adapter {

        private Country.Province[] provinces;
        private final int VIEW_TYPE_LABEL = 0;
        private final int VIEW_TYPE_ITEM = 1;

        public MyAdapter(Country.Province[] provinces) {
            this.provinces = provinces;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            ViewGroup itemView = (ViewGroup) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_address_picker_recycler_item, viewGroup, false);
            return new MyViewHolder(itemView, viewType);
        }

        @Override
        public int getItemViewType(int position) {
            /*if (position==0&&getItemCount()>0){
                labelCount++;
                return VIEW_TYPE_LABEL;
            }else if((position<getItemCount()-1)&&position>1){
                if(provinces[position-labelCount].type!=provinces[position-labelCount-1].type){
                    return  VIEW_TYPE_LABEL;
                }
            }*/
            return VIEW_TYPE_ITEM;
        }

        private void createLabel(MyViewHolder viewHolder, int position) {
            viewHolder.province_tv.setText(provinces[position].typeName);
            viewHolder.province_tv.setTextSize(13);
            viewHolder.forward_iv.setVisibility(View.GONE);
        }

        private int labelCount = 0;

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int itemPosition) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            /*if(itemPosition==0&&getItemCount()>0){
                createLabel(myViewHolder,itemPosition);
                return;
            }
            if(itemPosition>1&&(itemPosition<getItemCount()-1)){
                if(provinces[itemPosition-labelCount].type !=provinces[itemPosition-labelCount-1].type){
                    createLabel(myViewHolder,itemPosition-labelCount);
                    labelCount++;
                    return;
                }
            }*/
            //final int position=itemPosition-labelCount;
            int position = itemPosition;
            myViewHolder.province_tv.setText(provinces[position].name);
            if (provinces[position].city == null || provinces[position].city.length == 0) {
                myViewHolder.forward_iv.setVisibility(View.GONE);
                myViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            Intent intent = new Intent(getActivity(), ActivityInfo.class);
                            /*if(listener!=null) {
                                listener.onAddressSelected(provinces[position].name, position);
                            }*/
                            Bundle bundle = new Bundle();
                            bundle.putString("address", provinces[position].name);
                            bundle.putInt("itemPosition", position);
                            intent.putExtra("addressBundle", bundle);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
            } else {
                myViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Log.d("city_not_null", Arrays.toString(provinces[position].city));
                            Log.d("city_not_null", getActivity().toString());
                            listener.onAddressSelected(provinces[position].name, itemPosition, provinces[position].city);
                        }
                        return true;
                    }
                });
            }

        }

        private int totalLabelCount = 2;

        @Override
        public int getItemCount() {
            //return provinces.length+totalLabelCount;
            return provinces.length;
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {

            TextView province_tv;
            ImageView forward_iv;

            public MyViewHolder(@NonNull View itemView, int viewType) {
                super(itemView);
                if (viewType == VIEW_TYPE_LABEL) {
                    itemView.setPadding(Utils.dp2px(getContext(), 12), 0, 0, 0);
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                province_tv = itemView.findViewById(R.id.address_picker_recycler_item_province);
                forward_iv = itemView.findViewById(R.id.address_picker_recycler_item_forward);

            }
        }
    }
}
