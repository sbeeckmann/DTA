package messaging.response;

public class BasicResponse {

    private String error; // TODO enum -> additional task (refactor /  readability)
    private Object responseData; // TODO some type safety with an interface or sth -> additional task

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }
}
