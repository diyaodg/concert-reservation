package concert.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représente une réservation de billets pour un concert.
 */
public class Reservation {

    private static int compteurId = 1;

    private int id;
    private Utilisateur utilisateur;
    private Evenement evenement;
    private int nbBilletsReserves;
    private LocalDateTime dateReservation;
    private double montantTotal;

    public Reservation(Utilisateur utilisateur, Evenement evenement, int nbBillets) {
        this.id = compteurId++;
        this.utilisateur = utilisateur;
        this.evenement = evenement;
        this.nbBilletsReserves = nbBillets;
        this.dateReservation = LocalDateTime.now();
        this.montantTotal = nbBillets * evenement.getPrixBillet();
    }

    // Getters
    public int getId() { return id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public Evenement getEvenement() { return evenement; }
    public int getNbBilletsReserves() { return nbBilletsReserves; }
    public LocalDateTime getDateReservation() { return dateReservation; }
    public double getMontantTotal() { return montantTotal; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format(
            "Réservation #%d | Concert : %s | Billets : %d | Montant : %.2f FCFA | Réservé le : %s",
            id, evenement.getNomArtiste(), nbBilletsReserves, montantTotal, dateReservation.format(fmt)
        );
    }
}
