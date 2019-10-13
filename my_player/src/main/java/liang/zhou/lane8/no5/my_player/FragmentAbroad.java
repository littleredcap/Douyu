package liang.zhou.lane8.no5.my_player;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import liang.zhou.lane8.no5.my_player.ui.Utils;

public class FragmentAbroad extends Fragment {

    private RecyclerView recyclerView;
    private Country[] countries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_view_pager_recommend_recycler_view,null);
        initCountriesList();
        recyclerView=viewGroup.findViewById(R.id.home_page_view_pager_recommend_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        recyclerView.setAdapter(new MyAdapter(countries));
        recyclerView.addItemDecoration(new DividerItemDecoration(viewGroup.getContext(),DividerItemDecoration.VERTICAL));
        return viewGroup;
    }

    private String countries_str[]={"美国","加拿大","英国","法国"};
    private void initCountriesList() {
        countries=new Country[countries_str.length];
        for (int i=0;i<countries_str.length;i++){
            Country country=new Country(0);
            country.name=countries_str[i];
            countries[i]=country;
        }
    }

    class MyAdapter extends RecyclerView.Adapter{

        private Country countries[];

        MyAdapter(Country countries[]){
            this.countries=countries;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            ViewGroup itemView= (ViewGroup) LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.activity_address_picker_recycler_item,viewGroup,false);
            return new MyViewHolder(itemView,-1);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            MyViewHolder myViewHolder= (MyViewHolder) viewHolder;
            myViewHolder.province_tv.setText(countries[position].name);
            myViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Intent intent=new Intent(getActivity(),ActivityInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("address", countries[position].name);
                        bundle.putInt("itemPosition", position);
                        intent.putExtra("addressBundle", bundle);
                        startActivity(intent);
                    }
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return countries.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView province_tv;
            ImageView forward_iv;

            public MyViewHolder(@NonNull View itemView, int viewType) {
                super(itemView);

                province_tv = itemView.findViewById(R.id.address_picker_recycler_item_province);
                forward_iv = itemView.findViewById(R.id.address_picker_recycler_item_forward);
                forward_iv.setVisibility(View.GONE);
            }
        }
    }

}
