package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IContratRepository;
import tn.esprit.tpautoloc.service.IContratService;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {
    private final IContratRepository contratRepository;
    @Override
    public Contrat create(Contrat contrat) {
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        if (contrat.getMontantTotal() != null && contrat.getMontantTotal().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
        if (contrat.getPaiements() != null) {
            contrat.getPaiements().forEach(p -> p.setContrat(contrat));
        }
        return contratRepository.save(contrat);
    }
    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }
    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }
    @Override
    public Contrat update(Long id, Contrat contrat) {
        Contrat existant = findById(id);
        if (contrat.getMontantTotal() != null) {
            if (contrat.getMontantTotal().compareTo(java.math.BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
            }
            existant.setMontantTotal(contrat.getMontantTotal());
        }
        if (contrat.getDateSignature() != null) {
            existant.setDateSignature(contrat.getDateSignature());
        }
        existant.setValide(contrat.isValide());
        return contratRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }
    @Override
    public long count() {
        return contratRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return contratRepository.existsById(id);
    }
}
