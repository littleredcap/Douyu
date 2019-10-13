package liang.zhou.lane8.no5.my_player.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Arrays;

public class MyCustomTextView extends android.support.v7.widget.AppCompatEditText {

    private Context context;
    public MyCustomTextView(Context context) {
        super(context);
        this.context=context;
    }

    public MyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public MyCustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }



    /*@Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            Drawable drawable= context.getDrawable(R.drawable.video_view_main_bottom_bar_send_pressing);
            setCompoundDrawables(drawable,drawable,drawable,drawable);
        }else if(event.getAction()==MotionEvent.ACTION_UP) {
            *//*Drawable drawables[] = getCompoundDrawables();
            for (int i = 0; i < drawables.length; i++) {
                if (drawables[i] != null) {
                    Rect rect = drawables[i].getBounds();
                    Log.d("onTouchEvent", rect.top + "," + rect.left + "," + rect.right + "," + rect.bottom);
                    if (rect.contains((int) (event.getX()), (int) (event.getY()))) {
                        Log.d("onTouchEvent", "you click in me");
                    }
                }
            }*//*
        }
        return super.onTouchEvent(event);
    }*/

}
