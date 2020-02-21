package liang.zhou.lane8.no5.my_player.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.ArrayList;

import liang.zhou.lane8.no5.my_player.R;

public class JellyButton extends View {

    private Circle core;
    private ArrayList<Circle> accessoryCircles;
    private CoordinateAllocator xyAllocator;
    private int accessoryColor,coreColor;

    public JellyButton(Context context) {
        this(context,null);
    }

    public JellyButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JellyButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(Color.TRANSPARENT);
        accessoryCircles=new ArrayList<>(3);
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.JellyButton);
        accessoryColor=ta.getColor(R.styleable.JellyButton_accessory_circle_color,Color.BLACK);
        coreColor=ta.getColor(R.styleable.JellyButton_core_circle_color,Color.BLUE);
        ta.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(xyAllocator==null) {
            xyAllocator = new CoordinateAllocator(left, top, right, bottom);
        }
        createCenterCircle();
    }

    public float getCircleX(){
        if(core!=null) {
            return getX() + core.circleX - core.radius;
        }
        return 0;
    }
    public float getCircleY(){
        if(core!=null) {
            return getY() + core.circleY - core.radius;
        }
        return 0;
    }

    private void createCenterCircle(){
        if(core==null) {
            float circleX=getPivotX();
            float circleY=getPivotY();
            float radius=getHeight()<getWidth()?getHeight()/2:getWidth()/2-70;
            core=new Circle(radius,circleX,circleY,coreColor);
        }
    }
    private Circle createAccessoryCircle(int index) {
        Circle c = new Circle(core.radius/2,core.circleX,core.circleY,accessoryColor,
                xyAllocator.getDestX(index),xyAllocator.getDestY(index),core.circleX,core.circleY,
                createBessel(index));
        return c;
    }
    private void clearCircles() {
        if(accessoryCircles!=null){
            accessoryCircles.clear();
        }
    }
    private void createCircles(int howMany) {
        for(int i=0;i<howMany;i++){
            Circle c=createAccessoryCircle(i);
            accessoryCircles.add(c);
        }
    }
    private Bessel createBessel(int index){
        Bessel b=new Bessel(xyAllocator.getBesselStartX(index),
                xyAllocator.getBesselStartY(index),xyAllocator.getBesselEndX(index),
                xyAllocator.getBesselEndY(index),coreColor);
        return b;
    }

    private boolean inCircle(float circleX, float circleY, float touchX, float touchY) {
        return twoPointDistanceSquare(circleX,circleY,touchX,touchY)<core.radius*core.radius;
    }
    private boolean isSeparated(float aX,float aY,float bX,float bY) {
        float distanceSquare=twoPointDistanceSquare(aX,aY,bX,bY);
        return distanceSquare>((core.radius/2+core.radius+16)*(core.radius/2+core.radius+16));
    }
    private float twoPointDistanceSquare(float aX,float aY,float bX,float bY){
        float betweenX=Math.abs(bX-aX);
        float betweenY=Math.abs(bY-aY);
        return betweenX*betweenX+betweenY*betweenY;
    }

    private void invalidateNoDirty(){
        postInvalidate(getLeft(), getTop(), getRight(), getBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCenterCircle(canvas);
        drawAccessory(canvas);
    }

    private void drawAccessory(Canvas canvas) {
        if(accessoryCircles!=null&&accessoryCircles.size()>0){
            for(int i=0;i<accessoryCircles.size();i++) {
                Circle c=accessoryCircles.get(i);
                c.drawMyself(canvas);
                if(!inCircle(core.circleX,core.circleY,c.circleX,c.circleY)) {
                    if(!c.bessel.animationFinish) {
                        if(isSeparated(core.circleX,core.circleY,c.circleX,c.circleY)){
                            c.bessel.breakPointX=c.circleX;
                            c.bessel.breakPointY=c.circleY;
                            if(!c.bessel.animationPlaying){
                                playAnimation(c.bessel);
                            }
                        }else{
                            c.bessel.controlPointX=c.circleX;
                            c.bessel.controlPointY=c.circleY;
                        }
                        c.bessel.drawMyself(canvas);
                    }
                }
            }
        }
    }

    private void drawCenterCircle(Canvas canvas) {
        core.drawMyself(canvas);
    }

    public void playAnimation(){
        clearCircles();
        createCircles(3);
        radiate(accessoryCircles);
        invalidateNoDirty();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        float touchX=event.getX();
        float touchY=event.getY();
        if(action==MotionEvent.ACTION_DOWN){
            if(inCircle(core.circleX,core.circleY,touchX,touchY)) {
                clearCircles();
                //createCircles(3);
            }
        }else if(action==MotionEvent.ACTION_MOVE){

        }else if(action==MotionEvent.ACTION_UP){
            if(inCircle(core.circleX,core.circleY,touchX,touchY)) {
                //radiate(accessoryCircles);
            }else{
                clearCircles();
            }
        }
        invalidateNoDirty();
        return true;
    }

    private void radiate(ArrayList<Circle> object) {
        for(int i=0;i<object.size();i++) {
            final Circle c=object.get(i);
            ValueAnimator vaX = ValueAnimator.ofFloat(c.originalX,c.destX);
            ValueAnimator vaY = ValueAnimator.ofFloat(c.originalY,c.destY);
            vaX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    c.circleX= (float) animation.getAnimatedValue();
                    invalidateNoDirty();
                }
            });
            vaY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    c.circleY= (float) animation.getAnimatedValue();
                    invalidateNoDirty();
                }
            });
            AnimatorSet set=new AnimatorSet();
            set.playTogether(vaX,vaY);
            set.setDuration(600);
            set.setStartDelay(200);
            set.start();
        }
    }
    public void jellyAnimation(final Bessel b){
        ValueAnimator ob1 = getValueAnimator("forX",b, b.breakPointX, getPivotX());
        ValueAnimator ob2 = getValueAnimator("forY",b, b.breakPointY, getPivotY());
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                b.animationFinish = true;
                b.animationPlaying = false;
            }
        });
        set.setDuration(600);
        set.playTogether(ob1, ob2);
        set.start();
    }
    private ValueAnimator getValueAnimator(final String forWhom, final Bessel b, float ...value){
        ValueAnimator ob=ObjectAnimator.ofFloat(value);
        ob.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                //input总是从0到1。因此，如果要求函数的区间为0到3，则需要乘以3
                input=input*3;
                double result= (Math.sin(input*5)/(input*5));
                Log.d("onAnimation",input+"input");
                Log.d("onAnimation",result+"result");
                return (float) result;
            }
        });
        ob.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(forWhom.equals("forX")) {
                    b.controlPointX = (float) animation.getAnimatedValue();
                }else if(forWhom.equals("forY")) {
                    b.controlPointY = (float) animation.getAnimatedValue();
                }
                Log.d("onAnimation",animation.getAnimatedFraction()+"fraction");
                Log.d("onAnimation",animation.getAnimatedValue()+"value");
                invalidateNoDirty();
            }
        });
        return ob;
    }
    private void playAnimation(Bessel b) {
        Log.d("jellyAnimation","playingAnimation");
        b.animationPlaying=true;
        jellyAnimation(b);
    }
}
