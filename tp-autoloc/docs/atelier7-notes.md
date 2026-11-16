# Compte Rendu - Atelier 7 : Spring Data JPA DTO, Affectation/Désaffectation, MapStruct, SonarLint & Validation

**Module** : Architecture des Systèmes d'Information (UP ASI 26-27)  
**Application** : AutoLoc — Gestion de location de véhicules  

---

## 1. Partie 1 — Affectation et Désaffectation avec Spring Data JPA

### 1.1 Compréhension des associations
Dans le modèle AutoLoc :
- Une **Agence** possède plusieurs véhicules (`@OneToMany(mappedBy = "agence")`) et plusieurs employés (`@OneToMany(mappedBy = "agence")`).
- Le **Véhicule** et l'**Employé** sont le côté **propriétaire** de l'association car ils portent la clé étrangère (`@ManyToOne private Agence agence`).
- La modification de l'association se fait donc obligatoirement sur l'entité propriétaire (`vehicule.setAgence(...)` ou `employe.setAgence(...)`), puis persistance.

### 1.2 & 1.3 Opérations sur Véhicule
- `affecterVehiculeAAgence(Long vehiculeId, Long agenceId)` :
  - Vérifie l'existence du véhicule et de l'agence (sinon `ResourceNotFoundException`).
  - Met à jour `vehicule.setAgence(agence)`.
  - Transactionnel (`@Transactional`).
  - Endpoint REST : `PUT /api/vehicules/affecterVehicule/{agenceId}/{vehiculeId}` $\rightarrow$ `204 No Content`.
- `desaffecterVehiculeDeAgence(Long vehiculeId)` :
  - Vérifie l'existence du véhicule.
  - Met à jour `vehicule.setAgence(null)`.
  - Transactionnel (`@Transactional`).
  - Endpoint REST : `PUT /api/vehicules/desaffecterVehiculeDeAgence/{vehiculeId}` $\rightarrow$ `204 No Content`.

### 1.4 Opérations sur Employé
- `affecterEmployeAAgence(Long employeId, Long agenceId)` :
  - Endpoint REST : `PUT /api/employes/affecterEmploye/{agenceId}/{employeId}` $\rightarrow$ `204 No Content`.
- `desaffecterEmployeDeAgence(Long employeId)` :
  - Endpoint REST : `PUT /api/employes/desaffecterEmployeDeAgence/{employeId}` $\rightarrow$ `204 No Content`.

---

## 2. Partie 2 & 3 — DTO, MapStruct et Adaptation des Services/Contrôleurs

### 2.1 Rôle des DTO
Les DTO (*Data Transfer Objects*) découplent la couche de présentation (API REST) de la couche de persistance (Entités JPA) :
- Ils évitent d'exposer la structure interne de la base de données.
- Ils éliminent les risques de boucles infinies de sérialisation JSON (`@JsonManagedReference` / `@JsonBackReference`).
- Ils permettent d'adapter les contrats d'échange sans impacter le schéma relationnel.

### 2.2 Liste des DTO créés
| DTO | Attributs |
| :--- | :--- |
| `AgenceDTO` | `id`, `nom`, `ville`, `adresse`, `telephone` |
| `ClientDTO` | `id`, `nom`, `prenom`, `email`, `telephone`, `numPermis`, `dateInscription` |
| `ContratDTO` | `id`, `dateSignature`, `montantTotal`, `valide`, `reservationId` |
| `PaiementDTO` | `id`, `montant`, `datePaiement`, `modePaiement`, `contratId` |
| `ReservationRequestDTO` | `clientId`, `vehiculeId`, `dateDebut`, `dateFin` |
| `ReservationResponseDTO` | `id`, `dateDebut`, `dateFin`, `statut`, `clientId`, `clientNomComplet`, `vehiculeId`, `vehiculeImmatriculation`, `tarifEstime` |
| `VehiculeDTO` | `id`, `immatriculation`, `marque`, `modele`, `categorie`, `tarifJournalier`, `statut`, `agenceId` |

### 2.3 Mappers MapStruct
Générés automatiquement via `mapstruct-processor` et `lombok-mapstruct-binding` :
- `AgenceMapper`, `ClientMapper`, `ContratMapper`, `PaiementMapper`, `VehiculeMapper`.
- `ReservationMapper` avec expressions personnalisées pour :
  - `clientNomComplet = reservation.getClient().getPrenom() + " " + reservation.getClient().getNom()`
  - `clientId = reservation.getClient().getIdClient()`
  - `vehiculeId = reservation.getVehicule().getIdVehicule()`
  - `vehiculeImmatriculation = reservation.getVehicule().getImmatriculation()`

### 2.4 Contrôleurs REST
Tous les contrôleurs renvoient désormais des `ResponseEntity<DTO>` ou `ResponseEntity<List<DTO>>` et n'exposent plus aucune entité JPA en direct.

---

## 3. Partie 4 — Qualité de Code et Analyse SonarLint

Lors de l'analyse avec SonarLint, les bonnes pratiques suivantes ont été appliquées :
1. **Suppression du code mort et imports inutilisés** : Nettoyage des imports inutiles dans les contrôleurs et services.
2. **Gestion explicite des nullités** : Utilisation de `Optional.orElseThrow()` avec exceptions typées (`ResourceNotFoundException`).
3. **Immutabilité et injection par constructeur** : Utilisation de `private final` et Lombok `@RequiredArgsConstructor` évitant l'injection par champ `@Autowired`.
4. **Transactions précises** : Utilisation de `@Transactional(readOnly = true)` sur les méthodes de lecture pour optimiser les performances Hibernate (pas de dirty checking inutile).
5. **Gestion centralisée des erreurs** : `GlobalExceptionHandler` pour intercepter `ResourceNotFoundException`, `IllegalArgumentException` et `MethodArgumentNotValidException` avec des messages clairs et lisibles.

---

## 4. Partie 5 — Validation des Données avec Spring et Validateur Personnalisé

### 4.1 Contraintes Bean Validation
- `ClientDTO` : `@NotBlank` sur le nom/prénom, `@Email` sur l'adresse e-mail.
- `VehiculeDTO` : `@NotBlank` sur l'immatriculation, `@Positive` sur le tarif journalier.
- `PaiementDTO` : `@NotNull` et `@Positive` sur le montant, `@NotNull` sur `modePaiement` et `contratId`.
- `ReservationRequestDTO` : `@NotNull` sur `clientId`, `vehiculeId`, `dateDebut`, `dateFin`.

### 4.2 Validateur Personnalisé (`ReservationRequestValidator`)
Implémente `org.springframework.validation.Validator` pour gérer des règles multi-attributs et temporelles :
- Règle 1 : La date de début ne doit pas être passée (`dto.getDateDebut().isBefore(LocalDate.now())`).
- Règle 2 : La date de fin doit être strictement postérieure à la date de début (`dto.getDateFin().isAfter(dto.getDateDebut())`).

---

## 5. Synthèse des Tests Automatisés
- `AffectationDesaffectationTest` : vérifie l'affectation, la désaffectation et le transfert inter-agences pour les véhicules et les employés.
- `ValidationEtDtoTest` : valide le rejet des données erronées (email invalide, tarif négatif, dates incohérentes) et le succès des requêtes valides.
