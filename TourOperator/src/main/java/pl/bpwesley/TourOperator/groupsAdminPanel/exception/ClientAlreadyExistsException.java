package pl.bpwesley.TourOperator.groupsAdminPanel.exception;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
