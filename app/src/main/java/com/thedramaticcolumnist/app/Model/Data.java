package com.thedramaticcolumnist.app.Model;

public class Data {

    private String Message;
    private String type;

    public Data( String message, String Type) {

        Message = message;
        type = Type;

    }

    public Data() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}