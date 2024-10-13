package com.manager.boats.rental.boats_rental.util;

public class ApiResponse {
    private String messae;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(String messae, Object data) {
        this.messae = messae;
        this.data = data;
    }

    public String getMessae() {
        return messae;
    }

    public void setMessae(String messae) {
        this.messae = messae;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse [data=" + data + ", messae=" + messae + "]";
    }
    
}
