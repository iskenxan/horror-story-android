package samatov.space.spookies.model.api.beans;

public class ApiError {

    private int statusCode;
    private String message;


    public ApiError(int code, String message) {
        this.statusCode = code;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
