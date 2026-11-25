package tn.esprit.tpautoloc.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    // ==========================================
    // Atelier 8 - Exercice 3 : Recherche de réservations
    // ==========================================

    List<Reservation> findByClientIdClient(Long idClient);

    List<Reservation> findByClient_IdClient(Long idClient);

    @Query("SELECT r FROM Reservation r WHERE r.vehicule.idVehicule = :vehiculeId AND r.statut <> :statut")
    List<Reservation> findByVehicule_IdAndStatutNot(
            @Param("vehiculeId") Long vehiculeId,
            @Param("statut") StatutReservation statut);

    List<Reservation> findByVehicule_IdVehiculeAndStatutNot(
            Long vehiculeId,
            StatutReservation statut);

    List<Reservation> findByStatut(StatutReservation statut);

    List<Reservation> findByDateDebutBetween(LocalDate debut, LocalDate fin);

    List<Reservation> findByDateFinAfter(LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.statut = :statut AND r.vehicule.agence.idAgence = :agenceId")
    List<Reservation> findByStatutAndVehiculeAgenceId(
            @Param("statut") StatutReservation statut,
            @Param("agenceId") Long agenceId);

    List<Reservation> findByStatutAndVehicule_Agence_IdAgence(
            StatutReservation statut,
            Long agenceId);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.vehicule.idVehicule = :vehiculeId AND r.statut = :statut")
    boolean existsByVehicule_IdAndStatut(
            @Param("vehiculeId") Long vehiculeId,
            @Param("statut") StatutReservation statut);

    boolean existsByVehicule_IdVehiculeAndStatut(
            Long vehiculeId,
            StatutReservation statut);

    // ==========================================
    // Atelier 8 - Exercice 8.1 : Top 5 clients
    // ==========================================

    @Query("SELECT new tn.esprit.tpautoloc.web.dto.TopClientDTO(" +
            "r.client.idClient, r.client.nom, r.client.prenom, COUNT(r)) " +
            "FROM Reservation r " +
            "GROUP BY r.client.idClient, r.client.nom, r.client.prenom " +
            "ORDER BY COUNT(r) DESC")
    List<TopClientDTO> topClients(Pageable pageable);
}
