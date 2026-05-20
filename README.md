#  Système de Réservation de Billets de Concert

Projet Java L2 — Génie Logiciel  
Application console pour gérer les réservations de billets de concerts.

---

##  Fonctionnalités

### Utilisateur
- Consulter les concerts disponibles
- Réserver des billets (avec vérification de disponibilité)
- Voir et annuler ses réservations

### Administrateur
- Ajouter, modifier, supprimer des concerts
- Consulter toutes les réservations

---

##  Structure du projet

```
concert-reservation/
├── src/
│   └── main/
│       └── java/
│           └── concert/
│               ├── Main.java
│               ├── model/
│               │   ├── Evenement.java
│               │   ├── Reservation.java
│               │   └── Utilisateur.java
│               ├── service/
│               │   ├── GestionEvenements.java
│               │   ├── GestionReservations.java
│               │   └── GestionUtilisateurs.java
│               ├── ui/
│               │   └── ConsoleUI.java
│               └── exception/
│                   ├── BilletsInsuffisantsException.java
│                   ├── EvenementIntrouvableException.java
│                   └── ReservationIntrouvableException.java
└── README.md
```

---

```

---

##  Comptes de test

| Rôle          | Email                | Mot de passe |
|---------------|----------------------|--------------|
| Administrateur | admin@concert.bf    | admin123     |
| Utilisateur    | diyao@email.com      | 1234         |
| Utilisateur    | fatou@email.com     | 5678         |

---

##  Concepts Java utilisés

| Concept | Où |
|---|---|
| Classes & encapsulation | `Evenement`, `Reservation`, `Utilisateur` |
| Collections (`ArrayList`, `List`) | `GestionEvenements`, `GestionReservations` |
| Exceptions personnalisées | package `exception` |
| Stream API & lambdas | `GestionReservations`, `GestionUtilisateurs` |
| Interface console interactive | `ConsoleUI` |
| Séparation des responsabilités (MVC-like) | packages `model`, `service`, `ui` |

---

##  Auteur

Projet réalisé dans le cadre du cours de Programmation Orientée Objet — L2 Génie Logiciel.

