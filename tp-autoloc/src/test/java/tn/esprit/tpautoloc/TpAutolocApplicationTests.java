package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.repository.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TpAutolocApplicationTests {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    // Optional autonomous repositories
    @Autowired
    private IEmployeRepository employeRepository;

    @Autowired
    private IEquipementRepository equipementRepository;

    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    @Test
    void testAgenceRepository() {
        System.out.println("==================================================");
        System.out.println("               TEST AGENCE REPOSITORY             ");
        System.out.println("==================================================");

        // 1. Instancier et enregistrer une agence avec save()
        Agence agence = new Agence();
        // Set sample attributes matching your Agence entity (e.g., nom, adresse, ville, etc.)
        agence.setNom("Agence Centrale");
        agence.setAdresse("Avenue Habib Bourguiba");

        Agence savedAgence = agenceRepository.save(agence);
        assertNotNull(savedAgence);
        assertNotNull(savedAgence.getIdAgence());
        System.out.println("Agence sauvegardée: " + savedAgence);

        Long agenceId = savedAgence.getIdAgence();

        // 2. Afficher toutes les agences avec findAll()
        List<Agence> agences = agenceRepository.findAll();
        assertFalse(agences.isEmpty());
        System.out.println("Nombre total d'agences trouvées: " + agences.size());
        agences.forEach(a -> System.out.println(" - " + a));

        // 3. Récupérer avec findById()
        Optional<Agence> optionalAgence = agenceRepository.findById(agenceId);
        assertTrue(optionalAgence.isPresent());
        System.out.println("Agence récupérée par ID: " + optionalAgence.get());

        // 4. Vérifier l'existence avec existsById()
        boolean exists = agenceRepository.existsById(agenceId);
        assertTrue(exists);
        System.out.println("L'agence existe en base: " + exists);

        // 5. Afficher le nombre total avec count()
        long count = agenceRepository.count();
        System.out.println("Nombre d'agences (count): " + count);

        // 6. Supprimer avec deleteById() et vérifier la suppression
        agenceRepository.deleteById(agenceId);
        boolean existsAfterDelete = agenceRepository.existsById(agenceId);
        assertFalse(existsAfterDelete);
        System.out.println("L'agence existe-t-elle après suppression ? " + existsAfterDelete);
    }

    @Test
    void testClientRepository() {
        System.out.println("==================================================");
        System.out.println("               TEST CLIENT REPOSITORY             ");
        System.out.println("==================================================");

        // 1. Créer deux clients (adaptez aux attributs réels de votre entité Client)
        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Mohamed");
        c1.setEmail("mohamed.benali@esprit.tn");

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Salma");
        c2.setEmail("salma.trabelsi@esprit.tn");

        // 2. Enregistrer les deux clients
        Client savedC1 = clientRepository.save(c1);
        Client savedC2 = clientRepository.save(c2);
        assertNotNull(savedC1.getIdClient());
        assertNotNull(savedC2.getIdClient());

        // 3. Afficher la liste avec findAll()
        List<Client> clients = clientRepository.findAll();
        System.out.println("Liste des clients en base:");
        clients.forEach(c -> System.out.println(" - " + c));

        // 4. Rechercher un client avec findById()
        Optional<Client> foundClient = clientRepository.findById(savedC1.getIdClient());
        assertTrue(foundClient.isPresent());
        System.out.println("Client trouvé via findById: " + foundClient.get());

        // 5. Tester existsById() avec un id existant puis un inexistant (ex: 999999L)
        boolean existsReal = clientRepository.existsById(savedC1.getIdClient());
        boolean existsFake = clientRepository.existsById(999999L);
        assertTrue(existsReal);
        assertFalse(existsFake);
        System.out.println("existsById pour id réel (" + savedC1.getIdClient() + "): " + existsReal);
        System.out.println("existsById pour id fictif (999999): " + existsFake);

        // 6. Afficher le nombre de clients avec count()
        long totalClients = clientRepository.count();
        System.out.println("Total des clients (count): " + totalClients);
    }
   @Test
    void testVehiculeRepository() {
        System.out.println("==================================================");
        System.out.println("              TEST VEHICULE REPOSITORY            ");
        System.out.println("==================================================");

        // 1. Créer un véhicule (adaptez aux attributs réels de votre entité Vehicule)
        Vehicule v = new Vehicule();
        v.setImmatriculation("123-TN-4567");
        v.setMarque("Renault");
        v.setModele("Clio");

        // 2. Enregistrer avec save()
        Vehicule savedV = vehiculeRepository.save(v);
        assertNotNull(savedV.getIdVehicule());
        Long vehiculeId = savedV.getIdVehicule();
        System.out.println("Véhicule enregistré: " + savedV);

        // 3. Afficher tous les véhicules avec findAll()
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        System.out.println("Liste des véhicules:");
        vehicules.forEach(item -> System.out.println(" - " + item));

        // 4. Afficher le nombre de véhicules avec count()
        long totalVehicules = vehiculeRepository.count();
        System.out.println("Nombre total de véhicules (count): " + totalVehicules);

        // 5. Modifier une propriété puis sauvegarder avec save()
        savedV.setModele("Clio 5");
        Vehicule updatedV = vehiculeRepository.save(savedV);
        assertEquals("Clio 5", updatedV.getModele());
        System.out.println("Véhicule après modification: " + updatedV);

        // 6. Supprimer avec deleteById() et vérifier
        vehiculeRepository.deleteById(vehiculeId);
        boolean stillExists = vehiculeRepository.existsById(vehiculeId);
        assertFalse(stillExists);
        System.out.println("Le véhicule existe-t-il après deleteById ? " + stillExists);
    }

    @Test
    void testAutonomousRepositories() {
        System.out.println("==================================================");
        System.out.println("            TEST AUTONOME: 3 REPOSITORIES         ");
        System.out.println("==================================================");

        // --- 1. Employe ---
        Employe emp = new Employe();
        emp.setNom("Gharbi");
        emp.setPrenom("Karim");
        Employe savedEmp = employeRepository.save(emp);
        assertNotNull(savedEmp.getIdEmploye());
        assertTrue(employeRepository.existsById(savedEmp.getIdEmploye()));
        System.out.println("Employé créé: " + savedEmp);
        employeRepository.deleteById(savedEmp.getIdEmploye());
        assertFalse(employeRepository.existsById(savedEmp.getIdEmploye()));
        System.out.println("Employé supprimé avec succès.");

        // --- 2. Equipement ---
        Equipement eq = new Equipement();
        eq.setLibelle("GPS Navigation");
        Equipement savedEq = equipementRepository.save(eq);
        assertNotNull(savedEq.getIdEquipement());
        assertTrue(equipementRepository.findById(savedEq.getIdEquipement()).isPresent());
        System.out.println("Equipement créé: " + savedEq);
        equipementRepository.deleteById(savedEq.getIdEquipement());
        assertFalse(equipementRepository.existsById(savedEq.getIdEquipement()));
        System.out.println("Equipement supprimé avec succès.");

        // --- 3. Maintenance ---
        Maintenance m = new Maintenance();
        m.setDescription("Vidange et filtres");
        Maintenance savedM = maintenanceRepository.save(m);
        assertNotNull(savedM.getIdMaintenance());
        assertTrue(maintenanceRepository.existsById(savedM.getIdMaintenance()));
        System.out.println("Maintenance créée: " + savedM);
        maintenanceRepository.deleteById(savedM.getIdMaintenance());
        assertFalse(maintenanceRepository.existsById(savedM.getIdMaintenance()));
        System.out.println("Maintenance supprimée avec succès.");
    }






    @Test
    void contextLoads() {
        
    }

}
