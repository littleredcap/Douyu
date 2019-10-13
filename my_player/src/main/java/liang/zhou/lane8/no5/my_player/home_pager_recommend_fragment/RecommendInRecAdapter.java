package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_business.data_model.User;
import liang.zhou.lane8.no5.my_player.MyApplication;
import liang.zhou.lane8.no5.my_player.R;
import liang.zhou.lane8.no5.my_player.ServerResponse;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class RecommendInRecAdapter extends RecyclerView.Adapter {

    private final int ITEM_TYPE_BANNER = 0;
    private final int ITEM_TYPE_GRID_VIEW = 1;
    private final int ITEM_TYPE_VERTICAL_BANNER = 2;
    private final int ITEM_TYPE_BODY = 3;
    private final int LOAD_RECYCLER_VIEW=5;

    private Banner banner;
    private GridView gridCardView;
    private GridView gridViewTop;
    private GridView gridViewBottom;
    private ViewFlipper viewFlipper;

    private ArrayList<String> imagePath;
    private MyApplication ma;
    private Context ctx;
    private String sub_id;
    private String sub_ids[];
    private Function current_func;
    private SparseArray func_map_tv;

    public RecommendInRecAdapter(Context context) {
        ctx = context;
        AppCompatActivity a = (AppCompatActivity) ctx;
        ma = (MyApplication) a.getApplication();
        func_map_tv=new SparseArray();
        sub_ids();
    }

    private void sub_ids(){
        sub_id = ma.currentUser.getSubscribedFuncId().trim();
        sub_ids = sub_id.split(",");
        Arrays.sort(sub_ids, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int a=Integer.parseInt(o1);
                int b=Integer.parseInt(o2);
                if(a<b){
                    return -1;
                }else if(a==b){
                    return 0;
                }else{
                    return 1;
                }

            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        myHandler = new MyHandler();
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        Log.d("onCreateViewHolder", viewGroup.toString());
        ViewGroup itemView = null;
        switch (viewType) {
            case ITEM_TYPE_BANNER:
                itemView = (ViewGroup) layoutInflater.inflate(R.layout.home_page_recommend_in_recommend_banner,
                        viewGroup, false);
                initBanner(itemView);
                break;
            case ITEM_TYPE_GRID_VIEW:
                itemView = (ViewGroup) layoutInflater.inflate(R.layout.home_page_recommend_in_recommend_grid_view,
                        viewGroup, false);
                initGridView((ViewGroup) itemView);
                break;
            case ITEM_TYPE_VERTICAL_BANNER:
                itemView = (ViewGroup) layoutInflater.inflate(R.layout.home_page_rec_in_rec_view_flipper,
                        viewGroup, false);
                initFlipperView((ViewGroup) itemView);
                break;
            case ITEM_TYPE_BODY:
                itemView = (ViewGroup) layoutInflater.inflate(R.layout.home_page_recommend_grid_view,
                        viewGroup, false);
                initGridCardView((ViewGroup) itemView);
                break;
            default:
                break;
        }
        return new RecInRecViewHolder(itemView);
    }

    private static final int INIT_FLIPPER_VIEW = 2;

    private void initFlipperView(ViewGroup viewGroup) {
        if(viewFlipper==null) {
            viewFlipper = viewGroup.findViewById(R.id.home_page_rec_in_rec_vf);
        }
        OKHttpUtil.uploadJson(Constant.HOST + "FetchFunction",
                -1, "howManyFunc", 4 + "", new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer = response.body().string();
                            Gson gson = new Gson();
                            Log.d("initBanner", jsonFromServer);
                            ArrayList<Function> funcs = gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<Function>>() {
                                    }.getType());
                            Message message = myHandler.obtainMessage();
                            message.obj = funcs;
                            message.what = INIT_FLIPPER_VIEW;
                            myHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initGridView(ViewGroup itemView) {
        gridViewTop = itemView.findViewById(R.id.home_page_rec_in_rec_grid_view_top);
        OKHttpUtil.uploadJson(Constant.HOST + "FetchGameServlet",
                -1, "howManyGames", 9 + "", new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer = response.body().string();
                            Gson gson = new Gson();
                            Log.d("initBanner", jsonFromServer);
                            ArrayList<Game> advs = gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<Game>>() {
                                    }.getType());
                            Message message = myHandler.obtainMessage();
                            message.obj = advs;
                            message.what = INIT_GRID_VIEW;
                            myHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        //gridViewBottom=itemView.findViewById(R.id.home_page_rec_in_rec_grid_view_bottom);

        //gridViewTop.setAdapter(new MyGridViewAdapter());
    }

    private static final int INIT_GRID_VIEW = 1;

    private void initGridCardView(ViewGroup itemView) {
        gridCardView = itemView.findViewById(R.id.home_page_grid_view_card);
        OKHttpUtil.uploadJson(Constant.HOST+"FetchLiveRoomServlet",-1,"howMany",
                7+"",new ServerResponse(){

                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer=response.body().string();
                            Gson gson=new Gson();
                            ArrayList<LiveRoom> rooms=gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<LiveRoom>>() {}.getType());
                            //LiveRoom liveRoom=gson.fromJson(jsonFromServer,LiveRoom.class);
                            Message message=myHandler.obtainMessage();
                            message.what=LOAD_RECYCLER_VIEW;
                            message.obj=rooms;
                            myHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private static final int INIT_BANNER = 0;


    private void subscribed_or_not(Function f, TextView func_subscribe) {
        //Log.d("subscribed_or_not",f.getId()+","+Arrays.toString(sub_ids));
        int position = Arrays.binarySearch(sub_ids, f.getId() + "");
        if (position >= 0) {
            Drawable d = func_subscribe.getBackground();
            d.setTint(Color.GRAY);
            Log.d("subscribed_or_not_y", f.getId() + "," + Arrays.toString(sub_ids));
            func_subscribe.setText("已订阅");
        }else {
            Drawable d = func_subscribe.getBackground();
            d.setTint(0xffff8800);
            Log.d("subscribed_or_not_n", f.getId() + "," + Arrays.toString(sub_ids));
            func_subscribe.setText("预订");
        }
    }

    private void findViewFlipperChildren(View itemView, Function f) {
        TextView func_name = itemView.findViewById(R.id.home_page_rec_rec_activity_name);
        TextView func_date = itemView.findViewById(R.id.home_page_rec_rec_date);
        TextView func_time = itemView.findViewById(R.id.home_page_rec_rec_time);
        TextView func_sub_population = itemView.findViewById(R.id.home_page_rec_rec_population);
        TextView func_subscribe = itemView.findViewById(R.id.home_page_rec_rec_subscribe);
        func_map_tv.put(f.getId(),func_sub_population);
        func_subscribe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Arrays.binarySearch(sub_ids, f.getId()+"") >= 0) {
                    unSubscribe(func_subscribe,f.getId(),ma.currentUser.getUserId());
                } else {
                    subscribe(func_subscribe,f.getId(),ma.currentUser.getUserId());
                }
                return false;
            }
        });
        subscribed_or_not(f, func_subscribe);
        func_sub_population.setText(f.getSubPopulation() + "");
        func_name.setText(f.getFunctionName());
        Calendar c = Calendar.getInstance();
        c.setTime(f.getFunctionDate());
        func_date.setText(c.get(Calendar.MONTH)+1 + "月" + c.get(Calendar.DAY_OF_MONTH) + "日");
        int minute = f.getFunctionTime().getMinutes();
        if (minute < 10) {
            func_time.setText(f.getFunctionTime().getHours() + ":0" + f.getFunctionTime().getMinutes() + "开始");
        } else {
            func_time.setText(f.getFunctionTime().getHours() + ":" + f.getFunctionTime().getMinutes() + "开始");
        }
    }


    private void unSubscribe(TextView tv,int funcId,int userId) {
        Log.d("unSub","请求取消订阅");
        tv.setEnabled(false);
        tv.setClickable(false);
        OKHttpUtil.uploadJson(Constant.HOST + "UnSubscribeServlet", userId,
                "functionId", funcId + "", new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer = response.body().string();
                            try {
                                JSONObject jsonObject=new JSONObject(jsonFromServer);
                                int row=jsonObject.getInt("updateRow");
                                if(row>=1){
                                    Message message = myHandler.obtainMessage();
                                    message.what = UPDATE_USER;
                                    myHandler.sendMessage(message);
                                    OKHttpUtil.uploadJson(Constant.HOST + "FetchFunction", -1,
                                            "functionId", funcId+"", new ServerResponse() {
                                                @Override
                                                public void response(Call call, Response response) {
                                                    try {
                                                        String s=response.body().string();
                                                        Gson gson=new Gson();
                                                        current_func=gson.fromJson(s, Function.class);
                                                        Message message = myHandler.obtainMessage();
                                                        message.what = UPDATE_FUNC;
                                                        message.obj=tv;

                                                        myHandler.sendMessage(message);
                                                        Log.d("subscribe", jsonFromServer);

                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private static final int UPDATE_USER=3;
    private void subscribe(TextView tv,int funcId,int userId) {
        Log.d("unSub","请求订阅");
        tv.setEnabled(false);
        tv.setClickable(false);
        OKHttpUtil.uploadJson(Constant.HOST + "SubscribeServlet", userId,
                "functionId", funcId + "", new ServerResponse() {
            @Override
            public void response(Call call, Response response) {
                try {
                    String jsonFromServer = response.body().string();
                    try {
                        JSONObject jsonObject=new JSONObject(jsonFromServer);
                        int row=jsonObject.getInt("updateRow");
                        if(row>=1){
                            Message message = myHandler.obtainMessage();
                            message.what = UPDATE_USER;
                            myHandler.sendMessage(message);
                            OKHttpUtil.uploadJson(Constant.HOST + "FetchFunction", -1,
                                    "functionId", funcId+"", new ServerResponse() {
                                        @Override
                                        public void response(Call call, Response response) {
                                            try {
                                                String s=response.body().string();
                                                Gson gson=new Gson();
                                                current_func=gson.fromJson(s, Function.class);
                                                Message message = myHandler.obtainMessage();
                                                message.what = UPDATE_FUNC;
                                                message.obj=tv;

                                                myHandler.sendMessage(message);
                                                Log.d("subscribe", jsonFromServer);

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //OKHttpUtil.
    }

    private static final int UPDATE_FUNC=4;
    private StringBuffer funcIds=new StringBuffer();
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == INIT_BANNER) {
                initImagePath((ArrayList<Advertisement>) msg.obj);
            } else if (msg.what == INIT_GRID_VIEW) {
                gridViewTop.setAdapter(new MyGridViewRecAdapter((ArrayList<Game>) msg.obj));
            } else if (msg.what == INIT_FLIPPER_VIEW) {
                ArrayList<Function> functions = (ArrayList<Function>) msg.obj;
                for (Function f : functions) {
                    View itemView = LayoutInflater.from(viewFlipper.getContext()).
                            inflate(R.layout.home_page_rec_in_rec_vf_item1, null);
                    funcIds.append(f.getId()).append(",");
                    findViewFlipperChildren(itemView, f);
                    viewFlipper.addView(itemView);
                }
            }else if(msg.what==UPDATE_USER){
                JSONObject toServer=new JSONObject();
                try {
                    toServer.put("username",ma.currentUser.getUsername());
                    toServer.put("password",ma.currentUser.getPassword());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OKHttpUtil.uploadJSONs(Constant.HOST + "GetUserAfterUpdate", toServer.toString(), new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String s=response.body().string();
                            Log.d("GetUserAfterUpdate",s);
                            Gson gson=new Gson();
                            ma.currentUser=gson.fromJson(s, User.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else if(msg.what==UPDATE_FUNC){
                TextView tv= (TextView) msg.obj;
                if(current_func!=null){
                    tv.setText(current_func.getSubPopulation()+"");
                    sub_ids();
                    TextView tv_population= (TextView) func_map_tv.get(current_func.getId());
                    tv_population.setText(current_func.getSubPopulation()+"");
                    subscribed_or_not(current_func,tv);
                    tv.setEnabled(true);
                    tv.setClickable(true);
                }
            }else if(msg.what==LOAD_RECYCLER_VIEW){
                ArrayList<LiveRoom> liveRooms= (ArrayList<LiveRoom>) msg.obj;
                //LiveRoom liveRoom= (LiveRoom) msg.obj;
                //liveRooms.add(liveRoom);
                gridCardView.setAdapter(new MyGridCardViewAdapter(liveRooms,ctx));
            }
        }
    }

    private MyHandler myHandler;

    private void initBanner(View itemView) {
        banner = itemView.findViewById(R.id.home_page_recommend_in_recommend_banner);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setImageLoader(new MyImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.d("onBannerClick", position + "");
            }
        });
        OKHttpUtil.uploadJson(Constant.HOST + "FetchAdvertisement",
                -1, "howManyAdv", "8", new ServerResponse() {
                    @Override
                    public void response(Call call, Response response) {
                        try {
                            String jsonFromServer = response.body().string();
                            Gson gson = new Gson();
                            Log.d("initBanner", jsonFromServer);
                            ArrayList<Advertisement> advs = gson.fromJson(jsonFromServer,
                                    new TypeToken<ArrayList<Advertisement>>() {
                                    }.getType());
                            Message message = myHandler.obtainMessage();
                            message.obj = advs;
                            message.what = INIT_BANNER;
                            ;
                            myHandler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    private void initImagePath(ArrayList<Advertisement> advs) {
        if (imagePath == null) {
            imagePath = new ArrayList<>();
        }
        for (Advertisement adv : advs) {
            imagePath.add(adv.getPicUrl());
        }
        banner.setImages(imagePath).start();
    }

    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            RequestOptions options = new RequestOptions();
            options.transform(new RoundedCornersTransformation(25, 0));
            //Glide.with(context.getApplicationContext()).load(path).into(imageView);
            Glide.with(context.getApplicationContext()).load(path).apply(options).into(imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_BANNER;
        } else if (position == 1) {
            return ITEM_TYPE_GRID_VIEW;
        } else if (position == 2) {
            return ITEM_TYPE_VERTICAL_BANNER;
        } else {
            return ITEM_TYPE_BODY;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class RecInRecViewHolder extends RecyclerView.ViewHolder {

        public RecInRecViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
