package tn.esprit.tpautoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Selectionne dynamiquement la TarificationStrategy adaptee a une reservation
 * (duree du sejour). Toutes les implementations Spring de TarificationStrategy
 * sont injectees automatiquement (Map&lt;code, strategy&gt;).
 */
@Component
public class TarificationStrategyFactory {

    private final Map<String, ITarificationStrategy> strategies = new HashMap<>();

    public TarificationStrategyFactory(List<? extends ITarificationStrategy> strategyList) {
        if (strategyList != null) {
            for (ITarificationStrategy s : strategyList) {
                strategies.put(s.getCode(), s);
            }
        }
    }

    public ITarificationStrategy resoudre(Reservation reservation) {
        if (reservation == null || reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            return strategies.get("STANDARD");
        }
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());

        // Règle 1 : >= 7 jours -> LONGUE_DUREE
        if (jours >= 7) {
            return strategies.get("LONGUE_DUREE");
        }

        // Règle 2 : <= 3 jours et début un week-end (vendredi, samedi, dimanche) -> WEEKEND
        if (jours <= 3 && estWeekend(reservation)) {
            return strategies.get("WEEKEND");
        }

        // Règle 3 : fallback (4 à 6 jours, ou autre) -> STANDARD
        return strategies.get("STANDARD");
    }

    private boolean estWeekend(Reservation reservation) {
        if (reservation == null || reservation.getDateDebut() == null) {
            return false;
        }
        DayOfWeek dow = reservation.getDateDebut().getDayOfWeek();
        return dow == DayOfWeek.FRIDAY || dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
    }
}
