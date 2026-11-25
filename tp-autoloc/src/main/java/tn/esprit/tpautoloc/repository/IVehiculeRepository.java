package tn.esprit.tpautoloc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {

    // ==========================================
    // Atelier 8 - Exercice 1 : Query Methods
    // ==========================================

    List<Vehicule> findByAgenceVille(String ville);

    List<Vehicule> findByAgence_Ville(String ville);

    @Query("SELECT v FROM Vehicule v WHERE v.statut = :statut AND v.agence.idAgence = :agenceId")
    List<Vehicule> findByStatutAndAgence_Id(@Param("statut") StatutVehicule statut, @Param("agenceId") Long agenceId);

    List<Vehicule> findByStatutAndAgence_IdAgence(StatutVehicule statut, Long agenceId);

    Page<Vehicule> findByStatut(StatutVehicule statut, Pageable pageable);

    List<Vehicule> findByAgence_VilleAndCategorie(String ville, CategorieVehicule categorie);

    @Query("SELECT COUNT(v) > 0 FROM Vehicule v WHERE v.idVehicule = :vehiculeId AND v.statut = :statut")
    boolean existsByIdAndStatut(@Param("vehiculeId") Long vehiculeId, @Param("statut") StatutVehicule statut);

    boolean existsByIdVehiculeAndStatut(Long vehiculeId, StatutVehicule statut);

    long countByStatut(StatutVehicule statut);

    List<Vehicule> findByTarifJournalierLessThan(BigDecimal tarif);

    List<Vehicule> findByTarifJournalierGreaterThan(BigDecimal tarif);

    List<Vehicule> findAllByOrderByTarifJournalierAsc();

    // ==========================================
    // Atelier 8 - Exercice 7 : Requêtes JPQL
    // ==========================================

    /**
     * 7.1. Véhicules disponibles par ville, catégorie et période.
     * Filtre sur la ville et la catégorie, vérifie le statut DISPONIBLE,
     * et exclut les véhicules ayant une réservation active qui chevauche l'intervalle [dateDebut, dateFin].
     */
    @Query("SELECT v FROM Vehicule v WHERE v.agence.ville = :ville " +
            "AND v.categorie = :categorie " +
            "AND v.statut = tn.esprit.tpautoloc.domain.enums.StatutVehicule.DISPONIBLE " +
            "AND v.idVehicule NOT IN (" +
            "    SELECT r.vehicule.idVehicule FROM Reservation r " +
            "    WHERE r.vehicule IS NOT NULL " +
            "    AND r.statut <> tn.esprit.tpautoloc.domain.enums.StatutReservation.ANNULEE " +
            "    AND r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut" +
            ")")
    List<Vehicule> rechercherVehiculesDisponibles(
            @Param("ville") String ville,
            @Param("categorie") CategorieVehicule categorie,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    /**
     * 7.2. Véhicules dont la réservation en cours est arrivée à son terme à la date passée.
     */
    @Query("SELECT DISTINCT r.vehicule FROM Reservation r " +
            "WHERE r.dateFin = :aujourdhui " +
            "AND r.statut = tn.esprit.tpautoloc.domain.enums.StatutReservation.CONFIRMEE")
    List<Vehicule> trouverVehiculesAReleaser(@Param("aujourdhui") LocalDate aujourdhui);
}
