package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

import java.util.List;

public interface IPaiementService {
    Paiement create(Paiement paiement);
    Paiement findById(Long id);
    List<Paiement> findAll();
    Paiement update(Long id, Paiement paiement);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
    default Paiement save(Paiement paiement) {
        return create(paiement);
    }

    // Méthodes DTO (Atelier 7)
    PaiementDTO creer(PaiementDTO dto);
    PaiementDTO obtenirParId(Long id);
    List<PaiementDTO> listerToutes();

    // Recherches Query Methods (Atelier 8)
    List<PaiementDTO> findByContratId(Long contratId);
}
