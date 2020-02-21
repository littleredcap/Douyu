package liang.zhou.lane8.no5.my_player.ui;

class CoordinateAllocator {

    private float left,top,right,bottom;
    private float pivotX,pivotY;

    private float besselStartX[];
    private float besselStartY[];
    private float besselEndX[];
    private float besselEndY[];
    private float destX[],destY[];

    public CoordinateAllocator(float left,float top,float right,float bottom){
        this.left=left;
        this.top=top;
        this.right=right;
        this.bottom=bottom;
        pivotX=(right-left)/2;
        pivotY=(bottom-top)/2;
        destX=new float[]{pivotX-70,pivotX,pivotX+70};
        destY=new float[]{pivotY-30,pivotY-70,pivotY-30};
        besselStartX=new float[]{pivotX-26,pivotX-20,pivotX+8};
        besselStartY=new float[]{pivotY+16,pivotY-20,pivotY-30};
        besselEndX=new float[]{pivotX-8,pivotX+22,pivotX+26};
        besselEndY=new float[]{pivotY-30,pivotY-20,pivotY+16};
    }

    public float getBesselStartX(int i) {
        return besselStartX[i];
    }

    public float getBesselStartY(int i) {
        return besselStartY[i];
    }

    public float getBesselEndX(int i) {
        return besselEndX[i];
    }

    public float getBesselEndY(int i) {
        return besselEndY[i];
    }

    public float getDestX(int i) {
        return destX[i];
    }

    public float getDestY(int i) {
        return destY[i];
    }
}
