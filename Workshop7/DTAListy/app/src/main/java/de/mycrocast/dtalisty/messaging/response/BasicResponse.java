package de.mycrocast.dtalisty.messaging.response;

public class BasicResponse<T> {

    private String error; // TODO enum -> additional task (refactor /  readability)
    private T responseData;

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getResponseData() {
        return this.responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }
}
