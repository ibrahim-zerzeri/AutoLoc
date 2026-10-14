package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Equipement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IEquipementRepository;
import tn.esprit.tpautoloc.service.IEquipementService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {
    private final IEquipementRepository equipementRepository;
    @Override
    public Equipement create(Equipement equipement) {
        if (equipement.getIdEquipement() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau equipement ne doit pas avoir d'identifiant");
        }
        return equipementRepository.save(equipement);
    }
    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }
    @Override
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }
    @Override
    public Equipement update(Long id, Equipement equipement) {
        Equipement existant = findById(id);
        existant.setLibelle(equipement.getLibelle());
// recopier ici les autres attributs modifiables du equipement (Atelier 2),
// sans toucher à l'identifiant ni à la collection de paiements
        return equipementRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }
    @Override
    public long count() {
        return equipementRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return equipementRepository.existsById(id);
    }
}
