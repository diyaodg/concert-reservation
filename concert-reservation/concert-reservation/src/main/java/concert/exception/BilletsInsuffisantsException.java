package concert.exception;

/**
 * Lancée quand il n'y a pas assez de billets disponibles.
 */
public class BilletsInsuffisantsException extends Exception {
    public BilletsInsuffisantsException(String message) {
        super(message);
    }
}
