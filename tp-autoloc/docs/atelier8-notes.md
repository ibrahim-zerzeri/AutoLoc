# Atelier 8 — Spring Data JPA : Query Methods & Requêtes JPQL

## 1. Objectifs de l'Atelier
- Maîtriser les **Query Methods (méthodes dérivées)** : génération automatique des requêtes SQL/JPQL par Spring Data JPA à partir de la signature de méthode.
- Maîtriser les **requêtes JPQL personnalisées** (`@Query`) pour les cas complexes (recherche de disponibilité sans chevauchement de dates, expirations de contrats).
- Utiliser les **agrégations JPQL** (`COUNT`, `SUM`, `GROUP BY`, `ORDER BY`) combinées avec des **projections DTO sous forme de Java Records** (`TopClientDTO`, `ChiffreAffaireAgenceDTO`).
- Intégrer la **pagination et le tri** (`Pageable`, `Page<T>`).
- Exposer et tester l'ensemble des fonctionnalités via des endpoints REST documentés (SpringDoc / Swagger) et une collection Postman.

---

## 2. Partie I — Méthodes Dérivées (Query Methods)

### Principe Fondamental
Spring Data JPA analyse le nom de la méthode selon une convention stricte :
`[findBy | countBy | existsBy] + [Propriété] + [Opérateur optionnel] + [And | Or] + ...`

### Exemples Implémentés dans AutoLoc

| Repository | Méthode Dérivée | SQL / JPQL Généré Automatiquement |
|---|---|---|
| `IVehiculeRepository` | `findByAgenceVille(String ville)` | `SELECT v FROM Vehicule v WHERE v.agence.ville = :ville` |
| `IVehiculeRepository` | `findByStatut(StatutVehicule s, Pageable p)` | Filtre avec `LIMIT` et `OFFSET` pour la pagination |
| `IVehiculeRepository` | `findByAgence_VilleAndCategorie(String v, CategorieVehicule c)` | Filtre combiné sur l'agence imbriquée et l'énumération |
| `IVehiculeRepository` | `findByTarifJournalierLessThan(Double max)` | `v.tarifJournalier < :max` |
| `IClientRepository` | `findByEmail(String email)` | Recherche exacte |
| `IClientRepository` | `findByNomContaining(String nom)` | `v.nom LIKE %:nom%` |
| `IClientRepository` | `existsByEmail(String email)` | `SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE ...` |
| `IEmployeRepository` | `findByAgence_IdAgenceAndRole(Long agenceId, RoleEmploye role)` | Filtre sur la clé étrangère de l'agence et le rôle |
| `IPaiementRepository` | `findByContrat_IdContrat(Long contratId)` | `p.contrat.idContrat = :contratId` |

---

## 3. Partie II — Requêtes JPQL & Projections DTO (Records)

### 3.1 Pourquoi JPQL ?
JPQL (**Java Persistence Query Language**) travaille sur le **modèle d'objets (Entités et attributs Java)** et non sur les tables de la base de données relationnelle.
- Portable entre SGBD (MySQL, PostgreSQL, Oracle).
- Type-safe au niveau des types d'entités Java.
- Supporte la navigation d'associations (`c.reservation.vehicule.agence`).

### 3.2 Requête de Disponibilité sans Collision de Dates (Exercice 7.1)
Un véhicule est disponible dans une ville pour une période `[dateDebut, dateFin]` si :
1. Son agence se trouve dans la ville indiquée.
2. Sa catégorie correspond à celle demandée.
3. Son statut est `DISPONIBLE`.
4. Il **n'a aucune réservation active** dont la période chevauche `[dateDebut, dateFin]`.

**Formule mathématique de chevauchement de deux intervalles $[A, B]$ et $[C, D]$** :
$A \le D \text{ et } B \ge C$

