package concert.ui;

import concert.exception.BilletsInsuffisantsException;
import concert.exception.EvenementIntrouvableException;
import concert.exception.ReservationIntrouvableException;
import concert.model.Evenement;
import concert.model.Reservation;
import concert.model.Utilisateur;
import concert.service.GestionEvenements;
import concert.service.GestionReservations;
import concert.service.GestionUtilisateurs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Interface en ligne de commande (Console) de l'application.
 */
public class ConsoleUI {

    private final Scanner scanner;
    private final GestionEvenements gestionEvenements;
    private final GestionReservations gestionReservations;
    private final GestionUtilisateurs gestionUtilisateurs;
    private Utilisateur utilisateurConnecte;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String SEPARATEUR = "═".repeat(65);
    private static final String SEP_FIN    = "─".repeat(65);

    public ConsoleUI() {
        this.scanner             = new Scanner(System.in);
        this.gestionEvenements   = new GestionEvenements();
        this.gestionReservations = new GestionReservations();
        this.gestionUtilisateurs = new GestionUtilisateurs();
    }

    // ─────────────────────────── LANCEMENT ────────────────────────────────────

    public void demarrer() {
        afficherBanniere();
        menuAuthentification();
    }

    private void afficherBanniere() {
        System.out.println(SEPARATEUR);
        System.out.println("  🎵  SYSTÈME DE RÉSERVATION DE BILLETS DE CONCERT  🎵");
        System.out.println(SEPARATEUR);
    }

    // ─────────────────────────── AUTHENTIFICATION ─────────────────────────────

    private void menuAuthentification() {
        while (true) {
            System.out.println("\n  1. Se connecter");
            System.out.println("  2. S'inscrire");
            System.out.println("  0. Quitter");
            System.out.print("\n  Votre choix : ");

            int choix = lireEntier();
            switch (choix) {
                case 1 -> seConnecter();
                case 2 -> sInscrire();
                case 0 -> {
                    System.out.println("\n  Au revoir ! 👋");
                    System.exit(0);
                }
                default -> System.out.println("  ⚠  Choix invalide.");
            }
        }
    }

    private void seConnecter() {
        System.out.println("\n" + SEP_FIN);
        System.out.print("  Email    : ");
        String email = scanner.nextLine().trim();
        System.out.print("  Mot de passe : ");
        String mdp = scanner.nextLine().trim();

        utilisateurConnecte = gestionUtilisateurs.connecter(email, mdp);
        if (utilisateurConnecte != null) {
            System.out.println("\n  ✅  Bienvenue, " + utilisateurConnecte.getNom() + " !");
            if (utilisateurConnecte.estAdmin()) {
                menuAdmin();
            } else {
                menuUtilisateur();
            }
        } else {
            System.out.println("  ❌  Email ou mot de passe incorrect.");
        }
    }

    private void sInscrire() {
        System.out.println("\n" + SEP_FIN);
        System.out.print("  Nom complet  : ");
        String nom = scanner.nextLine().trim();
        System.out.print("  Email        : ");
        String email = scanner.nextLine().trim();
        System.out.print("  Mot de passe : ");
        String mdp = scanner.nextLine().trim();

        if (gestionUtilisateurs.inscrire(nom, email, mdp)) {
            System.out.println("  ✅  Inscription réussie ! Vous pouvez maintenant vous connecter.");
        } else {
            System.out.println("  ❌  Cet email est déjà utilisé.");
        }
    }

    // ─────────────────────────── MENU UTILISATEUR ─────────────────────────────

    private void menuUtilisateur() {
        while (true) {
            System.out.println("\n" + SEPARATEUR);
            System.out.println("  MENU UTILISATEUR — " + utilisateurConnecte.getNom());
            System.out.println(SEPARATEUR);
            System.out.println("  1. Voir les concerts disponibles");
            System.out.println("  2. Réserver des billets");
            System.out.println("  3. Mes réservations");
            System.out.println("  4. Annuler une réservation");
            System.out.println("  0. Se déconnecter");
            System.out.print("\n  Votre choix : ");

            int choix = lireEntier();
            switch (choix) {
                case 1 -> afficherEvenements(false);
                case 2 -> faireUneReservation();
                case 3 -> afficherMesReservations();
                case 4 -> annulerUneReservation();
                case 0 -> { utilisateurConnecte = null; return; }
                default -> System.out.println("  ⚠  Choix invalide.");
            }
        }
    }

