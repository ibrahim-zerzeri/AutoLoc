package tn.esprit.tpautoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class TarifLongueDureeStrategy implements ITarificationStrategy, TarificationStrategy {

    private static final BigDecimal REMISE_LONGUE_DUREE = new BigDecimal("0.80");

    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        if (vehicule == null || vehicule.getTarifJournalier() == null) {
            return BigDecimal.ZERO;
        }
        long jours = 1;
        if (reservation != null && reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            jours = Math.max(1, ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin()));
        }
        BigDecimal base = vehicule.getTarifJournalier().multiply(BigDecimal.valueOf(jours));
        return base.multiply(REMISE_LONGUE_DUREE);
    }

    @Override
    public String getCode() {
        return "LONGUE_DUREE";
    }
}
