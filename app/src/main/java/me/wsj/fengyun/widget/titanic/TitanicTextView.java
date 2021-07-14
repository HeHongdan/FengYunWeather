package me.wsj.fengyun.widget.titanic;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.core.content.res.ResourcesCompat;

import me.wsj.fengyun.R;

/**
 * Titanic
 * romainpiel
 * 13/03/2014
 */
public class TitanicTextView extends androidx.appcompat.widget.AppCompatTextView {

    public interface AnimationSetupCallback {
        public void onSetupAnimation(TitanicTextView titanicTextView);
    }

    // callback fired at first onSizeChanged
    private AnimationSetupCallback animationSetupCallback;
    // wave shader coordinates
    private float maskX, maskY;
    // if true, the shader will display the wave
    private boolean sinking;
    // true after the first onSizeChanged
    private boolean setUp;

    // shader containing a repeated wave
    private BitmapShader shader;
    // shader matrix
    private Matrix shaderMatrix;
    // wave drawable
    private Drawable wave;
    // (getHeight() - waveHeight) / 2
    private float offsetY;

    public TitanicTextView(Context context) {
        super(context);
        init();
    }

    public TitanicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitanicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        shaderMatrix = new Matrix();
    }

    public AnimationSetupCallback getAnimationSetupCallback() {
        return animationSetupCallback;
    }

    public void setAnimationSetupCallback(AnimationSetupCallback animationSetupCallback) {
        this.animationSetupCallback = animationSetupCallback;
    }

    public float getMaskX() {
        return maskX;
    }

    public void setMaskX(float maskX) {
        this.maskX = maskX;
        invalidate();
    }

    public float getMaskY() {
        return maskY;
    }

    public void setMaskY(float maskY) {
        this.maskY = maskY;
        invalidate();
    }

    public boolean isSinking() {
        return sinking;
    }

    public void setSinking(boolean sinking) {
        this.sinking = sinking;
    }

    public boolean isSetUp() {
        return setUp;
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        createShader();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        createShader();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();

        if (!setUp) {
            setUp = true;
            if (animationSetupCallback != null) {
                animationSetupCallback.onSetupAnimation(TitanicTextView.this);
            }
        }
        play();
    }

    /**
     * Create the shader
     * draw the wave with current color for a background
     * repeat the bitmap horizontally, and clamp colors vertically
     */
    private void createShader() {

        if (wave == null) {
//            Resources.Theme theme = getContext().getTheme();
//            theme.applyStyle(R.style.WaveStyle,false);
            wave = ResourcesCompat.getDrawable(getResources(), R.drawable.wave_blue, null);
        }


        int waveW = wave.getIntrinsicWidth();
        int waveH = wave.getIntrinsicHeight();

        Bitmap b = Bitmap.createBitmap(waveW, waveH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        c.drawColor(getCurrentTextColor());

        wave.setBounds(0, 0, waveW, waveH);
        wave.draw(c);

        shader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        getPaint().setShader(shader);

        offsetY = (getHeight() - waveH) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // modify text paint shader according to sinking state
        if (sinking && shader != null) {

            // first call after sinking, assign it to our paint
            if (getPaint().getShader() == null) {
                getPaint().setShader(shader);
            }

            // translate shader accordingly to maskX maskY positions
            // maskY is affected by the offset to vertically center the wave
            shaderMatrix.setTranslate(maskX, maskY + offsetY);

            // assign matrix to invalidate the shader
            shader.setLocalMatrix(shaderMatrix);
        } else {
            getPaint().setShader(null);
        }

        super.onDraw(canvas);
    }

    private AnimatorSet animatorSet;

    public void play() {
        if (animatorSet == null) {
            setSinking(true);

            ValueAnimator maskXAnimator = ValueAnimator.ofInt(0, 200);
            maskXAnimator.setRepeatCount(ValueAnimator.INFINITE);
            maskXAnimator.setDuration(1000);
            maskXAnimator.setStartDelay(0);
            maskXAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                maskX = value;
                invalidate();
            });

            int h = getMeasuredHeight();

            ValueAnimator maskYAnimator = ValueAnimator.ofInt(h / 2, -h / 2);
            maskYAnimator.setRepeatCount(ValueAnimator.INFINITE);
            maskYAnimator.setRepeatMode(ValueAnimator.REVERSE);
            maskYAnimator.setDuration(10000);
            maskYAnimator.setStartDelay(0);
            maskYAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                maskY = value;
                invalidate();
            });

            animatorSet = new AnimatorSet();
            animatorSet.playTogether(maskXAnimator, maskYAnimator);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.start();
        }
    }

    public void destory() {
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
            animatorSet = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destory();
    }
}
