package liang.zhou.lane8.no5.my_player;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

public class MyPopupWindow {

    public void show(Context context){
        PopupWindow popupWindow=new PopupWindow();
        popupWindow.setContentView(LayoutInflater.from(context).inflate(R.layout.
                popup_window_of_personal_info_portrait,null));
    }
}
