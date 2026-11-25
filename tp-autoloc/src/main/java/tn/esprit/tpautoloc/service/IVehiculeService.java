package tn.esprit.tpautoloc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

import java.time.LocalDate;
import java.util.List;

public interface IVehiculeService {
    Vehicule create(Vehicule vehicule);
    Vehicule findById(Long id);
    List<Vehicule> findAll();
    Vehicule update(Long id, Vehicule vehicule);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
    default Vehicule save(Vehicule vehicule) {
        return create(vehicule);
    }
    void affecterVehiculeAAgence(Long vehiculeId, Long agenceId);
    void desaffecterVehiculeDeAgence(Long vehiculeId);

    // Méthodes DTO (Atelier 7)
    VehiculeDTO creer(VehiculeDTO dto);
    VehiculeDTO obtenirParId(Long id);
    List<VehiculeDTO> listerToutes();
    VehiculeDTO update(Long id, VehiculeDTO dto);

    // Méthodes de recherche & JPQL (Atelier 8)
    List<VehiculeDTO> findByAgenceVille(String ville);
    Page<VehiculeDTO> findByStatut(StatutVehicule statut, Pageable pageable);
    List<VehiculeDTO> findByAgenceVilleAndCategorie(String ville, CategorieVehicule categorie);
    List<VehiculeDTO> rechercherVehiculesDisponibles(String ville, CategorieVehicule categorie, LocalDate dateDebut, LocalDate dateFin);
    List<VehiculeDTO> trouverVehiculesAReleaser(LocalDate aujourdhui);
}
