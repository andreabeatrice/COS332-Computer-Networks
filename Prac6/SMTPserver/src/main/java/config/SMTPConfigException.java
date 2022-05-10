package config;


public class SMTPConfigException extends RuntimeException {

    public SMTPConfigException(){

    }

    public SMTPConfigException(String message){
        super(message);
    }

    public SMTPConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMTPConfigException(Throwable cause){
        super(cause);
    }
}