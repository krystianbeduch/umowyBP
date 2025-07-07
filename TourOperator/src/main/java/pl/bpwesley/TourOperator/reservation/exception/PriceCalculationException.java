package pl.bpwesley.TourOperator.reservation.exception;

public class PriceCalculationException extends RuntimeException {
    public PriceCalculationException(String message) {
        super(message);
    }
}