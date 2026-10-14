package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IReservationRepository;

import tn.esprit.tpautoloc.service.IReservationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    private final IReservationRepository reservationRepository;
    @Override
    public Reservation create(Reservation reservation) {
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle  reservation ne doit pas avoir d'identifiant");
        }
        return reservationRepository.save(reservation);
    }
    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }
    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existant = findById(id);
        existant.setStatut(reservation.getStatut());
// recopier ici les autres attributs modifiables du reservation (Atelier 2),
// sans toucher à l'identifiant ni à la collection de paiements
        return reservationRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        reservationRepository.deleteById(id);
    }
}