```java
@Query("""
    SELECT v FROM Vehicule v
    WHERE v.agence.ville = :ville
      AND v.categorie = :categorie
      AND v.statut = tn.esprit.tpautoloc.entity.StatutVehicule.DISPONIBLE
      AND v.idVehicule NOT IN (
          SELECT r.vehicule.idVehicule FROM Reservation r
          WHERE r.statut NOT IN (
              tn.esprit.tpautoloc.entity.StatutReservation.ANNULEE,
              tn.esprit.tpautoloc.entity.StatutReservation.TERMINEE
          )
          AND r.dateDebut <= :dateFin
          AND r.dateFin >= :dateDebut
      )
""")
List<Vehicule> rechercherVehiculesDisponibles(
    @Param("ville") String ville,
    @Param("categorie") CategorieVehicule categorie,
    @Param("dateDebut") LocalDate dateDebut,
    @Param("dateFin") LocalDate dateFin);
```

### 3.3 Projections DTO avec Java Records (Exercices 8.1 & 9.1)
Pour retourner des résultats d'agrégation (`COUNT`, `SUM`) sans créer d'entités JPA artificielles, on utilise le mot-clé JPQL `SELECT new ...` ciblant un Java Record :

1. **Top Clients (`TopClientDTO`)** :
```java
public record TopClientDTO(Long clientId, String nom, String prenom, Long nombreReservations) {}
```
```java
@Query("""
    SELECT new tn.esprit.tpautoloc.web.dto.TopClientDTO(
        c.idClient, c.nom, c.prenom, COUNT(r.idReservation)
    )
    FROM Reservation r JOIN r.client c
    GROUP BY c.idClient, c.nom, c.prenom
    ORDER BY COUNT(r.idReservation) DESC
""")
List<TopClientDTO> topClients(Pageable pageable);
```

2. **Chiffre d'Affaires par Agence (`ChiffreAffaireAgenceDTO`)** :
```java
public record ChiffreAffaireAgenceDTO(Long agenceId, String nomAgence, Double chiffreAffaires) {}
```
```java
@Query("""
    SELECT new tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO(
        a.idAgence, a.nom, COALESCE(SUM(c.montantTotal), 0.0)
    )
    FROM Contrat c
    JOIN c.reservation.vehicule.agence a
    GROUP BY a.idAgence, a.nom
    ORDER BY SUM(c.montantTotal) DESC
""")
List<ChiffreAffaireAgenceDTO> chiffreAffaireParAgence();
```

---

## 4. Endpoints REST exposés

| Méthode | URL | Description | Type |
|---|---|---|---|
| `GET` | `/api/vehicules/ville?ville=Tunis` | Véhicules par ville d'agence | Query Method |
| `GET` | `/api/vehicules/statut?statut=DISPONIBLE&page=0&size=5` | Véhicules par statut avec pagination | Query Method |
| `GET` | `/api/vehicules/recherche?ville=Tunis&categorie=BERLINE` | Véhicules par ville et catégorie | Query Method |
| `GET` | `/api/vehicules/disponibles?ville=...&categorie=...&dateDebut=...&dateFin=...` | Disponibilité sans collision | Requête JPQL |
| `GET` | `/api/vehicules/a-releaser?date=2026-11-25` | Véhicules à libérer à la date | Requête JPQL |
| `GET` | `/api/reservations/top-clients?limit=5` | Classement clients par réservations | JPQL + Record |
| `GET` | `/api/reservations/chiffre-affaires-agences` | CA total par agence | JPQL + Record |
| `GET` | `/api/contrats/echeance-proche?jours=30` | Contrats se terminant sous X jours | Requête JPQL |
| `GET` | `/api/clients/recherche?email=...` | Client par adresse email | Query Method |
| `GET` | `/api/clients/recherche-nom?nom=...` | Clients par nom contenant chaîne | Query Method |
| `GET` | `/api/agences/ville?ville=...` | Agence par ville | Query Method |
| `GET` | `/api/employes/recherche?agenceId=1&role=AGENT` | Employés par agence et rôle | Query Method |
| `GET` | `/api/paiements/contrat/1` | Paiements d'un contrat donné | Query Method |
