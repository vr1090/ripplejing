package iseng.inu.de.cobaanimasi;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.*;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by fyu on 11/3/14.
 */

public class AnjingRipple extends RelativeLayout{

    private static final int DEFAULT_RIPPLE_COUNT=6;
    private static final int DEFAULT_DURATION_TIME=3000;
    private static final float DEFAULT_SCALE=6.0f;
    private static final int DEFAULT_FILL_TYPE=0;

    private int rippleColor;
    private float rippleStrokeWidth;
    private float rippleRadius;
    private int rippleDurationTime;
    private int rippleAmount;
    private int rippleDelay;
    private float rippleScale;
    private int rippleType;
    private Paint paint;
    private boolean animationRunning=false;
    private AnimatorSet animatorSet;
    private ArrayList<Animator> animatorList;
    private LayoutParams rippleParams;
    private ArrayList<RippleView> rippleViewList=new ArrayList<RippleView>();

    public AnjingRipple(Context context) {
        super(context);
    }

    public AnjingRipple(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnjingRipple(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
        rippleColor=typedArray.getColor(R.styleable.RippleBackground_rb_color, getResources().getColor(R.color.rippelColor));
        rippleStrokeWidth=typedArray.getDimension(R.styleable.RippleBackground_rb_strokeWidth, getResources().getDimension(R.dimen.rippleStrokeWidth));
        rippleRadius=typedArray.getDimension(R.styleable.RippleBackground_rb_radius,getResources().getDimension(R.dimen.rippleRadius));
        rippleDurationTime=typedArray.getInt(R.styleable.RippleBackground_rb_duration,DEFAULT_DURATION_TIME);
        rippleAmount=typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount,DEFAULT_RIPPLE_COUNT);
        rippleScale=typedArray.getFloat(R.styleable.RippleBackground_rb_scale,DEFAULT_SCALE);
        rippleType=typedArray.getInt(R.styleable.RippleBackground_rb_type,DEFAULT_FILL_TYPE);
        typedArray.recycle();





        rippleParams=new LayoutParams((int)(2*(rippleRadius+rippleStrokeWidth)),(int)(2*(rippleRadius+rippleStrokeWidth)));
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);

        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorList=new ArrayList<Animator>();


        rippleAmount = 1;
        //rippleDelay=rippleDurationTime/rippleAmount;
        rippleDelay = 1000;
            //paint it ...


            final Animator merah= createAnimator(0,2000,"#F0FF0000", new LinearInterpolator() );
            final Animator putih = createAnimator(300,2000,"#FFFFFFFF", new LinearInterpolator());

            merah.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    putih.start();
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });


        putih.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                merah.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        merah.start();

        //animatorSet.playTogether(animatorList);
    }//end method


    AnimatorSet createAnimator(int rippleDelay, int rippleDurationTime, String color, Interpolator polator){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle( Paint.Style.FILL);
        if(rippleType==DEFAULT_FILL_TYPE){
            rippleStrokeWidth=0;
            paint.setStyle(Paint.Style.FILL);
        }else
            paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.parseColor(color));
        AnimatorSet anim = new AnimatorSet();
        ArrayList<Animator> animatorList = new ArrayList<>();
        RippleView rippleView=new RippleView(getContext());
        rippleView.paint = paint;
        addView(rippleView,rippleParams);

        //add di relativeLayout..
        rippleViewList.add(rippleView);

        //animator.. scaleX.. sampe gedein berapa kali..
        final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView,"scaleX", 1.0f, rippleScale);//scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);//scaleXAnimator.setRepeatMode(ObjectAnimtor.REVERSE);//scaleXAnimator.setStartDelay(rippleDelay);//scaleXAnimator.setInterpolator(new OverhootInterpolator());
        //scaleXAnimator.setStartDelay(rippleDelay);
        scaleXAnimator.setDuration((rippleDurationTime));


        animatorList.add(scaleXAnimator);


        //animator scale y .. gendein berapa kali
        final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale);//scaleYAnimator.setStartDelay(rippleDelay);//scaleYAnimator.setInterpolator(new OvershootInterpolator());
        //scaleYAnimator.setStartDelay(rippleDelay);
        scaleYAnimator.setDuration((rippleDurationTime ));


        animatorList.add( scaleYAnimator);

        anim.playTogether(animatorList);
        anim.setStartDelay(rippleDelay);
        anim.setInterpolator(polator);

        return anim;
    }//end of method

    private class RippleView extends View{
        Paint paint;

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int radius=(Math.min(getWidth(),getHeight()))/2;
            canvas.drawCircle(radius,radius,radius-rippleStrokeWidth,paint);
        }
    }

    public void startRippleAnimation(){
        if(!isRippleAnimationRunning()){
            for(RippleView rippleView:rippleViewList){
                rippleView.setVisibility(VISIBLE);
            }
           animatorSet.start();
            animationRunning=true;

        }
    }

    public void stopRippleAnimation(){
        if(isRippleAnimationRunning()){
            animatorSet.end();
            animationRunning=false;
        }
    }

    public boolean isRippleAnimationRunning(){
        return animationRunning;
    }
}