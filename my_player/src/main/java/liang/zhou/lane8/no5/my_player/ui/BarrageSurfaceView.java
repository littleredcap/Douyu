package liang.zhou.lane8.no5.my_player.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.DanMu;
import liang.zhou.lane8.no5.my_player.DanMuManager;
import liang.zhou.lane8.no5.my_player.NewBarrageListener;

public class BarrageSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable, NewBarrageListener {

    private Paint barragePaint;
    private float origin_x;
    private float origin_y;
    private String test_barrage = "倩哥，被你萌到了";
    private Canvas canvas;
    private Thread drawThread;
    private ArrayList<DanMu> barrages;
    private ArrayList<DanMu> waitingForDrawBarrages;
    private DanMuManager barrageManager;

    public BarrageSurfaceView(Context context) {
        this(context, null);
    }

    public BarrageSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        //setBackgroundColor(Color.WHITE);
        setZOrderMediaOverlay(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        barragePaint = new Paint();
        barragePaint.setTextSize(Utils.sp2px(context, 13));
        barragePaint.setColor(Color.RED);
        barrages = new ArrayList<>();
        waitingForDrawBarrages = new ArrayList<>();
        barrageManager = DanMuManager.getDanMuManger();
        barrageManager.setBarrageListener(this);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        origin_x = getWidth();
        origin_y = getPivotY();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                drawBarrages();
            }
        }).start();
        //new DrawBarrageThread().start();
        //new DrawBarrageThread().start();
    }

    private void clearScreen() {
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(PixelFormat.TRANSPARENT, PorterDuff.Mode.CLEAR);
        getHolder().unlockCanvasAndPost(canvas);
    }

    private void performDraw() {
        while (true) {
            while (waitingForDrawBarrages == null || waitingForDrawBarrages.size() == 0) {
            }
            DanMu barrage = takeBarrage(waitingForDrawBarrages);
            if (barrage == null) {
                continue;
            }
            initBarrageChar(barrage);
            while (barrage.barrageChar.width + barrage.barrageChar.current_x > 0) {
                drawBarrage(barrage.barrageChar.width, barrage);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void initBarragesChar(ArrayList<DanMu> drawingBarrages) {
        Log.d("initBarrageChar", barrages.size() + "");
        for (DanMu barrage : drawingBarrages) {
            initBarrageChar(barrage);
        }
    }

    private void initBarrageChar(DanMu barrage) {
        barrage.barrageChar.moveSpeed = (int) (Math.random() * 3 + 3);
        barrage.barrageChar.current_x = (int) origin_x;

        barrage.barrageChar.height = getStringHeight(barrage.getContent(), barragePaint);
        //barrage.barrageChar.current_y =barrage.barrageChar.height;
        barrage.barrageChar.current_y = (int) (Math.random() * getPivotY() + barrage.barrageChar.height);
        barrage.barrageChar.width = (int) barragePaint.measureText(barrage.getContent());

    }

    private int getStringHeight(String string, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(string, 0, string.length(), rect);
        return rect.height();
    }


    @Override
    public void run() {

        while (true) {
            while (waitingForDrawBarrages == null || waitingForDrawBarrages.size() == 0) {
            }
            DanMu barrage = takeBarrage(waitingForDrawBarrages);
            initBarrageChar(barrage);
            while (barrage.barrageChar.width + barrage.barrageChar.current_x > 0) {
                drawBarrage(barrage.barrageChar.width, barrage);
            }
            /*waitingForDrawBarrages.addAll(barrages);
            initBarrageChar(waitingForDrawBarrages);
            int size=waitingForDrawBarrages.size();
            for (int i=0;i<size;i++) {
                DanMu barrage=waitingForDrawBarrages.get(i);
                int barrageWidth = barrage.barrageChar.width;
                while (barrageWidth + barrage.barrageChar.current_x > 0) {
                    drawBarrage(barrageWidth, barrage);
                }
                waitingForDrawBarrages.remove(barrage);
                barrages.remove(barrage);
            }*/
        }

    }

    public synchronized DanMu takeBarrage(ArrayList<DanMu> barrages) {
        DanMu barrage = null;
        if (barrages != null && barrages.size() != 0) {
            barrage = barrages.get(0);
            barrages.remove(barrage);
        }
        return barrage;
    }

    private void drawBarrages() {
        DanMu barrage = null;
        while (true) {
            if (waitingForDrawBarrages == null || waitingForDrawBarrages.size() == 0) {
                continue;
            }
            //initBarragesChar(waitingForDrawBarrages);
            canvas = getHolder().lockCanvas();
            if(canvas==null){
                return;
            }
            canvas.drawColor(PixelFormat.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for (int i = 0; i < waitingForDrawBarrages.size(); i++) {
                barrage = waitingForDrawBarrages.get(i);
                canvas.drawText(barrage.getContent(), barrage.barrageChar.current_x,
                        barrage.barrageChar.current_y, barragePaint);
                if (barrage.barrageChar.width + barrage.barrageChar.current_x <= 0) {
                    waitingForDrawBarrages.remove(barrage);
                } else {
                    barrage.barrageChar.current_x -= barrage.barrageChar.moveSpeed;
                }
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawBarrage(float barrageWidth, DanMu barrage) {
        /*canvas = getHolder().lockCanvas(new Rect(barrage.barrageChar.current_x,barrage.barrageChar.current_y,
                (int) (barrage.barrageChar.current_x+barrageWidth),
                barrage.barrageChar.current_y+13));*/
        canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawText(barrage.getContent(), barrage.barrageChar.current_x,
                barrage.barrageChar.current_y, barragePaint);
        barrage.barrageChar.current_x -= barrage.barrageChar.moveSpeed;
        if (barrageWidth + barrage.barrageChar.current_x <= 0) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void onNewBarrage(DanMu barrage) {
        waitingForDrawBarrages.add(barrage);
        //initBarragesChar(waitingForDrawBarrages);
        initBarrageChar(barrage);
    }
}
