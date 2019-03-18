package com.example.events;

public interface IPianoKeyEventDispatcher {
    public void addEventListener(String type,IPianoKeyEventHandler cbInterface);
    public void removeEventListener(String type);
    public void dispatchEvent(PianoKeyEvent event);
    public Boolean hasEventListener(String type);
    public void removeAllListeners();
}
