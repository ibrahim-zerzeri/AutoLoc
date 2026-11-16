package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

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
}
