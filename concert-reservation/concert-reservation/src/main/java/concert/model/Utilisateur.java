package concert.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente un utilisateur de l'application.
 */
public class Utilisateur {

    private String nom;
    private String email;
    private String motDePasse;
    private boolean estAdmin;
    private List<Reservation> reservations;

    public Utilisateur(String nom, String email, String motDePasse, boolean estAdmin) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.estAdmin = estAdmin;
        this.reservations = new ArrayList<>();
    }

    // Getters
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public boolean estAdmin() { return estAdmin; }
    public List<Reservation> getReservations() { return Collections.unmodifiableList(reservations); }

    // Authentification simple
    public boolean verifierMotDePasse(String mdp) {
        return this.motDePasse.equals(mdp);
    }

    public void ajouterReservation(Reservation r) {
        reservations.add(r);
    }

    public void supprimerReservation(Reservation r) {
        reservations.remove(r);
    }

    @Override
    public String toString() {
        return String.format("Utilisateur : %s (%s) | Admin : %s | Réservations : %d",
                nom, email, estAdmin ? "Oui" : "Non", reservations.size());
    }
}
