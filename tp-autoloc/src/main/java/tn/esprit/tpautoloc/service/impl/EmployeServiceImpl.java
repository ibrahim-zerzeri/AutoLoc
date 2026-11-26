package tn.esprit.tpautoloc.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.service.IEmployeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {
    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    @Override
    public Employe create(Employe employe) {
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employé ne doit pas avoir d'identifiant");
        }
        if (employe.getNom() == null || employe.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (employe.getPrenom() == null || employe.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom de l'employé est obligatoire");
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
        if (employe.getNom() != null) {
            if (employe.getNom().trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom de l'employé ne peut pas être vide");
            }
            existant.setNom(employe.getNom());
        }
        if (employe.getPrenom() != null) {
            if (employe.getPrenom().trim().isEmpty()) {
                throw new IllegalArgumentException("Le prénom de l'employé ne peut pas être vide");
            }
            existant.setPrenom(employe.getPrenom());
        }
        if (employe.getRole() != null) {
            existant.setRole(employe.getRole());
        }
        if (employe.getAgence() != null) {
            existant.setAgence(employe.getAgence());
        }
        return employeRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }
    @Override
    public long count() {
        return employeRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return employeRepository.existsById(id);
    }

    @Override
    @Transactional
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", employeId));
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
        employe.setAgence(agence);
        employeRepository.save(employe);
    }

    @Override
    @Transactional
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", employeId));
        employe.setAgence(null);
        employeRepository.save(employe);
    }

    @Override
    @Transactional
    public List<Employe> findByAgenceIdAndRole(Long agenceId, RoleEmploye role) {
        return employeRepository.findByAgence_IdAgenceAndRole(agenceId, role);
    }
}
