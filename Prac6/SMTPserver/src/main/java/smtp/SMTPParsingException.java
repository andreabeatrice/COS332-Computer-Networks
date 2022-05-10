package smtp;

public class SMTPParsingException extends Exception{

    private final SMTPStatusCode errorCode;

    public SMTPParsingException(SMTPStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public SMTPStatusCode getErrorCode() {
        return errorCode;
    }
}
