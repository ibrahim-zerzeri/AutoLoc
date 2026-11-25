package tn.esprit.tpautoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AffectationDesaffectationTest {

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    private Agence agenceA;
    private Agence agenceB;
    private Vehicule vehicule;
    private Employe employe;

    @BeforeEach
    void setUp() {
        Agence a1 = new Agence();
        a1.setNom("Agence Tunis");
        a1.setVille("Tunis");
        agenceA = agenceRepository.save(a1);

        Agence a2 = new Agence();
        a2.setNom("Agence Sousse");
        a2.setVille("Sousse");
        agenceB = agenceRepository.save(a2);

        Vehicule v = new Vehicule();
        v.setImmatriculation("123-TN-456");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.BERLINE);
        v.setTarifJournalier(new BigDecimal("100.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);
        vehicule = vehiculeRepository.save(v);

        Employe emp = new Employe();
        emp.setNom("Ben Salah");
        emp.setPrenom("Ahmed");
        emp.setRole(RoleEmploye.AGENT);
        employe = employeRepository.save(emp);
    }

    @Test
    void testAffecterVehiculeAAgence() {
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceA.getIdAgence());

        Vehicule refreshed = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow();
        assertNotNull(refreshed.getAgence());
        assertEquals(agenceA.getIdAgence(), refreshed.getAgence().getIdAgence());
    }

    @Test
    void testAffecterVehiculeInexistant() {
        assertThrows(ResourceNotFoundException.class, () ->
                vehiculeService.affecterVehiculeAAgence(999999L, agenceA.getIdAgence()));
    }

    @Test
    void testAffecterVehiculeAgenceInexistante() {
        assertThrows(ResourceNotFoundException.class, () ->
                vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), 999999L));
    }

    @Test
    void testTransfertVehiculeDuneAgenceAVersB() {
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceA.getIdAgence());
        Vehicule v1 = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow();
        assertEquals(agenceA.getIdAgence(), v1.getAgence().getIdAgence());

        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceB.getIdAgence());
        Vehicule v2 = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow();
        assertEquals(agenceB.getIdAgence(), v2.getAgence().getIdAgence());
    }

    @Test
    void testDesaffecterVehiculeDeAgence() {
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agenceA.getIdAgence());
        vehiculeService.desaffecterVehiculeDeAgence(vehicule.getIdVehicule());

        Vehicule refreshed = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow();
        assertNull(refreshed.getAgence());
    }

    @Test
    void testAffecterEmployeAAgence() {
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agenceA.getIdAgence());

        Employe refreshed = employeRepository.findById(employe.getIdEmploye()).orElseThrow();
        assertNotNull(refreshed.getAgence());
        assertEquals(agenceA.getIdAgence(), refreshed.getAgence().getIdAgence());
    }

    @Test
    void testDesaffecterEmployeDeAgence() {
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agenceA.getIdAgence());
        employeService.desaffecterEmployeDeAgence(employe.getIdEmploye());

        Employe refreshed = employeRepository.findById(employe.getIdEmploye()).orElseThrow();
        assertNull(refreshed.getAgence());
    }
}
