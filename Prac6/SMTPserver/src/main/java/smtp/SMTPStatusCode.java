package smtp;

public enum SMTPStatusCode {

    /*SUCCESS*/
    RESPONSE_220_SERVER_READY(220, "SMTP Service ready."),
    RESPONSE_221_SERVICE_CLOSING(221, "SMTP Service closing."),
    RESPONSE_250_ACTION_TAKEN_AND_COMPLETED(250, "Requested action taken and completed"),
    RESPONSE_354_START_MESSAGE(354, "Start message input and end with "),
    SERVER_ERROR_421_SERVICE_UNAVAILABLE(421, "The service is not available and the connection will be closed."),
    SERVER_ERROR_502_NOT_IMPLEMENTED(502, "Not Implemented"),
    SERVER_ERROR_550_MAILBOX_UNAVAILABLE(550, "The requested command failed because the user’s mailbox was unavailable");

    public final int STATUS_CODE;
    public final String MESSAGE;

    SMTPStatusCode(int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }
}
