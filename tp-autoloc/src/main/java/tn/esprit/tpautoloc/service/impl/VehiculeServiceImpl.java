package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IVehiculeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {
    private final IVehiculeRepository vehiculeRepository;
    @Override
    public Vehicule create(Vehicule contrat) {
        if (contrat.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau vehicule ne doit pas avoir d'identifiant");
        }
        return vehiculeRepository.save(contrat);
    }
    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }
    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }
    @Override
    public Vehicule update(Long id, Vehicule contrat) {
        Vehicule existant = findById(id);
        existant.setModele(contrat.getModele());
// recopier ici les autres attributs modifiables du vehicule (Atelier 2),
// sans toucher à l'identifiant ni à la collection de paiements
        return vehiculeRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }
}
