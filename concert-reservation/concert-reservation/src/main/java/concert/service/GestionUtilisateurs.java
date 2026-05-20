package concert.service;

import concert.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gère les comptes utilisateurs et l'authentification.
 */
public class GestionUtilisateurs {

    private List<Utilisateur> utilisateurs;

    public GestionUtilisateurs() {
        this.utilisateurs = new ArrayList<>();
        // Comptes par défaut
        utilisateurs.add(new Utilisateur("Admin", "admin@concert.bf", "admin123", true));
        utilisateurs.add(new Utilisateur("Diyaodine Ouédraogo", "diyao@email.com", "1234", false));
        utilisateurs.add(new Utilisateur("Fatou Kaboré", "fatou@email.com", "5678", false));
    }

    /**
     * Authentifie un utilisateur par email et mot de passe.
     * @return l'utilisateur si les identifiants sont corrects, null sinon.
     */
    public Utilisateur connecter(String email, String motDePasse) {
        Optional<Utilisateur> opt = utilisateurs.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.verifierMotDePasse(motDePasse))
                .findFirst();
        return opt.orElse(null);
    }

    /**
     * Inscrit un nouvel utilisateur.
     * @return false si l'email est déjà utilisé.
     */
    public boolean inscrire(String nom, String email, String motDePasse) {
        boolean emailExistant = utilisateurs.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (emailExistant) return false;

        utilisateurs.add(new Utilisateur(nom, email, motDePasse, false));
        return true;
    }

    public List<Utilisateur> getTousLesUtilisateurs() {
        return utilisateurs;
    }
}
