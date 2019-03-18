package com.example.piano;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class PianoHorizontalScrollView extends HorizontalScrollView {
    KeyView view = findViewById(R.id.keys);
    public PianoHorizontalScrollView(Context context) {
        super(context);
    }

    public PianoHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PianoHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if(view.keyForCoords(event.getX(),event.getY()) != null){
            return false;
        }
        return super.onTouchEvent(event);
    }*/
}
