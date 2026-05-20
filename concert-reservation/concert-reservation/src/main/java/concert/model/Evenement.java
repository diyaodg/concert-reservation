package concert.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Représente un événement (concert).
 */
public class Evenement {

    private static int compteurId = 1;

    private int id;
    private String nomArtiste;
    private LocalDate date;
    private String lieu;
    private int nbBilletsTotal;
    private int nbBilletsDisponibles;
    private double prixBillet;

    public Evenement(String nomArtiste, LocalDate date, String lieu, int nbBillets, double prixBillet) {
        this.id = compteurId++;
        this.nomArtiste = nomArtiste;
        this.date = date;
        this.lieu = lieu;
        this.nbBilletsTotal = nbBillets;
        this.nbBilletsDisponibles = nbBillets;
        this.prixBillet = prixBillet;
    }

    // Getters
    public int getId() { return id; }
    public String getNomArtiste() { return nomArtiste; }
    public LocalDate getDate() { return date; }
    public String getLieu() { return lieu; }
    public int getNbBilletsTotal() { return nbBilletsTotal; }
    public int getNbBilletsDisponibles() { return nbBilletsDisponibles; }
    public double getPrixBillet() { return prixBillet; }

    // Setters (pour modification par admin)
    public void setNomArtiste(String nomArtiste) { this.nomArtiste = nomArtiste; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public void setPrixBillet(double prixBillet) { this.prixBillet = prixBillet; }

    public void setNbBilletsTotal(int nbBillets) {
        int diff = nbBillets - this.nbBilletsTotal;
        this.nbBilletsTotal = nbBillets;
        this.nbBilletsDisponibles = Math.max(0, this.nbBilletsDisponibles + diff);
    }

    /**
     * Réduit le stock de billets disponibles après une réservation.
     */
    public void reserverBillets(int quantite) {
        this.nbBilletsDisponibles -= quantite;
    }

    /**
     * Remet des billets en stock lors d'une annulation.
     */
    public void libererBillets(int quantite) {
        this.nbBilletsDisponibles = Math.min(nbBilletsTotal, nbBilletsDisponibles + quantite);
    }

    public boolean estDisponible() {
        return nbBilletsDisponibles > 0;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%d] %s | %s | %s | Billets dispo : %d/%d | Prix : %.2f FCFA",
                id, nomArtiste, date.format(fmt), lieu, nbBilletsDisponibles, nbBilletsTotal, prixBillet);
    }
}
