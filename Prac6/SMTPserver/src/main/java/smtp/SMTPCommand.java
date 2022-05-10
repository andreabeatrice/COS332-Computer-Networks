package smtp;

public enum SMTPCommand {
    HELO("HELO", "HELO<SP><domain><CRLF>"),
    MAIL("MAIL", "MAIL<SP>FROM : <reverse-path><CRLF>"),
    RCPT("RCPT", "RCPT<SP>TO : <forward-path><CRLF>"),
    DATA("DATA", "DATA<CRLF>"),
    QUIT("QUIT", "QUIT<CRLF>");

    public final String NAME;
    public final String FORM;

    SMTPCommand(String NAME, String FORM) {
        this.NAME = NAME;
        this.FORM = FORM;
    }
}
