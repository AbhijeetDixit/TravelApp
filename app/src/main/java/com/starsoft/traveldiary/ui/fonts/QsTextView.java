package com.starsoft.traveldiary.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Aashish on 7/18/2016.
 */
public class QsTextView extends TextView {
    public QsTextView(Context context) {
        super(context);
        setCustomFont(context);
    }

    public QsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public QsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }

    private void setCustomFont(Context context){
        Typeface customFont = FontCache.getTypeface("Quicksand-Light.otf",context);
        setTypeface(customFont);
    }
}
