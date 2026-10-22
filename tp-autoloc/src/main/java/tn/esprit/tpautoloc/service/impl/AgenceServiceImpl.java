package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.service.IAgenceService;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {
    private final IAgenceRepository agenceRepository;
    @Override
    public Agence create(Agence agence) {
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        if (agence.getNom() == null || agence.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
        if (agence.getVille() == null || agence.getVille().trim().isEmpty()) {
            throw new IllegalArgumentException("La ville de l'agence est obligatoire");
        }
        return agenceRepository.save(agence);
    }
    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }
    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }
    @Override
    public Agence update(Long id, Agence agence) {
        Agence existant = findById(id);
        if (agence.getNom() != null) {
            if (agence.getNom().trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom de l'agence ne peut pas être vide");
            }
            existant.setNom(agence.getNom());
        }
        if (agence.getVille() != null) {
            if (agence.getVille().trim().isEmpty()) {
                throw new IllegalArgumentException("La ville de l'agence ne peut pas être vide");
            }
            existant.setVille(agence.getVille());
        }
        if (agence.getAdresse() != null) {
            existant.setAdresse(agence.getAdresse());
        }
        if (agence.getTelephone() != null) {
            existant.setTelephone(agence.getTelephone());
        }
        return agenceRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }
    @Override
    public long count() {
        return agenceRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return agenceRepository.existsById(id);
    }
}
