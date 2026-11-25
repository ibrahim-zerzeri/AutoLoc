package tn.esprit.tpautoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpautoloc.domain.Paiement;

import java.time.LocalDate;
import java.util.List;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {

    // ==========================================
    // Atelier 8 - Exercice 6 : Recherche de paiements
    // ==========================================

    @Query("SELECT p FROM Paiement p WHERE p.contrat.idContrat = :contratId")
    List<Paiement> findByContrat_Id(@Param("contratId") Long contratId);

    List<Paiement> findByContrat_IdContrat(Long contratId);

    List<Paiement> findByDatePaiementBetween(LocalDate debut, LocalDate fin);
}
