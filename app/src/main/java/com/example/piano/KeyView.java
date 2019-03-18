package com.example.piano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.example.events.IPianoKeyEventDispatcher;
import com.example.events.IPianoKeyEventHandler;
import com.example.events.PianoKeyEvent;
import com.example.events.PianoKeyListener;

import java.util.ArrayList;
import java.util.Iterator;

public class KeyView extends View  implements IPianoKeyEventDispatcher {
    public static final int NB = 28;
    private Paint black, yellow, white;
    private ArrayList<Key> whites = new ArrayList<>();
    private ArrayList<Key> blacks = new ArrayList<>();
    private int keyWidth, height;
    private AudioSoundPlayer soundPlayer;

    protected ArrayList<PianoKeyListener> listeners = new ArrayList<>();

    public KeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        black = new Paint();
        black.setColor(Color.BLACK);
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.FILL);
        soundPlayer = new AudioSoundPlayer(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        keyWidth = 137;
        height = h-50;

        int count = 29;

        for (int i = 0; i < NB; i++) {
            int left = i * keyWidth;
            int right = left + keyWidth;

            if (i == NB - 1) {
                right = w;
            }

            RectF rect = new RectF(left, 0, right, height);
            whites.add(new Key(rect, i + 1));

            if (i != 0  &&   i != 3  &&  i != 7  &&  i != 10 &&   i != 14  &&  i != 17  &&  i != 21 &&  i != 24) {
                rect = new RectF((float) (i - 1) * keyWidth + 0.5f * keyWidth + 0.25f * keyWidth, 0,
                        (float) i * keyWidth + 0.25f * keyWidth, 0.67f * height);
                blacks.add(new Key(rect, count));
                count++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Key k : whites) {
            canvas.drawRect(k.rect, k.down ? yellow : white);
        }

        for (int i = 1; i < NB; i++) {
            canvas.drawLine(i * keyWidth, 0, i * keyWidth, height, black);
        }

        for (Key k : blacks) {
            canvas.drawRect(k.rect, k.down ? yellow : black);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN;

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            Key k = keyForCoords(x,y);

            if (k != null) {
                k.down = isDownAction;
            }
        }

        ArrayList<Key> tmp = new ArrayList<>(whites);
        tmp.addAll(blacks);

        for (Key k : tmp) {
            if (k.down) {
                if (!soundPlayer.isNotePlaying(k.sound)) {
                    soundPlayer.playNote(k.sound);
                    invalidate();
                } else {
                    soundPlayer.stopNote(k.sound);
                    releaseKey(k);
                }
            } else {
                releaseKey(k);
            }
        }

        return true;
    }

    public Key keyForCoords(float x, float y) {
        Key result = null;

        for (Key k : whites) {
            if (k.rect.contains(x,y)) {
                result = k;
                break;
            }
        }

        for (Key k : blacks) {
            if (k.rect.contains(x,y)) {
                result = k;
                break;
            }
        }

        return result;
    }

    private void releaseKey(final Key k) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                k.down = false;
                handler.sendEmptyMessage(0);
            }
        }, 100);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    @Override
    public void addEventListener(String type, IPianoKeyEventHandler handler) {
        PianoKeyListener listener = new PianoKeyListener(type,handler);
        removeEventListener(type);
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(String type) {
        for (Iterator<PianoKeyListener> iterator = listeners.iterator();iterator.hasNext();){
            PianoKeyListener listener = iterator.next();
            if (listener.getType() == type){
                listeners.remove(listener);
            }
        }
    }

    @Override
    public void dispatchEvent(PianoKeyEvent event) {
        for (Iterator<PianoKeyListener> iterator = listeners.iterator();iterator.hasNext();){
            PianoKeyListener listener = iterator.next();
            if (event.getStrType() == listener.getType()){
                IPianoKeyEventHandler eventHandler = listener.getHandler();
                eventHandler.callback(event);
            }
        }
    }

    @Override
    public Boolean hasEventListener(String type) {
        return false;
    }

    @Override
    public void removeAllListeners() {

    }

    public void myCallback(){
        PianoKeyEvent event = new PianoKeyEvent(PianoKeyEvent.DOWN);
        dispatchEvent(event);
    }
}
