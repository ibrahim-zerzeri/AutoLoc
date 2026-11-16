package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IContratRepository;
import tn.esprit.tpautoloc.repository.IPaiementRepository;
import tn.esprit.tpautoloc.service.IPaiementService;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;
import tn.esprit.tpautoloc.web.mapper.PaiementMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {
    private final IPaiementRepository paiementRepository;
    private final IContratRepository contratRepository;
    private final PaiementMapper paiementMapper;
    @Override
    public Paiement create(Paiement paiement) {
        if (paiement.getIdPaiement() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau paiement ne doit pas avoir d'identifiant");
        }
        return paiementRepository.save(paiement);
    }
    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }
    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }
    @Override
    public Paiement update(Long id, Paiement paiement) {
        Paiement existant = findById(id);
        existant.setMontant(paiement.getMontant());
// recopier ici les autres attributs modifiables du paiement (Atelier 2),
// sans toucher à l'identifiant ni à la collection de paiements
        return paiementRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paiement", id);
        }
        paiementRepository.deleteById(id);
    }
    @Override
    public long count() {
        return paiementRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return paiementRepository.existsById(id);
    }

    @Override
    @Transactional
    public PaiementDTO creer(PaiementDTO dto) {
        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setIdPaiement(null);
        if (dto.getContratId() != null) {
            Contrat contrat = contratRepository.findById(dto.getContratId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contrat", dto.getContratId()));
            paiement.setContrat(contrat);
        }
        Paiement saved = paiementRepository.save(paiement);
        return paiementMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementDTO obtenirParId(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable, id=" + id));
        return paiementMapper.toDto(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementDTO> listerToutes() {
        return paiementRepository.findAll().stream()
                .map(paiementMapper::toDto)
                .toList();
    }
}
