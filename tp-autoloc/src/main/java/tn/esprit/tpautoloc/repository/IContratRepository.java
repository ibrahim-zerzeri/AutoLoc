package tn.esprit.tpautoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO;

import java.time.LocalDate;
import java.util.List;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    // ==========================================
    // Atelier 8 - Exercice 9 : Agrégations sur contrats
    // ==========================================

    /**
     * 9.1. Chiffre d'affaires agrégé par agence à partir des contrats valides.
     */
    @Query("SELECT new tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO(" +
            "a.idAgence, a.nom, SUM(c.montantTotal)) " +
            "FROM Contrat c " +
            "JOIN c.reservation r " +
            "JOIN r.vehicule v " +
            "JOIN v.agence a " +
            "WHERE c.valide = true " +
            "GROUP BY a.idAgence, a.nom")
    List<ChiffreAffaireAgenceDTO> chiffreAffaireParAgence();

    /**
     * 9.2. Contrats arrivant à échéance sous un délai donné (selon la date de fin de réservation).
     */
    @Query("SELECT c FROM Contrat c " +
            "JOIN c.reservation r " +
            "WHERE r.dateFin BETWEEN :aujourdhui AND :dateLimite")
    List<Contrat> trouverContratsAEcheanceProche(
            @Param("aujourdhui") LocalDate aujourdhui,
            @Param("dateLimite") LocalDate dateLimite);
}
