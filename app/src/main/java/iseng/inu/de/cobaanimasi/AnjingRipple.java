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
import android.view.animation.AccelerateDecelerateInterpolator;
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

        rippleAmount = 2;
        rippleDelay=rippleDurationTime/rippleAmount;



        rippleParams=new LayoutParams((int)(2*(rippleRadius+rippleStrokeWidth)),(int)(2*(rippleRadius+rippleStrokeWidth)));
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);

        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorList=new ArrayList<Animator>();



        for(int i=0;i<rippleAmount;i++){
            //paint it ...
            paint = new Paint();
            paint.setAntiAlias(true);
            if(rippleType==DEFAULT_FILL_TYPE){
                rippleStrokeWidth=0;
                paint.setStyle(Paint.Style.FILL);
            }else
                paint.setStyle(Paint.Style.STROKE);

            if( i%2 == 0) {
                paint.setColor(Color.parseColor("#FFFF0000"));
            }else {
                paint.setColor(Color.parseColor("#FFFFFFFF"));
            }



            //bikin ripple view..
            RippleView rippleView=new RippleView(getContext());
            rippleView.paint = paint;
            addView(rippleView,rippleParams);

            //add di relativeLayout..
            rippleViewList.add(rippleView);

            //animator.. scaleX.. sampe gedein berapa kali..
            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, rippleScale);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(i * rippleDelay);
            scaleXAnimator.setDuration(rippleDurationTime);
            animatorList.add(scaleXAnimator);

            //animator scale y .. gendein berapa kali
            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(i * rippleDelay);
            scaleYAnimator.setDuration(rippleDurationTime);
            animatorList.add(scaleYAnimator);

//            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0f);
//            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
//            alphaAnimator.setStartDelay(i * rippleDelay);
//            alphaAnimator.setDuration(rippleDurationTime);
//            animatorList.add(alphaAnimator);
        } // end for loopp

        animatorSet.playTogether(animatorList);
    }

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