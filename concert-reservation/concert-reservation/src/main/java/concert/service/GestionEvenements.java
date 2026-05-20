package concert.service;

import concert.exception.EvenementIntrouvableException;
import concert.model.Evenement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsable de la gestion des événements (CRUD admin).
 */
public class GestionEvenements {

    private List<Evenement> evenements;

    public GestionEvenements() {
        this.evenements = new ArrayList<>();
        chargerEvenementsDemo();
    }

    /**
     * Données de démonstration pour tester l'application.
     */
    private void chargerEvenementsDemo() {
        evenements.add(new Evenement("Miss Tanya", LocalDate.of(2026, 8, 15), "Stade du 4-Août, Ouagadougou", 500, 5000));
        evenements.add(new Evenement("Floby", LocalDate.of(2026, 9, 1), "Palais des Sports, Bobo-Dioulasso", 300, 3000));
        evenements.add(new Evenement("Mpap La Legende", LocalDate.of(2026, 10, 20), "CENASA, Ouagadougou", 200, 2500));
        evenements.add(new Evenement("Amzy", LocalDate.of(2026, 11, 5), "Institut Français, Ouagadougou", 150, 4000));
    }

    // ── CRUD ────────────────────────────────────────────────────────────────

    public void ajouterEvenement(Evenement e) {
        evenements.add(e);
    }

    public void modifierEvenement(int id, String nomArtiste, LocalDate date,
                                   String lieu, int nbBillets, double prix)
            throws EvenementIntrouvableException {
        Evenement e = trouverParId(id);
        e.setNomArtiste(nomArtiste);
        e.setDate(date);
        e.setLieu(lieu);
        e.setNbBilletsTotal(nbBillets);
        e.setPrixBillet(prix);
    }

    public void supprimerEvenement(int id) throws EvenementIntrouvableException {
        Evenement e = trouverParId(id);
        evenements.remove(e);
    }

    // ── Consultation ──────────────────────────────────────────────────────────

    public List<Evenement> getTousLesEvenements() {
        return Collections.unmodifiableList(evenements);
    }

    public List<Evenement> getEvenementsDisponibles() {
        return evenements.stream()
                .filter(Evenement::estDisponible)
                .collect(Collectors.toList());
    }

    public Evenement trouverParId(int id) throws EvenementIntrouvableException {
        return evenements.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EvenementIntrouvableException(
                        "Aucun événement trouvé avec l'ID : " + id));
    }
}
