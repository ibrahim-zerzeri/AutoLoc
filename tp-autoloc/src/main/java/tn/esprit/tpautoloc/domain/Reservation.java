package tn.esprit.tpautoloc.domain;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonAlias("id")
    private Long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;
    @OneToOne
    private Contrat contrat;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Vehicule vehicule;

    public Long getId() {
        return idReservation;
    }

    public void setId(Long id) {
        this.idReservation = id;
    }

    public void setStatut(String statutStr) {
        if (statutStr != null) {
            this.statut = StatutReservation.valueOf(statutStr);
        }
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public static class ReservationBuilder {
        public ReservationBuilder statut(String statutStr) {
            if (statutStr != null) {
                this.statut = StatutReservation.valueOf(statutStr);
            }
            return this;
        }

        public ReservationBuilder statut(StatutReservation statut) {
            this.statut = statut;
            return this;
        }
    }
}