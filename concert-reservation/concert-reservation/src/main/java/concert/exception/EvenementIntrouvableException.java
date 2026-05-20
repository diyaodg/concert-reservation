package concert.exception;

/**
 * Lancée quand un événement demandé n'existe pas.
 */
public class EvenementIntrouvableException extends Exception {
    public EvenementIntrouvableException(String message) {
        super(message);
    }
}
