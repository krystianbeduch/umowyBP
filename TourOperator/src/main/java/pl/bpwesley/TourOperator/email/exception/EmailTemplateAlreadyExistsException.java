package pl.bpwesley.TourOperator.email.exception;

public class EmailTemplateAlreadyExistsException extends RuntimeException {
    public EmailTemplateAlreadyExistsException(String message) {
        super(message);
    }
}
