package tn.esprit.tpautoloc.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;
import tn.esprit.tpautoloc.web.mapper.VehiculeMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {
    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;
    @Override
    public Vehicule create(Vehicule vehicule) {
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        if (vehicule.getTarifJournalier() != null && vehicule.getTarifJournalier().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
        return vehiculeRepository.save(vehicule);
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
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existant = findById(id);
        if (vehicule.getTarifJournalier() != null) {
            if (vehicule.getTarifJournalier().compareTo(java.math.BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
            }
            existant.setTarifJournalier(vehicule.getTarifJournalier());
        }
        if (vehicule.getImmatriculation() != null) {
            existant.setImmatriculation(vehicule.getImmatriculation());
        }
        if (vehicule.getMarque() != null) {
            existant.setMarque(vehicule.getMarque());
        }
        if (vehicule.getModele() != null) {
            existant.setModele(vehicule.getModele());
        }
        if (vehicule.getCategorie() != null) {
            existant.setCategorie(vehicule.getCategorie());
        }
        if (vehicule.getStatut() != null) {
            existant.setStatut(vehicule.getStatut());
        }
        if (vehicule.getAgence() != null) {
            existant.setAgence(vehicule.getAgence());
        }
        return vehiculeRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }
    @Override
    public long count() {
        return vehiculeRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return vehiculeRepository.existsById(id);
    }
    @Transactional
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId){
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable",vehiculeId));
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable",agenceId));
        vehicule.setAgence(agence);
        vehiculeRepository.save(vehicule);
    }
    @Transactional
     public void  desaffecterVehiculeDeAgence(Long vehiculeId){
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable",vehiculeId));
        vehicule.setAgence(null);
        vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public VehiculeDTO creer(VehiculeDTO dto) {
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        vehicule.setIdVehicule(null);
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", dto.getAgenceId()));
            vehicule.setAgence(agence);
        }
        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public VehiculeDTO obtenirParId(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + id,id));
        return vehiculeMapper.toDto(vehicule);
    }

    @Override
    @Transactional
    public List<VehiculeDTO> listerToutes() {
        return vehiculeRepository.findAll().stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public VehiculeDTO update(Long id, VehiculeDTO dto) {
        Vehicule existant = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + id,id));
        if (dto.getImmatriculation() != null) existant.setImmatriculation(dto.getImmatriculation());
        if (dto.getMarque() != null) existant.setMarque(dto.getMarque());
        if (dto.getModele() != null) existant.setModele(dto.getModele());
        if (dto.getCategorie() != null) existant.setCategorie(dto.getCategorie());
        if (dto.getTarifJournalier() != null) existant.setTarifJournalier(dto.getTarifJournalier());
        if (dto.getStatut() != null) existant.setStatut(dto.getStatut());
        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable", dto.getAgenceId()));
            existant.setAgence(agence);
        }
        Vehicule saved = vehiculeRepository.save(existant);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public List<VehiculeDTO> findByAgenceVille(String ville) {
        return vehiculeRepository.findByAgenceVille(ville).stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public Page<VehiculeDTO> findByStatut(StatutVehicule statut, Pageable pageable) {
        return vehiculeRepository.findByStatut(statut, pageable)
                .map(vehiculeMapper::toDto);
    }

    @Override
    @Transactional
    public List<VehiculeDTO> findByAgenceVilleAndCategorie(String ville, CategorieVehicule categorie) {
        return vehiculeRepository.findByAgence_VilleAndCategorie(ville, categorie).stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<VehiculeDTO> rechercherVehiculesDisponibles(String ville, CategorieVehicule categorie, LocalDate dateDebut, LocalDate dateFin) {
        return vehiculeRepository.rechercherVehiculesDisponibles(ville, categorie, dateDebut, dateFin).stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<VehiculeDTO> trouverVehiculesAReleaser(LocalDate aujourdhui) {
        return vehiculeRepository.trouverVehiculesAReleaser(aujourdhui).stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }
}
