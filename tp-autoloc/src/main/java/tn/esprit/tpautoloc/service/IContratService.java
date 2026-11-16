package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.web.dto.ContratDTO;

import java.util.List;

public interface IContratService {
    Contrat create(Contrat contrat);
    Contrat findById(Long id);
    List<Contrat> findAll();
    Contrat update(Long id, Contrat contrat);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
    default Contrat save(Contrat contrat) {
        return create(contrat);
    }

    // Méthodes DTO (Atelier 7)
    ContratDTO creer(ContratDTO dto);
    ContratDTO obtenirParId(Long id);
    List<ContratDTO> listerToutes();
    ContratDTO update(Long id, ContratDTO dto);
}
