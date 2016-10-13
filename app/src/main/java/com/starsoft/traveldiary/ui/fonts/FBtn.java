package com.starsoft.traveldiary.ui.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by Aashish on 9/10/2016.
 */
public class FBtn extends AppCompatButton {
    public FBtn(Context context) {
        super(context);
        init(context);
    }

    public FBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        Typeface font_type= FontCache.getTypeface("Museo.otf",context);
        setTypeface(font_type);
    }

}
