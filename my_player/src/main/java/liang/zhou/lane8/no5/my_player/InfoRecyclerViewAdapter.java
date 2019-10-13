package liang.zhou.lane8.no5.my_player;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeanboy.cropview.cropper.CropperManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_business.Constant;
import liang.zhou.lane8.no5.my_player.okhttp.OKHttpUtil;
import liang.zhou.lane8.no5.my_player.ui.MyCircleImageView;

public class InfoRecyclerViewAdapter extends RecyclerView.Adapter
        implements AddressSelectedListener {

    private ArrayList<ItemsInInfo> items;
    private ImageView iv_portrait;

    private MyCircleImageView portrait;
    private final int ITEM_PORTRAIT=0;
    private final int ITEM_VALUE_KEY=1;
    private Context c;
    //private RecyclerView recyclerView;
    private PopupWindow popupWindow;
    private AppCompatActivity a;

    public int getCurrentItemPosition() {
        return currentItemPosition;
    }

    private int currentItemPosition;
    private int minimum_distance;

    public InfoRecyclerViewAdapter(ArrayList<ItemsInInfo> items){
        this.items=items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        c=viewGroup.getContext();
        a= (AppCompatActivity) c;
        //this.recyclerView= (RecyclerView) viewGroup;
        ViewGroup itemView= (ViewGroup) LayoutInflater.from(c).
                inflate(R.layout.activity_personal_info_recycler_view_item,viewGroup,false);
        minimum_distance=ViewConfiguration.get(a).getScaledTouchSlop();
        return new MyViewHolder(itemView,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return ITEM_PORTRAIT;
        }else{
            return ITEM_VALUE_KEY;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        MyViewHolder holder= (MyViewHolder) viewHolder;
        holder.label.setText(items.get(i).key);
        if (i == 0) {
            //Bitmap bitmap=(Bitmap) (items.get(i).value);
            //holder.portrait.setImageBitmap(bitmap);
            Glide.with(c).load(items.get(i).value).into(holder.portrait);
            iv_portrait=holder.portrait;
            Log.d("onBindViewHolder",""+holder.portrait.getDrawable());
        }else{
            holder.personal_info_value.setText(items.get(i).value.toString());
            int textColor=items.get(i).valueColor;
            holder.personal_info_value.setTextColor(textColor);
        }
        addItemTouch(holder,i);
    }
    public ImageView getPortraitImageView(){
        return iv_portrait;
    }

    public void showDialog(String key,int layoutId){
        final Dialog dialog=new Dialog(c,R.style.popupDialog);
        View dialogView=LayoutInflater.from(c).inflate(layoutId,null);

        TextView title=dialogView.findViewById(R.id.popup_window_of_personal_info_title);
        final TextView content1=dialogView.findViewById(R.id.popup_window_of_personal_info_content1);
        final TextView content2=dialogView.findViewById(R.id.popup_window_of_personal_info_content2);
        if(key.equals("头像")){
            title.setText("上传头像");
            content1.setText("从相册选择");
            content2.setText("拍照");
            content1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //lookOverAlbum();
                    a.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4);
                    dialog.dismiss();
                    return false;
                }
            });
            content2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    a.requestPermissions(new String[]{Manifest.permission.CAMERA}, 5);
                    dialog.dismiss();
                    return false;
                }
            });
        }else if(key.equals("性别")){
            title.setText("选择性别");
            content1.setText("男");
            content2.setText("女");
            content1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    items.get(currentItemPosition).value=content1.getText();
                    OKHttpUtil.uploadJson(Constant.HOST+"UpdatePersonalInfoServlet",
                            ((MyApplication)(a.getApplication())).currentUser.getUserId(),
                            "sex",content1.getText().toString(),null);
                    InfoRecyclerViewAdapter.this.notifyItemChanged(currentItemPosition);
                    dialog.dismiss();
                    return false;
                }
            });
            content2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    items.get(currentItemPosition).value=content2.getText();
                    OKHttpUtil.uploadJson(Constant.HOST+"UpdatePersonalInfoServlet",
                            ((MyApplication)(a.getApplication())).currentUser.getUserId(),
                            "sex",content2.getText().toString(),null);
                    InfoRecyclerViewAdapter.this.notifyItemChanged(currentItemPosition);
                    dialog.dismiss();
                    return false;
                }
            });
        }

        dialog.setContentView(dialogView);
        initDialogWindow(dialog);
        dialog.show();
    }

    private final int take_photo=2;
    public void launchCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityInfo a= (ActivityInfo) c;
        a.startActivityForResult(intent,take_photo);
    }

    private final int select_photo=1;
    private void lookOverAlbum() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        ActivityInfo a= (ActivityInfo) c;
        a.startActivityForResult(intent,select_photo);
    }

    private void initDialogWindow(Dialog dialog){
        Window window=dialog.getWindow();
        WindowManager.LayoutParams wlp=window.getAttributes();
        wlp.gravity=Gravity.BOTTOM;
        wlp.width=WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
    }

    private void addItemTouch(MyViewHolder holder, final int position){
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("onTouch","down");
                int action=event.getAction();
                ViewGroup viewGroup= (ViewGroup) v;
                int childCount=viewGroup.getChildCount();
                ItemsInInfo itemsInInfo=items.get(position);
                float down_x=0,down_y=0;
                if(action==MotionEvent.ACTION_DOWN){
                    down_x=event.getX();
                    down_y=event.getY();
                    for(int i=0;i<childCount;i++){
                        View view=viewGroup.getChildAt(i);
                        transparent(view,itemsInInfo);
                    }
                    viewGroup.requestDisallowInterceptTouchEvent(true);
                    return true;
                }else if(action==MotionEvent.ACTION_UP){
                    Log.d("onTouch","action_up");
                    currentItemPosition=position;

                    for(int i=0;i<childCount;i++){
                        View view=viewGroup.getChildAt(i);
                        restore(view,itemsInInfo);
                    }
                    if(itemsInInfo.key.equals("头像")||itemsInInfo.key.equals("性别")) {
                        showDialog(itemsInInfo.key,R.layout.popup_window_of_personal_info_portrait);
                    }else if(itemsInInfo.key.equals("昵称")){
                        AppCompatActivity a= (AppCompatActivity) c;
                        Intent intent=new Intent(a,ActivityAlterNickname.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("昵称",itemsInInfo.value.toString());
                        intent.putExtras(bundle);
                        a.startActivity(intent);
                    }else if(itemsInInfo.key.equals("生日")){
                        showDatePickerDialog(position);
                    }else if(itemsInInfo.key.equals("所在地")){
                        currentItemPosition=position;
                        AppCompatActivity a= (AppCompatActivity) c;
                        Intent intent=new Intent(a,ActivityAddress.class);
                        a.startActivity(intent);
                        //a.startActivityF
                    }else if(itemsInInfo.key.equals("常出没地")){
                        AppCompatActivity a= (AppCompatActivity) c;
                        Intent intent=new Intent(a,AlwaysAppearance.class);
                        a.startActivity(intent);
                    }else if(itemsInInfo.key.equals("邮箱")){
                        AppCompatActivity a= (AppCompatActivity) c;
                        Intent intent=new Intent(a,ActivityMailBinding.class);
                        a.startActivity(intent);
                    }else if (itemsInInfo.key.equals("实名认证")){
                        AppCompatActivity a= (AppCompatActivity) c;
                        Intent intent=new Intent(a,ActivityRealNameAuth.class);
                        a.startActivity(intent);
                    }else if(itemsInInfo.key.equals("密码")){
                        AppCompatActivity a= (AppCompatActivity) c;
                        Intent intent=new Intent(a,ActivityAlterPassword.class);
                        a.startActivity(intent);
                    }

                    Log.d("action_up","action_up");
                    return true;
                }else if(action==MotionEvent.ACTION_MOVE){
                    Log.d("onTouch","action_move");
                    viewGroup.requestDisallowInterceptTouchEvent(false);
                    /*Log.d("onTouch",Math.abs(event.getX()-downX)+","+Math.abs(event.getY()-downY));
                    if(Math.abs(event.getX()-downX)>750||Math.abs(event.getY()-downY)>750) {
                        for (int i = 0; i < childCount; i++) {
                            View view = viewGroup.getChildAt(i);
                            restore(view, itemsInInfo);
                        }
                    }*/
                    Log.d("onTouch",Math.abs(event.getX()-down_x)+","+Math.abs(event.getY()-down_x));
                    if(Math.abs(event.getX()-down_x)>minimum_distance||
                            Math.abs(event.getY()-down_y)>minimum_distance) {
                        for (int i = 0; i < childCount; i++) {
                            View view = viewGroup.getChildAt(i);
                            restore(view, itemsInInfo);
                        }
                        //restore(viewGroup,itemsInInfo);
                    }
                    /*for (int i = 0; i < childCount; i++) {
                        View view = viewGroup.getChildAt(i);
                        restore(view, itemsInInfo);
                    }*/
                    Log.d("action_move","action_move");
                    return true;
                }
                return false;
            }
        });
    }

    private void showDatePickerDialog(final int position) {
        final Dialog dialog=new Dialog(c);
        View dialogView=LayoutInflater.from(c).inflate(R.layout.
                activity_personal_info_date_picker_dialog,null);
        final DatePicker dp=dialogView.findViewById(R.id.activity_personal_info_date_picker);
        TextView cancel=dialogView.findViewById(R.id.activity_personal_info_date_picker_cancel);
        TextView confirm=dialogView.findViewById(R.id.activity_personal_info_date_picker_confirm);
        cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });
        confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                items.get(position).value=dp.getYear()+"-"+(dp.getMonth()+1)+"-"+dp.getDayOfMonth();
                OKHttpUtil.uploadJson(Constant.HOST+"UpdatePersonalInfoServlet",
                        ((MyApplication)(a.getApplication())).currentUser.getUserId(),
                        "birthday",items.get(position).value.toString(),null);
                notifyItemChanged(position);
                dialog.dismiss();
                return false;
            }
        });
        dp.setMaxDate(System.currentTimeMillis());
        dialog.setContentView(dialogView);
        initDialogWindow(dialog);
        dialog.show();
    }

    private void restore(View v,ItemsInInfo itemsInInfo){
        if(v instanceof TextView){
            if(v.getId()==R.id.personal_info_label_tv){
                ((TextView)v).setTextColor(Color.argb(255,0,0,0));
            }else if(v.getId()==R.id.personal_info_value){
                if (itemsInInfo!=null) {
                    ((TextView) v).setTextColor(itemsInInfo.valueColor);
                }
            }
        }else if(v instanceof ImageView){
            ImageView iv= (ImageView) v;
            iv.setImageAlpha(255);
        }
    }

    private void transparent(View v,ItemsInInfo itemsInInfo){
        if(v instanceof TextView){
            if(v.getId()==R.id.personal_info_label_tv){
                ((TextView)v).setTextColor(Color.argb(45,0,0,0));
            }else if(v.getId()==R.id.personal_info_value){
                if(itemsInInfo.valueColor==Color.GRAY) {
                    ((TextView) v).setTextColor(Color.argb(45, 170, 170, 170));
                }else{
                    ((TextView) v).setTextColor(Color.argb(45,255,101,0));
                }
            }
        }else if(v instanceof ImageView){
            ImageView iv= (ImageView) v;
            Log.d("transparent",""+v.getClass().getName());
            iv.setImageAlpha(55);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAddressSelected(String address,int itemPosition,String city[]) {
        items.get(itemPosition).value=address;
        notifyItemChanged(itemPosition);
    }

    public void setImageUri(String replace) {
        Bitmap bitmap=BitmapFactory.decodeFile(replace);
        items.get(0).value=bitmap;
        InfoRecyclerViewAdapter.this.notifyItemChanged(currentItemPosition);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        TextView label;
        ImageView portrait;
        TextView personal_info_value;

        public MyViewHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            if (viewType==ITEM_PORTRAIT){
                portrait=itemView.findViewById(R.id.personal_info_portrait);
                portrait.setVisibility(View.VISIBLE);
            }else if (viewType==ITEM_VALUE_KEY){
                personal_info_value=itemView.findViewById(R.id.personal_info_value);
                personal_info_value.setVisibility(View.VISIBLE);
            }
            label=itemView.findViewById(R.id.personal_info_label_tv);
        }
    }

}
