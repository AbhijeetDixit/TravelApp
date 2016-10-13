package com.starsoft.traveldiary.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextSwitcher;

/**
 * Created by Aashish on 9/10/2016.
 */
public class TSwitcher extends TextSwitcher {
    public TSwitcher(Context context) {
        super(context);
    }

    public TSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context){
        Typeface font_type= FontCache.getTypeface("Museo.otf",context);

    }
}
