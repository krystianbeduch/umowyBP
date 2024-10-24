package pl.bpwesley.TourOperator.email.exception;

public class EmailTemplateNotFoundException extends RuntimeException {
    public EmailTemplateNotFoundException(String message) {
        super(message);
    }
}
