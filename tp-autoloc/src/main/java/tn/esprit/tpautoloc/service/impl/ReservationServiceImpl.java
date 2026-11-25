package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;
import tn.esprit.tpautoloc.web.mapper.ReservationMapper;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationSubject reservationSubject;
    private final ReservationMapper reservationMapper;
    private final TarificationStrategyFactory tarificationStrategyFactory;
    @Override
    public Reservation create(Reservation reservation) {
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null &&
                reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
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
        if (reservation.getDateDebut() != null) {
            existant.setDateDebut(reservation.getDateDebut());
        }
        if (reservation.getDateFin() != null) {
            existant.setDateFin(reservation.getDateFin());
        }
        if (existant.getDateDebut() != null && existant.getDateFin() != null &&
                existant.getDateFin().isBefore(existant.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
        if (reservation.getStatut() != null) {
            existant.setStatut(reservation.getStatut());
        }
        if (reservation.getClient() != null) {
            existant.setClient(reservation.getClient());
        }
        if (reservation.getVehicule() != null) {
            existant.setVehicule(reservation.getVehicule());
        }
        if (reservation.getContrat() != null) {
            existant.setContrat(reservation.getContrat());
        }
        return reservationRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }
    @Override
    public long count() {
        return reservationRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }

    @Override
    public Reservation creerReservation(Client client) {
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .statut(StatutReservation.CONFIRMEE.name())
                .build();
        reservationSubject.notifierConfirmation(reservation);
        return reservation;
    }

    @Override
    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE.name());
        String email = (reservation.getClient() != null) ? reservation.getClient().getEmail() : "inconnu";
        log.info("Reservation #{} annulée pour le client {}",
                reservation.getId(), email);
        reservationSubject.notifierAnnulation(reservation);
        return reservation;
    }

    @Override
    @Transactional
    public ReservationResponseDTO creer(ReservationRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable, id=" + dto.getClientId(),dto.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + dto.getVehiculeId(),dto.getVehiculeId()));

        Reservation reservation = new Reservation();
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setClient(client);
        reservation.setVehicule(vehicule);

        Reservation saved = reservationRepository.save(reservation);
        reservationSubject.notifierConfirmation(saved);

        ReservationResponseDTO response = reservationMapper.toDto(saved);
        if (tarificationStrategyFactory != null && saved.getVehicule() != null) {
            var strategy = tarificationStrategyFactory.resoudre(saved);
            if (strategy != null) {
                response.setTarifEstime(strategy.calculerTarif(saved.getVehicule(), saved));
            }
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO obtenirParId(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable, id=" + id,id));
        ReservationResponseDTO response = reservationMapper.toDto(reservation);
        if (tarificationStrategyFactory != null && reservation.getVehicule() != null) {
            var strategy = tarificationStrategyFactory.resoudre(reservation);
            if (strategy != null) {
                response.setTarifEstime(strategy.calculerTarif(reservation.getVehicule(), reservation));
            }
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> listerToutes() {
        return reservationRepository.findAll().stream()
                .map(r -> {
                    ReservationResponseDTO dto = reservationMapper.toDto(r);
                    if (tarificationStrategyFactory != null && r.getVehicule() != null) {
                        var strategy = tarificationStrategyFactory.resoudre(r);
                        if (strategy != null) {
                            dto.setTarifEstime(strategy.calculerTarif(r.getVehicule(), r));
                        }
                    }
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopClientDTO> topClients(Pageable pageable) {
        return reservationRepository.topClients(pageable);
    }
}
