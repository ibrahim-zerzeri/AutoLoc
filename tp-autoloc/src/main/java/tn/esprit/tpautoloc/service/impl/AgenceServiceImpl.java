package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.mapper.AgenceMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {
    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;
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

    @Override
    @Transactional
    public AgenceDTO creer(AgenceDTO dto) {
        Agence agence = agenceMapper.toEntity(dto);
        agence.setIdAgence(null);
        Agence saved = agenceRepository.save(agence);
        return agenceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AgenceDTO obtenirParId(Long id) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable, id=" + id));
        return agenceMapper.toDto(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenceDTO> listerToutes() {
        return agenceRepository.findAll().stream()
                .map(agenceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AgenceDTO update(Long id, AgenceDTO dto) {
        Agence existant = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable, id=" + id));
        if (dto.getNom() != null) existant.setNom(dto.getNom());
        if (dto.getVille() != null) existant.setVille(dto.getVille());
        if (dto.getAdresse() != null) existant.setAdresse(dto.getAdresse());
        if (dto.getTelephone() != null) existant.setTelephone(dto.getTelephone());
        Agence saved = agenceRepository.save(existant);
        return agenceMapper.toDto(saved);
    }
}
