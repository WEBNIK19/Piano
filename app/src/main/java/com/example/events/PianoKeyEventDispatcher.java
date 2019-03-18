package com.example.events;

import java.util.ArrayList;
import java.util.Iterator;

public class PianoKeyEventDispatcher implements IPianoKeyEventDispatcher {
    protected ArrayList<PianoKeyListener> listeners = new ArrayList<>();

    @Override
    public void addEventListener(String type, IPianoKeyEventHandler handler) {
        PianoKeyListener listener = new PianoKeyListener(type,handler);
        removeEventListener(type);
        listeners.add(0,listener);
    }

    @Override
    public void removeEventListener(String type) {
        for (Iterator<PianoKeyListener> iterator = listeners.iterator();iterator.hasNext();){
            PianoKeyListener listener = (PianoKeyListener)iterator.next();
            if(listener.getType() == type){
                listeners.remove(listener);
            }
        }
    }

    @Override
    public void dispatchEvent(PianoKeyEvent event) {
        for (Iterator<PianoKeyListener> iterator = listeners.iterator();iterator.hasNext();){
            PianoKeyListener listener = (PianoKeyListener) iterator.next();
            if(event.getStrType() == listener.getType()){
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
}
