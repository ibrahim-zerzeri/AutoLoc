package tn.esprit.tpautoloc.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    // ==========================================
    // Atelier 8 - Exercice 3 : Recherche de réservations
    // ==========================================

    List<Reservation> findByClient_IdClient(Long idClient);

    List<Reservation> findByVehicule_IdVehiculeAndStatutNot(
            Long vehiculeId,
            StatutReservation statut);

    List<Reservation> findByStatut(StatutReservation statut);

    List<Reservation> findByDateDebutBetween(LocalDate debut, LocalDate fin);

    List<Reservation> findByDateFinAfter(LocalDate date);

    List<Reservation> findByStatutAndVehicule_Agence_IdAgence(
            StatutReservation statut,
            Long agenceId);

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
