package concert;

import concert.ui.ConsoleUI;

/**
 * Point d'entrée de l'application de réservation de billets de concert.
 */
public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.demarrer();
    }
}
