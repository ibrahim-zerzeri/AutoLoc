package tn.esprit.tpautoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;

import java.util.List;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {

    // ==========================================
    // Atelier 8 - Exercice 4 : Recherche d'employés
    // ==========================================

    List<Employe> findByAgenceNom(String nomAgence);

    @Query("SELECT e FROM Employe e WHERE e.agence.idAgence = :agenceId AND e.role = :role")
    List<Employe> findByAgence_IdAndRole(@Param("agenceId") Long agenceId, @Param("role") RoleEmploye role);

    List<Employe> findByAgence_IdAgenceAndRole(Long agenceId, RoleEmploye role);

    List<Employe> findByNomContaining(String nom);
}
