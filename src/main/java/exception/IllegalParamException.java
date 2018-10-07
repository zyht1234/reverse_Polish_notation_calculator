package exception;

public class IllegalParamException extends Exception {
    private int statusCode;

    public IllegalParamException() {
        super();
    }

    public IllegalParamException(String msg) {
        super(msg);
    }

    public IllegalParamException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }


}
