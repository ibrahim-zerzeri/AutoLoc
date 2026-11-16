package tn.esprit.tpautoloc.service.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifLongueDureeStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifStandardStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifWeekendStrategy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TarificationStrategyFactoryTest {

    private TarificationStrategyFactory factory;
    private TarifStandardStrategy standard;
    private TarifWeekendStrategy weekend;
    private TarifLongueDureeStrategy longueDuree;

    @BeforeEach
    void setUp() {
        standard = new TarifStandardStrategy();
        weekend = new TarifWeekendStrategy();
        longueDuree = new TarifLongueDureeStrategy();
        factory = new TarificationStrategyFactory(List.of(standard, weekend, longueDuree));
    }

    @Test
    void doitRetournerLongueDureePour7JoursOuPlus() {
        // 1. Construire une réservation avec dateDebut = 2024-01-01, dateFin = 2024-01-08 -> 7 jours
        Reservation reservation = Reservation.builder()
                .dateDebut(LocalDate.of(2024, 1, 1))
                .dateFin(LocalDate.of(2024, 1, 8))
                .build();

        // 2. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 3. Vérifier deux assertions
        assertThat(resultat).isInstanceOf(TarifLongueDureeStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerLongueDureePourExactement7Jours() {
        // 1. Construire une réservation du 2024-05-01 au 2024-05-08 -> exactement 7 jours
        Reservation reservation = Reservation.builder()
                .dateDebut(LocalDate.of(2024, 5, 1))
                .dateFin(LocalDate.of(2024, 5, 8))
                .build();

        // 2. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 3. Vérifier que le code vaut "LONGUE_DUREE"
        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerWeekendPourCourtSejourVendredi() {
        // 1. Choisir une date de début qui est un vendredi — ici 2024-03-08
        // 2. Construire une réservation du 2024-03-08 au 2024-03-10 -> 2 jours (<= 3)
        Reservation reservation = Reservation.builder()
                .dateDebut(LocalDate.of(2024, 3, 8))
                .dateFin(LocalDate.of(2024, 3, 10))
                .build();

        // 3. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 4. Vérifier
        assertThat(resultat).isInstanceOf(TarifWeekendStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("WEEKEND");
    }

    @Test
    void doitRetournerStandardPourMoyenneDuree() {
        // 1. Construire une réservation du 2024-03-04 (lundi) au 2024-03-08 -> 4 jours
        Reservation reservation = Reservation.builder()
                .dateDebut(LocalDate.of(2024, 3, 4))
                .dateFin(LocalDate.of(2024, 3, 8))
                .build();

        // 2. Appeler factory.resoudre(reservation)
        ITarificationStrategy resultat = factory.resoudre(reservation);

        // 3. Vérifier que le code vaut "STANDARD"
        assertThat(resultat).isInstanceOf(TarifStandardStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("STANDARD");
    }
}
