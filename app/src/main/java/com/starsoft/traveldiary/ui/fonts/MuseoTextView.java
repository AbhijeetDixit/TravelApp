package com.starsoft.traveldiary.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Aashish on 9/9/2016.
 */
public class MuseoTextView extends TextView {
    public MuseoTextView(Context context) {
        super(context);
        setCustomFont(context);
    }

    public MuseoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public MuseoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }

    private void setCustomFont(Context context){
        Typeface customFont = FontCache.getTypeface("Museo.otf",context);
        setTypeface(customFont);
    }
}
