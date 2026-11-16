package tn.esprit.tpautoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void testCreerReservationEtObserver(CapturedOutput output) {
        Client client = Client.builder()
                .idClient(1L)
                .nom("Ben Ali")
                .prenom("Mohamed")
                .email("mohamed.benali@esprit.tn")
                .telephone("+21620123456")
                .build();

        Reservation reservation = reservationService.creerReservation(client);

        assertNotNull(reservation);
        assertEquals(client, reservation.getClient());
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());

        // Vérification des logs observateurs dans la console
        assertThat(output.getOut())
                .contains("[EMAIL] Confirmation envoyee au client mohamed.benali@esprit.tn")
                .contains("[SMS] Confirmation envoyee au +21620123456");
    }

    @Test
    void testAnnulerReservationEtObserver(CapturedOutput output) {
        Client client = Client.builder()
                .idClient(2L)
                .nom("Trabelsi")
                .prenom("Sarra")
                .email("sarra.trabelsi@esprit.tn")
                .telephone("+21622987654")
                .build();

        Reservation reservation = reservationService.creerReservation(client);
        reservation.setId(42L);

        Reservation annulee = reservationService.annulerReservation(reservation);

        assertNotNull(annulee);
        assertEquals(StatutReservation.ANNULEE, annulee.getStatut());

        // Vérification des logs observateurs dans la console
        assertThat(output.getOut())
                .contains("[EMAIL] Annulation notifiee au client sarra.trabelsi@esprit.tn")
                .contains("[SMS] Annulation notifiee au +21622987654");
    }
}
