package com.example.automateresponseandroidjava.model;

public class Response {
    private String response;
    private boolean autoResponse;
    private boolean selected;

    public Response(String response, boolean autoResponse) {
        this.response = response;
        this.autoResponse = autoResponse;
        this.selected = false;
    }

    public String getResponse() {
        return response;
    }

    public boolean isAutoResponse() {
        return autoResponse;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
