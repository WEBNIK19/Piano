package com.example.events;



public class PianoKeyEvent  {

    public static final String DOWN = "down";
    public static final String UP = "up";
    public static final String MOVE = "move";
    protected String StrType = "";
    protected Object params;
    public PianoKeyEvent(String type) {
        initProperties(type,null);
    }
    public PianoKeyEvent(String type, Object params) {

        initProperties(type,params);
    }

    protected void initProperties(String type, Object params){
        StrType = type;
        this.params = params;
    }

    public String getStrType() {
        return StrType;
    }

    public Object getParams(){
        return params;
    }
}