    private void afficherEvenements(boolean tousLesEvenements) {
        List<Evenement> liste = tousLesEvenements
                ? gestionEvenements.getTousLesEvenements()
                : gestionEvenements.getEvenementsDisponibles();

        System.out.println("\n" + SEP_FIN);
        if (liste.isEmpty()) {
            System.out.println("  Aucun concert disponible pour le moment.");
        } else {
            System.out.println("  CONCERTS " + (tousLesEvenements ? "(TOUS)" : "DISPONIBLES") + " :\n");
            liste.forEach(e -> System.out.println("  " + e));
        }
        System.out.println(SEP_FIN);
    }

    private void faireUneReservation() {
        afficherEvenements(false);

        if (gestionEvenements.getEvenementsDisponibles().isEmpty()) {
            System.out.println("  Aucun concert disponible.");
            return;
        }

        System.out.print("\n  ID du concert : ");
        int idEvent = lireEntier();
        System.out.print("  Nombre de billets : ");
        int nbBillets = lireEntier();

        try {
            Evenement evenement = gestionEvenements.trouverParId(idEvent);
            Reservation r = gestionReservations.creerReservation(utilisateurConnecte, evenement, nbBillets);
            System.out.println("\n  ✅  Réservation confirmée !");
            System.out.println("  " + r);
        } catch (EvenementIntrouvableException | BilletsInsuffisantsException e) {
            System.out.println("  ❌  " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
    }

    private void afficherMesReservations() {
        List<Reservation> reservations = gestionReservations.getReservationsUtilisateur(utilisateurConnecte);
        System.out.println("\n" + SEP_FIN);
        if (reservations.isEmpty()) {
            System.out.println("  Vous n'avez aucune réservation.");
        } else {
            System.out.println("  MES RÉSERVATIONS :\n");
            reservations.forEach(r -> System.out.println("  " + r));
        }
        System.out.println(SEP_FIN);
    }

    private void annulerUneReservation() {
        afficherMesReservations();
        System.out.print("\n  ID de la réservation à annuler : ");
        int idRes = lireEntier();

        try {
            gestionReservations.annulerReservation(utilisateurConnecte, idRes);
            System.out.println("  ✅  Réservation #" + idRes + " annulée avec succès.");
        } catch (ReservationIntrouvableException e) {
            System.out.println("  ❌  " + e.getMessage());
        }
    }

    // ─────────────────────────── MENU ADMIN ───────────────────────────────────

    private void menuAdmin() {
        while (true) {
            System.out.println("\n" + SEPARATEUR);
            System.out.println("  MENU ADMINISTRATEUR — " + utilisateurConnecte.getNom());
            System.out.println(SEPARATEUR);
            System.out.println("  1. Voir tous les concerts");
            System.out.println("  2. Ajouter un concert");
            System.out.println("  3. Modifier un concert");
            System.out.println("  4. Supprimer un concert");
            System.out.println("  5. Voir toutes les réservations");
            System.out.println("  0. Se déconnecter");
            System.out.print("\n  Votre choix : ");

            int choix = lireEntier();
            switch (choix) {
                case 1 -> afficherEvenements(true);
                case 2 -> ajouterEvenement();
                case 3 -> modifierEvenement();
                case 4 -> supprimerEvenement();
                case 5 -> afficherToutesLesReservations();
                case 0 -> { utilisateurConnecte = null; return; }
                default -> System.out.println("  ⚠  Choix invalide.");
            }
        }
    }

    private void ajouterEvenement() {
        System.out.println("\n" + SEP_FIN);
        System.out.print("  Nom de l'artiste : ");
        String artiste = scanner.nextLine().trim();
        System.out.print("  Date (dd/MM/yyyy) : ");
        LocalDate date = lireDate();
        if (date == null) return;
        System.out.print("  Lieu : ");
        String lieu = scanner.nextLine().trim();
        System.out.print("  Nombre de billets : ");
        int nbBillets = lireEntier();
        System.out.print("  Prix par billet (FCFA) : ");
        double prix = lireDouble();

        gestionEvenements.ajouterEvenement(new Evenement(artiste, date, lieu, nbBillets, prix));
        System.out.println("  ✅  Concert ajouté avec succès !");
    }

    private void modifierEvenement() {
        afficherEvenements(true);
        System.out.print("\n  ID du concert à modifier : ");
        int id = lireEntier();

        try {
            Evenement e = gestionEvenements.trouverParId(id);
            System.out.println("  Concert actuel : " + e);
            System.out.print("  Nouveau nom d'artiste [" + e.getNomArtiste() + "] : ");
            String artiste = scanner.nextLine().trim();
            if (artiste.isEmpty()) artiste = e.getNomArtiste();

            System.out.print("  Nouvelle date [" + e.getDate().format(DATE_FMT) + "] : ");
            String dateStr = scanner.nextLine().trim();
            LocalDate date = dateStr.isEmpty() ? e.getDate() : LocalDate.parse(dateStr, DATE_FMT);

            System.out.print("  Nouveau lieu [" + e.getLieu() + "] : ");
            String lieu = scanner.nextLine().trim();
            if (lieu.isEmpty()) lieu = e.getLieu();

            System.out.print("  Nouveaux billets [" + e.getNbBilletsTotal() + "] : ");
            String nbStr = scanner.nextLine().trim();
            int nb = nbStr.isEmpty() ? e.getNbBilletsTotal() : Integer.parseInt(nbStr);

            System.out.print("  Nouveau prix [" + e.getPrixBillet() + "] : ");
            String prixStr = scanner.nextLine().trim();
            double prix = prixStr.isEmpty() ? e.getPrixBillet() : Double.parseDouble(prixStr);

            gestionEvenements.modifierEvenement(id, artiste, date, lieu, nb, prix);
            System.out.println("  ✅  Concert modifié avec succès !");
        } catch (EvenementIntrouvableException ex) {
            System.out.println("  ❌  " + ex.getMessage());
        } catch (DateTimeParseException | NumberFormatException ex) {
            System.out.println("  ⚠  Format de saisie incorrect.");
        }
    }

    private void supprimerEvenement() {
        afficherEvenements(true);
        System.out.print("\n  ID du concert à supprimer : ");
        int id = lireEntier();

        try {
            gestionEvenements.supprimerEvenement(id);
            System.out.println("  ✅  Concert supprimé avec succès !");
        } catch (EvenementIntrouvableException e) {
            System.out.println("  ❌  " + e.getMessage());
        }
    }

    private void afficherToutesLesReservations() {
        List<Reservation> reservations = gestionReservations.getToutesLesReservations();
        System.out.println("\n" + SEP_FIN);
        if (reservations.isEmpty()) {
            System.out.println("  Aucune réservation enregistrée.");
        } else {
            System.out.println("  TOUTES LES RÉSERVATIONS :\n");
            reservations.forEach(r -> System.out.println(
                    "  [" + r.getUtilisateur().getNom() + "] " + r));
        }
        System.out.println(SEP_FIN);
    }

    // ─────────────────────────── UTILITAIRES ──────────────────────────────────

    private int lireEntier() {
        while (true) {
            try {
                String ligne = scanner.nextLine().trim();
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                System.out.print("  ⚠  Entrez un nombre entier : ");
            }
        }
    }

    private double lireDouble() {
        while (true) {
            try {
                String ligne = scanner.nextLine().trim();
                return Double.parseDouble(ligne);
            } catch (NumberFormatException e) {
                System.out.print("  ⚠  Entrez un nombre valide : ");
            }
        }
    }

    private LocalDate lireDate() {
        while (true) {
            try {
                String ligne = scanner.nextLine().trim();
                return LocalDate.parse(ligne, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.print("  ⚠  Format invalide. Utilisez dd/MM/yyyy : ");
            }
        }
    }
}
