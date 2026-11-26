package tn.esprit.tpautoloc.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.web.dto.ChiffreAffaireAgenceDTO;
import tn.esprit.tpautoloc.web.dto.TopClientDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QueryMethodsAndJpqlTest {

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private IContratRepository contratRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    @Autowired
    private IPaiementRepository paiementRepository;

    private Agence agenceTunis;
    private Agence agenceSousse;
    private Vehicule clio;
    private Vehicule golf;
    private Client clientAli;
    private Client clientSarra;

    @BeforeEach
    void setUp() {
        // 1. Agences
        Agence a1 = new Agence();
        a1.setNom("Agence Tunis Centre");
        a1.setVille("Tunis");
        agenceTunis = agenceRepository.save(a1);

        Agence a2 = new Agence();
        a2.setNom("Agence Sousse Plage");
        a2.setVille("Sousse");
        agenceSousse = agenceRepository.save(a2);

        // 2. Clients
        clientAli = Client.builder()
                .nom("Ben Ali")
                .prenom("Mohamed")
                .email("mohamed.ali@autoloc.tn")
                .numPermis("PERMIS-1001")
                .telephone("+21620000001")
                .dateInscription(LocalDate.now().minusMonths(6))
                .build();
        clientAli = clientRepository.save(clientAli);

        clientSarra = Client.builder()
                .nom("Trabelsi")
                .prenom("Sarra")
                .email("sarra.t@autoloc.tn")
                .numPermis("PERMIS-1002")
                .telephone("+21620000002")
                .dateInscription(LocalDate.now().minusMonths(3))
                .build();
        clientSarra = clientRepository.save(clientSarra);

        // 3. Véhicules
        clio = new Vehicule();
        clio.setImmatriculation("200-TN-1111");
        clio.setMarque("Renault");
        clio.setModele("Clio");
        clio.setCategorie(CategorieVehicule.CITADINE);
        clio.setTarifJournalier(new BigDecimal("80.00"));
        clio.setStatut(StatutVehicule.DISPONIBLE);
        clio.setAgence(agenceTunis);
        clio = vehiculeRepository.save(clio);

        golf = new Vehicule();
        golf.setImmatriculation("200-TN-2222");
        golf.setMarque("Volkswagen");
        golf.setModele("Golf");
        golf.setCategorie(CategorieVehicule.BERLINE);
        golf.setTarifJournalier(new BigDecimal("150.00"));
        golf.setStatut(StatutVehicule.DISPONIBLE);
        golf.setAgence(agenceTunis);
        golf = vehiculeRepository.save(golf);

        // 4. Réservations
        Reservation res1 = Reservation.builder()
                .client(clientAli)
                .vehicule(clio)
                .dateDebut(LocalDate.now().plusDays(2))
                .dateFin(LocalDate.now().plusDays(6))
                .statut(StatutReservation.CONFIRMEE)
                .build();
        res1 = reservationRepository.save(res1);

        Reservation res2 = Reservation.builder()
                .client(clientAli)
                .vehicule(golf)
                .dateDebut(LocalDate.now().plusDays(10))
                .dateFin(LocalDate.now().plusDays(15))
                .statut(StatutReservation.CONFIRMEE)
                .build();
        res2 = reservationRepository.save(res2);

        // 5. Contrats
        Contrat c1 = new Contrat();
        c1.setDateSignature(LocalDate.now());
        c1.setMontantTotal(new BigDecimal("320.00"));
        c1.setValide(true);
        c1.setReservation(res1);
        contratRepository.save(c1);
    }

    // ==========================================
    // Tests Exercice 1 : Query Methods Véhicule
    // ==========================================

    @Test
    void testVehiculeFindByAgenceVille() {
        List<Vehicule> vehicules = vehiculeRepository.findByAgenceVille("Tunis");
        assertThat(vehicules).hasSizeGreaterThanOrEqualTo(2);
        assertTrue(vehicules.stream().allMatch(v -> "Tunis".equals(v.getAgence().getVille())));
    }

    @Test
    void testVehiculeFindByStatutAndAgenceId() {
        List<Vehicule> vehicules = vehiculeRepository.findByStatutAndAgence_Id(
                StatutVehicule.DISPONIBLE, agenceTunis.getIdAgence());
        assertThat(vehicules).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void testVehiculePaginationByStatut() {
        Page<Vehicule> page = vehiculeRepository.findByStatut(
                StatutVehicule.DISPONIBLE, PageRequest.of(0, 5));
        assertNotNull(page);
        assertTrue(page.getTotalElements() >= 2);
    }

    @Test
    void testVehiculeTarifComparisons() {
        List<Vehicule> cheap = vehiculeRepository.findByTarifJournalierLessThan(new BigDecimal("100.00"));
        assertTrue(cheap.stream().anyMatch(v -> v.getIdVehicule().equals(clio.getIdVehicule())));

        List<Vehicule> expensive = vehiculeRepository.findByTarifJournalierGreaterThan(new BigDecimal("100.00"));
        assertTrue(expensive.stream().anyMatch(v -> v.getIdVehicule().equals(golf.getIdVehicule())));

        List<Vehicule> sorted = vehiculeRepository.findAllByOrderByTarifJournalierAsc();
        assertThat(sorted).isNotEmpty();
    }

    @Test
    void testVehiculeExistsAndCount() {
        boolean exists = vehiculeRepository.existsByIdAndStatut(clio.getIdVehicule(), StatutVehicule.DISPONIBLE);
        assertTrue(exists);

        long count = vehiculeRepository.countByStatut(StatutVehicule.DISPONIBLE);
        assertTrue(count >= 2);
    }

    // ==========================================
    // Tests Exercice 2 : Query Methods Client
    // ==========================================

    @Test
    void testClientQueryMethods() {
        Optional<Client> found = clientRepository.findByEmail("mohamed.ali@autoloc.tn");
        assertTrue(found.isPresent());
        assertEquals("Ben Ali", found.get().getNom());

        Optional<Client> byPermis = clientRepository.findByNumPermis("PERMIS-1001");
        assertTrue(byPermis.isPresent());

        List<Client> matching = clientRepository.findByNomContaining("Tra");
        assertThat(matching).isNotEmpty();

        assertTrue(clientRepository.existsByEmail("mohamed.ali@autoloc.tn"));
        assertFalse(clientRepository.existsByEmail("inexistant@autoloc.tn"));
    }

    // ==========================================
    // Tests Exercice 7 : Requêtes JPQL Véhicules
    // ==========================================

    @Test
    void testRechercherVehiculesDisponiblesSansChevauchement() {
        // La clio a une réservation du +2 au +6.
        // Si on cherche du +10 au +12 à Tunis en ECONOMIQUE : la clio DOIT être disponible !
        LocalDate debut = LocalDate.now().plusDays(10);
        LocalDate fin = LocalDate.now().plusDays(12);

        List<Vehicule> dispos = vehiculeRepository.rechercherVehiculesDisponibles(
                "Tunis", CategorieVehicule.BERLINE, debut, fin);

        assertThat(dispos).extracting(Vehicule::getIdVehicule).contains(clio.getIdVehicule());
    }

    @Test
    void testRechercherVehiculesDisponiblesAvecChevauchementExclu() {
        // La clio est réservée du +2 au +6.
        // Si on cherche du +3 au +5 : la clio NE DOIT PAS être retournée !
        LocalDate debut = LocalDate.now().plusDays(3);
        LocalDate fin = LocalDate.now().plusDays(5);

        List<Vehicule> dispos = vehiculeRepository.rechercherVehiculesDisponibles(
                "Tunis", CategorieVehicule.UTILITAIRE, debut, fin);

        assertThat(dispos).extracting(Vehicule::getIdVehicule).doesNotContain(clio.getIdVehicule());
    }

    // ==========================================
    // Tests Exercice 8 & 9 : Agrégations JPQL et DTO
    // ==========================================

    @Test
    void testTopClients() {
        List<TopClientDTO> top = reservationRepository.topClients(PageRequest.of(0, 5));
        assertThat(top).isNotEmpty();
        // Client Ali a 2 réservations, il doit être en tête
        assertEquals(clientAli.getIdClient(), top.get(0).clientId());
        assertEquals(2L, top.get(0).nombreReservations());
    }

    @Test
    void testChiffreAffaireParAgence() {
        List<ChiffreAffaireAgenceDTO> caList = contratRepository.chiffreAffaireParAgence();
        assertThat(caList).isNotEmpty();
        assertTrue(caList.stream().anyMatch(ca ->
                ca.agenceId().equals(agenceTunis.getIdAgence()) &&
                ca.chiffreAffaires().compareTo(new BigDecimal("320.00")) == 0));
    }
}
