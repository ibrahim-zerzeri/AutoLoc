package tn.esprit.tpautoloc.service;

import org.springframework.data.domain.Pageable;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;

import java.util.List;

public interface IReservationService {
    Reservation create(Reservation reservation);
    Reservation findById(Long id);
    List<Reservation> findAll();
    Reservation update(Long id, Reservation reservation);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
    default Reservation save(Reservation reservation) {
        return create(reservation);
    }
    Reservation creerReservation(Client client);
    Reservation annulerReservation(Reservation reservation);

    // Méthodes DTO (Atelier 7)
    ReservationResponseDTO creer(ReservationRequestDTO dto);
    ReservationResponseDTO obtenirParId(Long id);
    List<ReservationResponseDTO> listerToutes();

    // Statistiques & Agrégations JPQL (Atelier 8)
    List<TopClientDTO> topClients(Pageable pageable);
}
