package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion des véhicules, recherches avancées et requêtes JPQL")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister tous les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée")
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> findAll() {
        return ResponseEntity.ok(vehiculeService.listerToutes());
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.obtenirParId(id));
    }

    @Operation(summary = "Créer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou valeur numérique négative")
    })
    @PostMapping
    public ResponseEntity<VehiculeDTO> create(@Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculeService.creer(dto));
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou valeur numérique négative"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> update(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
            @Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.ok(vehiculeService.update(id, dto));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Affecter un véhicule à une agence")
    @PutMapping("/affecterVehicule/{agenceId}/{vehiculeId}")
    public ResponseEntity<Void> affecterVehiculeAgence(
            @PathVariable Long vehiculeId,
            @PathVariable Long agenceId) {
        vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Désaffecter un véhicule de son agence")
    @PutMapping("/desaffecterVehiculeDeAgence/{vehiculeId}")
    public ResponseEntity<Void> desaffecterVehiculeDeAgence(@PathVariable Long vehiculeId) {
        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // Atelier 8 - Recherche par Query Methods (Partie I)
    // ==========================================

    @Operation(summary = "Recherche de véhicules par ville d'agence (Query Method)")
    @GetMapping("/ville")
    public ResponseEntity<List<VehiculeDTO>> findByAgenceVille(@RequestParam String ville) {
        return ResponseEntity.ok(vehiculeService.findByAgenceVille(ville));
    }

    @Operation(summary = "Recherche de véhicules par statut avec pagination (Query Method)")
    @GetMapping("/statut")
    public ResponseEntity<Page<VehiculeDTO>> findByStatut(
            @RequestParam StatutVehicule statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(vehiculeService.findByStatut(statut, PageRequest.of(page, size)));
    }

    @Operation(summary = "Recherche de véhicules par ville et catégorie")
    @GetMapping("/recherche")
    public ResponseEntity<List<VehiculeDTO>> findByAgenceVilleAndCategorie(
            @RequestParam String ville,
            @RequestParam CategorieVehicule categorie) {
        return ResponseEntity.ok(vehiculeService.findByAgenceVilleAndCategorie(ville, categorie));
    }

    // ==========================================
    // Atelier 8 - Recherches personnalisées JPQL (Partie II)
    // ==========================================

    @Operation(summary = "Recherche de véhicules disponibles par ville, catégorie et période (JPQL)")
    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculeDTO>> rechercherDisponibles(
            @RequestParam String ville,
            @RequestParam CategorieVehicule categorie,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(vehiculeService.rechercherVehiculesDisponibles(ville, categorie, dateDebut, dateFin));
    }

    @Operation(summary = "Véhicules dont la réservation se termine à la date donnée (JPQL)")
    @GetMapping("/a-releaser")
    public ResponseEntity<List<VehiculeDTO>> trouverVehiculesAReleaser(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate cible = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(vehiculeService.trouverVehiculesAReleaser(cible));
    }
}
