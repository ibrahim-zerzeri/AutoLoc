package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.service.IEmployeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {
    private final IEmployeRepository employeRepository;
    @Override
    public Employe create(Employe employe) {
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau employe ne doit pas avoir d'identifiant");
        }
        return employeRepository.save(employe);
    }
    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }
    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }
    @Override
    public Employe update(Long id, Employe employe) {
        Employe existant = findById(id);
        existant.setRole(employe.getRole());
// recopier ici les autres attributs modifiables du employe (Atelier 2),
// sans toucher à l'identifiant ni à la collection de paiements
        return employeRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }
}
