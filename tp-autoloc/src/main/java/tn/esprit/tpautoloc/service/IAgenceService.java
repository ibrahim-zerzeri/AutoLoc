package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;

import java.util.List;

public interface IAgenceService {
    Agence create(Agence agence);
    Agence findById(Long id);
    List<Agence> findAll();
    Agence update(Long id, Agence agence);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
    default Agence save(Agence agence) {
        return create(agence);
    }

    // Méthodes DTO (Atelier 7)
    AgenceDTO creer(AgenceDTO dto);
    AgenceDTO obtenirParId(Long id);
    List<AgenceDTO> listerToutes();
    AgenceDTO update(Long id, AgenceDTO dto);
}
