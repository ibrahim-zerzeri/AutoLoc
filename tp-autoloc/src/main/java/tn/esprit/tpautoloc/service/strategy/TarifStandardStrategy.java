package tn.esprit.tpautoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class TarifStandardStrategy implements ITarificationStrategy, TarificationStrategy {

    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        if (vehicule == null || vehicule.getTarifJournalier() == null) {
            return BigDecimal.ZERO;
        }
        long jours = 1;
        if (reservation != null && reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            jours = Math.max(1, ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin()));
        }
        return vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(jours));
    }

    @Override
    public String getCode() {
        return "STANDARD";
    }
}
