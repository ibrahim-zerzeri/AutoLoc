package tn.esprit.tpautoloc.web;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.ModelPaiement;
import tn.esprit.tpautoloc.web.dto.ClientDTO;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidationEtDtoTest {

    @Autowired
    private ReservationRequestValidator reservationRequestValidator;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testClientEmailInvalideDoitEtreRejete() {
        ClientDTO dto = ClientDTO.builder()
                .nom("Ben Foulen")
                .prenom("Ali")
                .email("email-invalide") // format incorrect
                .build();

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testVehiculeSansImmatriculationDoitEtreRejete() {
        VehiculeDTO dto = VehiculeDTO.builder()
                .immatriculation("  ") // vide
                .marque("Peugeot")
                .modele("208")
                .categorie(CategorieVehicule.BERLINE)
                .tarifJournalier(new BigDecimal("90.00"))
                .build();

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("immatriculation")));
    }

    @Test
    void testPaiementMontantNulOuNegatifDoitEtreRejete() {
        PaiementDTO dtoZero = PaiementDTO.builder()
                .montant(BigDecimal.ZERO)
                .modePaiement(ModelPaiement.CARTE)
                .contratId(1L)
                .build();

        var violationsZero = validator.validate(dtoZero);
        assertFalse(violationsZero.isEmpty());
        assertTrue(violationsZero.stream().anyMatch(v -> v.getPropertyPath().toString().equals("montant")));

        PaiementDTO dtoNegatif = PaiementDTO.builder()
                .montant(new BigDecimal("-50.00"))
                .modePaiement(ModelPaiement.CARTE)
                .contratId(1L)
                .build();

        var violationsNegatif = validator.validate(dtoNegatif);
        assertFalse(violationsNegatif.isEmpty());
    }

    @Test
    void testReservationSansDateDebutOuFinDoitEtreRejete() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(null)
                .dateFin(null)
                .build();

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateDebut")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateFin")));
    }

    @Test
    void testReservationDateFinAnterieureOuEgaleRejeteeParValidateurPersonnalise() {
        // Date fin antérieure à date début
        ReservationRequestDTO dtoAnterieure = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(LocalDate.now().plusDays(5))
                .dateFin(LocalDate.now().plusDays(2))
                .build();

        Errors errors = new BeanPropertyBindingResult(dtoAnterieure, "reservationRequestDTO");
        reservationRequestValidator.validate(dtoAnterieure, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("dateFin"));

        // Date fin égale à date début
        ReservationRequestDTO dtoEgale = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(LocalDate.now().plusDays(5))
                .dateFin(LocalDate.now().plusDays(5))
                .build();

        Errors errorsEgale = new BeanPropertyBindingResult(dtoEgale, "reservationRequestDTO");
        reservationRequestValidator.validate(dtoEgale, errorsEgale);

        assertTrue(errorsEgale.hasErrors());
        assertTrue(errorsEgale.hasFieldErrors("dateFin"));
    }

    @Test
    void testReservationDatesCoherentesReussit() {
        ReservationRequestDTO dtoValide = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(LocalDate.now().plusDays(1))
                .dateFin(LocalDate.now().plusDays(5))
                .build();

        var violations = validator.validate(dtoValide);
        assertTrue(violations.isEmpty());

        Errors errors = new BeanPropertyBindingResult(dtoValide, "reservationRequestDTO");
        reservationRequestValidator.validate(dtoValide, errors);

        assertFalse(errors.hasErrors());
    }
}
