package com.starsoft.traveldiary.ui.fonts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Aashish on 7/18/2016.
 */
public class QsRTextView extends TextView implements ShimmerViewBase {

    private ShimmerViewHelper shimmerViewHelper;

    public QsRTextView(Context context) {
        super(context);
        setCustomFont(context);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), null);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }

    public QsRTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), attrs);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }


    public QsRTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context);
        shimmerViewHelper = new ShimmerViewHelper(this, getPaint(), attrs);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
    }


    private void setCustomFont(Context context){
        Typeface customFont = FontCache.getTypeface("Quicksand-Regular.otf",context);
        setTypeface(customFont);
    }

    @Override
    public float getGradientX() {
        return shimmerViewHelper.getGradientX();
    }

    @Override
    public void setGradientX(float gradientX) {
        shimmerViewHelper.setGradientX(gradientX);
    }

    @Override
    public boolean isShimmering() {
        return shimmerViewHelper.isShimmering();
    }

    @Override
    public void setShimmering(boolean isShimmering) {
        shimmerViewHelper.setShimmering(isShimmering);
    }

    @Override
    public boolean isSetUp() {
        return shimmerViewHelper.isSetUp();
    }

    @Override
    public void setAnimationSetupCallback(ShimmerViewHelper.AnimationSetupCallback callback) {
        shimmerViewHelper.setAnimationSetupCallback(callback);
    }

    @Override
    public int getPrimaryColor() {
        return shimmerViewHelper.getPrimaryColor();
    }

    @Override
    public void setPrimaryColor(int primaryColor) {
        shimmerViewHelper.setPrimaryColor(primaryColor);
    }

    @Override
    public int getReflectionColor() {
        return shimmerViewHelper.getReflectionColor();
    }

    @Override
    public void setReflectionColor(int reflectionColor) {
        shimmerViewHelper.setReflectionColor(reflectionColor);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.onSizeChanged();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (shimmerViewHelper != null) {
            shimmerViewHelper.onDraw();
        }
        super.onDraw(canvas);
    }
}
