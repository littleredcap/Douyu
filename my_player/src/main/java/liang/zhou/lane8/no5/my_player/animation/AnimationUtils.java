package liang.zhou.lane8.no5.my_player.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import liang.zhou.lane8.no5.my_player.ui.JellyButton;

public class AnimationUtils {

    public static final boolean OPAQUE=true;
    public static final boolean TRANSPARENCY=false;

    public enum transparent{
        OPAQUE,TRANSPARENCY;
    }

    public static int currentColor;

    public static void colorChange(BackgroundTintWrapper target,boolean alphaDirection,int...color){
        ObjectAnimator alpha=null;
        if(alphaDirection==OPAQUE){
            alpha=ObjectAnimator.ofInt(target,
                    "alpha",0,255);
        }else if(alphaDirection==TRANSPARENCY){
            alpha=ObjectAnimator.ofInt(target,
                    "alpha",255,0);
        }
        alpha.setDuration(1000);
        alpha.addListener(new AnimatorListenerAdapter() {
            int index;
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                if(index<color.length) {
                    currentColor=color[index];
                    target.setColor(currentColor);
                    index++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                target.setColor(color[color.length-1]);
            }
        });
        alpha.setRepeatCount(color.length);
        alpha.start();
    }

    public static void locationChange(View view,float valueX[],float valueY[],View view2,boolean reverse){
        ObjectAnimator forX=ObjectAnimator.ofFloat(view,"X",valueX);
        ObjectAnimator forY=ObjectAnimator.ofFloat(view,"Y",valueY);
        forY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (reverse) {

                } else {
                    view.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    if (view2 instanceof JellyButton) {
                        ((JellyButton) view2).playAnimation();
                    }
                }
            }
        });
        AnimatorSet set=new AnimatorSet();
        set.playTogether(forX,forY);
        set.setDuration(600);
        set.start();
    }
}