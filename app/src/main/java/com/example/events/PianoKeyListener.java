package com.example.events;

public class PianoKeyListener {
    private String type;
    private IPianoKeyEventHandler handler;


    public PianoKeyListener(String type, IPianoKeyEventHandler handler) {
        this.type = type;
        this.handler = handler;
    }

    public String getType() {
        return type;
    }

    public IPianoKeyEventHandler getHandler() {
        return handler;
    }
}
