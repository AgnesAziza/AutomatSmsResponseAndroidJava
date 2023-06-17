package com.example.automateresponseandroidjava.model;

public class Response {
    private String response;
    private boolean autoResponse;

    public Response(String response, boolean autoResponse) {
        this.response = response;
        this.autoResponse = autoResponse;
    }

    public String getResponse() {
        return response;
    }

    public boolean isAutoResponse() {
        return autoResponse;
    }
}
