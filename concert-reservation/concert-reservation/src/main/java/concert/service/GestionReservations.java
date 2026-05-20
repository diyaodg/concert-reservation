package concert.service;

import concert.exception.BilletsInsuffisantsException;
import concert.exception.EvenementIntrouvableException;
import concert.exception.ReservationIntrouvableException;
import concert.model.Evenement;
import concert.model.Reservation;
import concert.model.Utilisateur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsable de la logique de réservation.
 */
public class GestionReservations {

    private List<Reservation> toutesLesReservations;

    public GestionReservations() {
        this.toutesLesReservations = new ArrayList<>();
    }

    /**
     * Crée une réservation après vérification de la disponibilité.
     *
     * @throws BilletsInsuffisantsException si le stock est insuffisant
     * @throws EvenementIntrouvableException si l'événement n'existe pas
     */
    public Reservation creerReservation(Utilisateur utilisateur, Evenement evenement, int nbBillets)
            throws BilletsInsuffisantsException, EvenementIntrouvableException {

        if (evenement == null) {
            throw new EvenementIntrouvableException("L'événement spécifié n'existe pas.");
        }
        if (nbBillets <= 0) {
            throw new IllegalArgumentException("Le nombre de billets doit être supérieur à 0.");
        }
        if (nbBillets > evenement.getNbBilletsDisponibles()) {
            throw new BilletsInsuffisantsException(
                String.format("Seulement %d billet(s) disponible(s) pour '%s'.",
                    evenement.getNbBilletsDisponibles(), evenement.getNomArtiste())
            );
        }

        // Mise à jour du stock
        evenement.reserverBillets(nbBillets);

        // Création et enregistrement
        Reservation reservation = new Reservation(utilisateur, evenement, nbBillets);
        toutesLesReservations.add(reservation);
        utilisateur.ajouterReservation(reservation);

        return reservation;
    }

    /**
     * Annule une réservation et remet les billets en stock.
     */
    public void annulerReservation(Utilisateur utilisateur, int idReservation)
            throws ReservationIntrouvableException {

        Reservation reservation = toutesLesReservations.stream()
                .filter(r -> r.getId() == idReservation
                          && r.getUtilisateur().getEmail().equals(utilisateur.getEmail()))
                .findFirst()
                .orElseThrow(() -> new ReservationIntrouvableException(
                        "Réservation #" + idReservation + " introuvable pour cet utilisateur."));

        // Restitution des billets
        reservation.getEvenement().libererBillets(reservation.getNbBilletsReserves());

        // Suppression
        toutesLesReservations.remove(reservation);
        utilisateur.supprimerReservation(reservation);
    }

    public List<Reservation> getReservationsUtilisateur(Utilisateur utilisateur) {
        return toutesLesReservations.stream()
                .filter(r -> r.getUtilisateur().getEmail().equals(utilisateur.getEmail()))
                .collect(Collectors.toList());
    }

    public List<Reservation> getToutesLesReservations() {
        return Collections.unmodifiableList(toutesLesReservations);
    }
}
