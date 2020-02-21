package liang.zhou.lane8.no5.my_player.ui;

import android.graphics.Canvas;
import android.graphics.Paint;

class Circle {
    Paint p;
    float radius;
    Bessel bessel;
    float originalX,originalY,destX,destY,circleX,circleY;

    private Circle(){

    }

    public Circle(float radius,float circleX, float circleY, int color){
        this();
        this.p=new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
        this.radius=radius;
        this.circleX=circleX;
        this.circleY=circleY;
    }
    public Circle(float radius,float circleX, float circleY,int color,
                  float destX,float destY,float originalX,float originalY,Bessel b){
        this(radius,circleX,circleY,color);
        this.bessel=b;
        this.destX=destX;
        this.destY=destY;
        this.originalX=originalX;
        this.originalY=originalY;
    }
    public void drawMyself(Canvas c){
        c.drawCircle(circleX,circleY,radius,p);
    }
}
