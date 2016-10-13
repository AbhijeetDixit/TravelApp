package com.starsoft.traveldiary.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



/**
 * Created by Aashish on 9/23/2016.
 */
public class RalewayRTextView extends TextView {
    public RalewayRTextView(Context context) {
        super(context);
        setCustomFont(context);
    }

    public RalewayRTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public RalewayRTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context);
    }

    private void setCustomFont(Context context){
        Typeface customFont = FontCache.getTypeface("Raleway-Regular.ttf",context);
        setTypeface(customFont);
    }
}
