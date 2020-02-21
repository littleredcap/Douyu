package liang.zhou.lane8.no5.my_player.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

class Bessel {
    Paint p;
    float startPointX;
    float startPointY;
    float endPointX;
    float endPointY;
    float controlPointX;
    float controlPointY;
    float breakPointX;
    float breakPointY;
    boolean animationFinish;
    boolean animationPlaying;

    public Bessel(float besselStartX,float besselStartY,float besselEndX,float besselEndY,int color){
        this.startPointX=besselStartX;
        this.startPointY=besselStartY;
        this.endPointX=besselEndX;
        this.endPointY=besselEndY;
        this.animationFinish=false;
        this.animationPlaying=false;
        this.p=new Paint();
        p.setColor(color);
        p.setStrokeWidth(2);
        p.setAntiAlias(true);;
    }
    public void drawMyself(Canvas c){
        Path path=new Path();
        path.moveTo(startPointX,startPointY);
        path.quadTo(controlPointX, controlPointY, endPointX, endPointY);
        c.drawPath(path,p);
    }
}
