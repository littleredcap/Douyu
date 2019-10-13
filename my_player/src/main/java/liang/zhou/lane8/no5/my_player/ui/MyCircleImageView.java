package liang.zhou.lane8.no5.my_player.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class MyCircleImageView extends AppCompatImageView {

    private float measuredWidth;
    private float measuredHeight;
    private float radius;
    private Paint circlePaint;
    private Matrix matrix;

    public MyCircleImageView(Context context) {
        this(context,null);
    }

    public MyCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circlePaint=new Paint();
        circlePaint.setAntiAlias(true);
        matrix=new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight=getMeasuredHeight();
        measuredWidth=getMeasuredWidth();
        radius=Math.min(measuredWidth,measuredHeight)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable=getDrawable();

        if(drawable instanceof BitmapDrawable) {
            circlePaint.setShader(getDrawableShader((BitmapDrawable)drawable));
            canvas.drawCircle(measuredWidth/2, measuredHeight/2, radius, circlePaint);
            return;
        }
    }

    public void setImageAlpha(int alpha){
        circlePaint.setAlpha(alpha);
        invalidate();
    }

    private BitmapShader getDrawableShader(BitmapDrawable drawable) {
        Bitmap bitmap=drawable.getBitmap();
        BitmapShader shader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = Math.max(measuredWidth / bitmap.getWidth(), measuredHeight / bitmap.getHeight());
        matrix.setScale(scale,scale);
        shader.setLocalMatrix(matrix);
        return shader;
    }
}
