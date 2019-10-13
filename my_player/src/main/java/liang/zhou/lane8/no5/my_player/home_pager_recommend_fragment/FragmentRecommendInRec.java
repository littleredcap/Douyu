package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ui.Utils;

public class FragmentRecommendInRec extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private MyGridCardViewAdapter gridCardViewAdapter;
    private Banner banner;
    private ArrayList<Integer> imagePath;
    private GridView gridView;
    private GridView grid_card_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_view_pager_recommend_recycler_view,null);
        recyclerView=viewGroup.findViewById(R.id.home_page_view_pager_recommend_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGroup.getContext()));
        adapter=new RecommendInRecAdapter(container.getContext());
        recyclerView.setAdapter(adapter);
        /*banner=viewGroup.findViewById(R.id.home_page_recommend_in_recommend_banner);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);

        banner.setImageLoader(new MyImageLoader());
        initImagePath();
        createBitmap();
        banner.setImages(bitmaps).start();*/
        /*ViewGroup viewGroup= (ViewGroup) inflater.
                inflate(R.layout.home_page_recommend_in_recommend,null);
        initBanner(viewGroup);
        initGridView(viewGroup);*/
        //initCardView(viewGroup);
        //initRecyclerView(viewGroup);
        //initGridCardView(viewGroup);
        return viewGroup;
    }

    /*private void initGridCardView(ViewGroup viewGroup) {
        grid_card_view=viewGroup.findViewById(R.id.home_page_recommend_in_recommend_grid_card_view);
        gridCardViewAdapter=new MyGridCardViewAdapter();
        grid_card_view.setAdapter(gridCardViewAdapter);
    }*/

    /*private void initRecyclerView(ViewGroup viewGroup) {
        recyclerView=viewGroup.findViewById(R.id.home_page_recommend_in_recommend_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        adapter=new RecommendInRecAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }*/

    private void initCardView(ViewGroup viewGroup) {
    }

    private void initGridView(ViewGroup viewGroup) {
    }

    private void initBanner(ViewGroup viewGroup) {
        banner=viewGroup.findViewById(R.id.home_page_recommend_in_recommend_banner);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);

        banner.setImageLoader(new MyImageLoader());
        initImagePath();
        banner.setImages(imagePath).start();
    }

    /*private void createBitmap(){
        bitmaps=new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.yukee);
        for (int i=0;i<4;i++) {

            Matrix matrix = new Matrix();
            matrix.setScale(0.5f, 0.5f);
            Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);

            bitmaps.add(bm);
        }
    }*/

    private void initImagePath() {
        imagePath=new ArrayList<>();
        imagePath.add(R.mipmap.yukee);
        imagePath.add(R.mipmap.yukee);
        imagePath.add(R.mipmap.yukee);
        imagePath.add(R.mipmap.yukee);
        imagePath.add(R.mipmap.yukee);
    }

    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

}
