# Atelier 5 : Spring MVC et SpringDoc OpenAPI

## A. Endpoints

| Contrôleur | Verbe et Route | Service Appelé | Succès | Erreurs |
|---|---|---|---|---|
| **ContratController** | `GET /api/contrats` | `contratService.findAll()` | 200 | — |
| | `GET /api/contrats/{id}` | `contratService.findById(id)` | 200 | 404 |
| | `POST /api/contrats` | `contratService.create(contrat)` | 201 | 400 |
| | `PUT /api/contrats/{id}` | `contratService.update(id, contrat)` | 200 | 400, 404 |
| | `DELETE /api/contrats/{id}` | `contratService.deleteById(id)` | 204 | 404 |
| **PaiementController** | `GET /api/paiements` | `paiementService.findAll()` | 200 | — |
| | `GET /api/paiements/{id}` | `paiementService.findById(id)` | 200 | 404 |
| **AgenceController** | `GET /api/agences` | `agenceService.findAll()` | 200 | — |
| | `GET /api/agences/{id}` | `agenceService.findById(id)` | 200 | 404 |
| | `POST /api/agences` | `agenceService.create(agence)` | 201 | 400 |
| | `PUT /api/agences/{id}` | `agenceService.update(id, agence)` | 200 | 400, 404 |
| | `DELETE /api/agences/{id}` | `agenceService.deleteById(id)` | 204 | 404 |
| **ClientController** | `GET /api/clients` | `clientService.findAll()` | 200 | — |
| | `GET /api/clients/{id}` | `clientService.findById(id)` | 200 | 404 |
| | `POST /api/clients` | `clientService.create(client)` | 201 | 400 |
| | `PUT /api/clients/{id}` | `clientService.update(id, client)` | 200 | 400, 404 |
| | `DELETE /api/clients/{id}` | `clientService.deleteById(id)` | 204 | 404 |
| **VehiculeController** | `GET /api/vehicules` | `vehiculeService.findAll()` | 200 | — |
| | `GET /api/vehicules/{id}` | `vehiculeService.findById(id)` | 200 | 404 |
| | `POST /api/vehicules` | `vehiculeService.create(vehicule)` | 201 | 400 |
| | `PUT /api/vehicules/{id}` | `vehiculeService.update(id, vehicule)` | 200 | 400, 404 |
| | `DELETE /api/vehicules/{id}` | `vehiculeService.deleteById(id)` | 204 | 404 |
| **EmployeController** | `GET /api/employes` | `employeService.findAll()` | 200 | — |
| | `GET /api/employes/{id}` | `employeService.findById(id)` | 200 | 404 |
| | `POST /api/employes` | `employeService.create(employe)` | 201 | 400 |
| | `PUT /api/employes/{id}` | `employeService.update(id, employe)` | 200 | 400, 404 |
| | `DELETE /api/employes/{id}` | `employeService.deleteById(id)` | 204 | 404 |
| **EquipementController** | `GET /api/equipements` | `equipementService.findAll()` | 200 | — |
| | `GET /api/equipements/{id}` | `equipementService.findById(id)` | 200 | 404 |
| | `POST /api/equipements` | `equipementService.create(equipement)` | 201 | 400 |
| | `PUT /api/equipements/{id}` | `equipementService.update(id, equipement)` | 200 | 400, 404 |
| | `DELETE /api/equipements/{id}` | `equipementService.deleteById(id)` | 204 | 404 |
| **ReservationController** | `GET /api/reservations` | `reservationService.findAll()` | 200 | — |
| | `GET /api/reservations/{id}` | `reservationService.findById(id)` | 200 | 404 |
| | `POST /api/reservations` | `reservationService.create(reservation)` | 201 | 400 |
| | `PUT /api/reservations/{id}` | `reservationService.update(id, reservation)` | 200 | 400, 404 |
| | `DELETE /api/reservations/{id}` | `reservationService.deleteById(id)` | 204 | 404 |
| **MaintenanceController** | `GET /api/maintenances` | `maintenanceService.findAll()` | 200 | — |
| | `GET /api/maintenances/{id}` | `maintenanceService.findById(id)` | 200 | 404 |
| | `POST /api/maintenances` | `maintenanceService.create(maintenance)` | 201 | 400 |
| | `PUT /api/maintenances/{id}` | `maintenanceService.update(id, maintenance)` | 200 | 400, 404 |
| | `DELETE /api/maintenances/{id}` | `maintenanceService.deleteById(id)` | 204 | 404 |

---

## B. Annotations Jackson

### 1. Associations retenues et justifications
- **`Contrat` ↔ `Paiement` (Composition pure)** :
  - `Contrat.paiements` est annoté `@JsonManagedReference("contrat-paiements")`.
  - `Paiement.contrat` est annoté `@JsonBackReference("contrat-paiements")`.
  - *Justification* : Les paiements font partie intégrante du cycle de vie du contrat et peuvent être créés directement au sein du corps JSON d'un contrat (`cascade = ALL`). `@JsonManagedReference` sérialise la liste des paiements, et `@JsonBackReference` empêche Jackson de renvoyer le contrat à l'intérieur de chaque paiement pour éviter la boucle infinie.
