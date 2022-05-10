package smtp;


public class SMTPRequest extends SMTPMessage{

    private SMTPCommand method;
    private String requestTarget;
    private String httpVersion;

    SMTPRequest(){

    }

    public SMTPCommand getMethod() {
        return method;
    }

    void setMethod(String methodName) throws SMTPParsingException {
        for (SMTPCommand method: SMTPCommand.values()){
            if (methodName.equals(method.name())){
                this.method = method;
                return;
            }
        }
        throw new SMTPParsingException(
                SMTPStatusCode.SERVER_ERROR_502_NOT_IMPLEMENTED
        );
        //this.method = HttpMethod.valueOf(methodName);
    }
}

