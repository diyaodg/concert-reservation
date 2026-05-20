package concert.exception;

/**
 * Lancée quand une réservation demandée n'existe pas.
 */
public class ReservationIntrouvableException extends Exception {
    public ReservationIntrouvableException(String message) {
        super(message);
    }
}