- **Toutes les autres associations (`Agence` ↔ `Vehicule`, `Agence` ↔ `Employe`, `Client` ↔ `Reservation`, etc.)** :
  - Les collections côté parent sont annotées avec `@JsonIgnore` (`Agence.vehicules`, `Agence.employes`, `Client.reservations`, `Vehicule.reservations`, `Vehicule.maintenances`, `Equipement.vehicules`).
  - Les champs côté enfant (`Employe.agence`, `Reservation.client`, `Reservation.vehicule`, `Maintenance.vehicule`) ne reçoivent **aucune annotation de restriction** (pas de `@JsonBackReference`).
  - *Justification* : Ces entités sont créées indépendamment et désignent leur parent via un objet imbriqué (ex. `{ "agence": { "id": 1 } }`). Si l'on plaçait un `@JsonBackReference` sur l'enfant, Jackson ignorerait la référence lors de la désérialisation (lecture) et le lien en base de données serait nul.

### 2. Différence entre `@JsonBackReference` et `@JsonIgnore`
- **`@JsonBackReference`** : Ne sérialise pas la propriété vers le JSON (écriture masquée), mais lors de la désérialisation (lecture d'un JSON parent-enfant), Jackson reconstruit automatiquement le pointeur vers l'objet parent.
- **`@JsonIgnore`** : Ignore totalement la propriété, aussi bien en sérialisation qu'en désérialisation. Utilisé sur les collections inverses pour empêcher l'arbre JSON d'explorer des sous-graphes infinis.

---

## C. Postman et Swagger

- **Postman** : Outil de test dynamique et rejouable. Il permet d'envoyer des requêtes HTTP personnalisées, de manipuler des variables d'environnement (ex: `{{baseUrl}}`, `{{contratId}}`), d'enchaîner des scénarios complets (créer un contrat avec ses paiements, récupérer son ID, puis vérifier que les paiements apparaissent) et d'automatiser des tests d'assertion (`pm.response.to.have.status(...)`).
- **Swagger UI (SpringDoc OpenAPI)** : Outil de documentation interactive et de contrat d'API. Généré automatiquement à partir des métadonnées du code (`@RestController`, `@Operation`, `@ApiResponse`), il expose la documentation vivante accessible à `/api/swagger-ui.html` et `/api/v3/api-docs` pour visualiser les schémas, les routes et tester les endpoints manuellement dans un navigateur.

---

## D. Anomalies SonarQube

1. **Règle : "Unused imports should be removed"**
   - *Explication* : Des imports inutilisés ou redondants peuvent alourdir le code et créer des dépendances superflues.
   - *Correction* : Suppression des imports superflus dans l'ensemble des contrôleurs et entités.
2. **Règle : "Field injection should be avoided / Use constructor injection"**
   - *Explication* : L'injection par champ via `@Autowired` rend les classes plus difficiles à tester unitairement sans conteneur Spring et masque les dépendances obligatoires.
   - *Correction* : Utilisation systématique de champs `private final` avec l'annotation `@RequiredArgsConstructor` de Lombok pour garantir l'injection par constructeur.

---

## E. Questions de compréhension

1. **Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?**
   - C'est le **`DispatcherServlet`** (le Front Controller de Spring MVC) qui intercepte chaque requête HTTP entrante. Il consulte ensuite le **`HandlerMapping`** qui compare l'URL, le verbe HTTP et les en-têtes avec les métadonnées déclarées sur les contrôleurs (`@RequestMapping`, `@GetMapping`, etc.) pour identifier la méthode exacte à exécuter.

2. **Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?**
   - Pour respecter le principe d'**inversion de contrôle (IoC)**, de **séparation des responsabilités** et de **couplage faible** :
     - Dépendre de l'interface `IContratService` permet d'isoler la couche Web de l'implémentation métier et facilite le remplacement ou le mocking lors des tests.
     - Ne pas appeler directement le Repository garantit que les règles métier, validations et transactions situées dans la couche Service ne sont jamais contournées.

3. **Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?**
   - **`201 Created`** indique formellement que la ressource demandée a été créée avec succès sur le serveur.
   - **`204 No Content`** indique que l'action a réussi (la ressource a été supprimée) et qu'il n'y a plus de corps de contenu à retourner.
   - Une ressource absente renvoie **`404 Not Found`** car il s'agit d'une erreur fonctionnelle côté client (l'identifiant demandé n'existe pas en base), et non d'une anomalie interne inattendue ou d'un crash du serveur (qui donnerait **`500 Internal Server Error`**).

4. **Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?**
   - Parce que le cycle de vie des paiements est intimement lié à celui du contrat : les paiements sont créés et portés par le contrat (`cascade = ALL`). `PaiementService` est donc en lecture seule.
   - Un appel `POST /api/paiements` reçoit une réponse HTTP **`405 Method Not Allowed`** car aucune méthode `@PostMapping` n'est déclarée sur `/paiements`.

5. **Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?**
   - **Risques de l'exposition directe** :
     - Couplage fort entre le schéma de la base de données et l'API publique.
     - Fuite accidentelle de champs sensibles ou internes.
     - Risques de boucles de sérialisation récursives infinies (`StackOverflowError`).
     - Failles de sécurité lors de la désérialisation (sur-assignation de champs comme les IDs ou les statuts).
   - **Justification des annotations** :
     - `Contrat.paiements` utilise `@JsonManagedReference` car c'est une composition : lors de la création d'un contrat, la liste de ses paiements est fournie dans le même corps JSON.
     - `Agence.vehicules` utilise `@JsonIgnore` car un véhicule est créé seul et référence son agence par identifiant (`{ "agence": { "id": 1 } }`). Ignorer la collection inverse évite les boucles tout en permettant à l'entité enfant de désigner librement son agence sans que Jackson n'annule la valeur.
