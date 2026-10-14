package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.repository.*;
import tn.esprit.tpautoloc.service.*;

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

    // ==========================================
    // Injection des Services (Atelier 4 - Part 2)
    // ==========================================
    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IVehiculeService vehiculeService;

    // Services pour l'exercice autonome (Partie 5)
    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IEquipementService equipementService;

    @Autowired
    private IMaintenanceService maintenanceService;

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

        // 7. Supprimer les clients de test avec deleteById() et vérifier
        clientRepository.deleteById(savedC1.getIdClient());
        clientRepository.deleteById(savedC2.getIdClient());
        assertFalse(clientRepository.existsById(savedC1.getIdClient()));
        assertFalse(clientRepository.existsById(savedC2.getIdClient()));
        System.out.println("Clients supprimés avec succès après le test.");
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

    // =========================================================================
    // ATELIER 4 - PARTIE 2 : TESTS DE LA COUCHE SERVICE
    // =========================================================================

    @Test
    void testAgenceService() {
        System.out.println("==================================================");
        System.out.println("               TEST AGENCE SERVICE                ");
        System.out.println("==================================================");

        // 1. Créer une instance d'Agence avec les attributs disponibles dans l'entité
        Agence agence = new Agence();
        assertNull(agence.getIdAgence());
        agence.setNom("Agence Centrale");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setVille("Tunis");
        agence.setTelephone("71123456");

        // 2. Enregistrer l'agence
        Agence savedAgence = agenceService.create(agence);
        assertNotNull(savedAgence);
        assertNotNull(savedAgence.getIdAgence());

        // 3. Afficher l'objet retourné
        System.out.println("Agence sauvegardée: " + savedAgence);
        Long agenceId = savedAgence.getIdAgence();

        // Tester update() (objectif de l'atelier)
        savedAgence.setVille("Ariana");
        Agence updatedAgence = agenceService.update(agenceId, savedAgence);
        assertEquals("Ariana", updatedAgence.getVille());
        assertNotEquals("Tunis", updatedAgence.getVille());
        System.out.println("Agence après update: " + updatedAgence);

        // 4. Afficher toutes les agences avec findAll()
        List<Agence> agences = agenceService.findAll();
        assertFalse(agences.isEmpty());
        System.out.println("Nombre total d'agences trouvées: " + agences.size());
        agences.forEach(a -> System.out.println(" - " + a));

        // 5. Récupérer une agence avec findById() et afficher le résultat
        Agence foundAgence = agenceService.findById(agenceId);
        assertNotNull(foundAgence);
        assertEquals(agenceId, foundAgence.getIdAgence());
        System.out.println("Agence récupérée par ID: " + foundAgence);

        // 6. Afficher le nombre d'agences avec count()
        long count = agenceService.count();
        assertTrue(count > 0);
        System.out.println("Nombre d'agences (count): " + count);

        // 7. Supprimer l'agence avec deleteById(), puis vérifier qu'elle n'existe plus
        agenceService.deleteById(agenceId);
        boolean existsAfterDelete = agenceService.existsById(agenceId);
        assertFalse(existsAfterDelete);
        System.out.println("L'agence existe-t-elle après suppression ? " + existsAfterDelete);
    }

    @Test
    void testClientService() {
        System.out.println("==================================================");
        System.out.println("               TEST CLIENT SERVICE                ");
        System.out.println("==================================================");

        // 1. Créer deux clients en utilisant les attributs réellement présents dans l'entité Client
        Client c1 = new Client();
        assertNull(c1.getIdClient());
        c1.setNom("Ben Ali");
        c1.setPrenom("Mohamed");
        c1.setEmail("mohamed.benali@esprit.tn");
        c1.setTelephone("98123456");
        c1.setNumPermis("12/123456");

        Client c2 = new Client();
        assertNull(c2.getIdClient());
        c2.setNom("Trabelsi");
        c2.setPrenom("Salma");
        c2.setEmail("salma.trabelsi@esprit.tn");
        c2.setTelephone("98654321");
        c2.setNumPermis("12/654321");

        // 2. Enregistrer les deux clients
        Client savedC1 = clientService.create(c1);
        Client savedC2 = clientService.create(c2);
        assertNotNull(savedC1.getIdClient());
        assertNotNull(savedC2.getIdClient());
        System.out.println("Client 1 sauvegardé: " + savedC1);
        System.out.println("Client 2 sauvegardé: " + savedC2);

        // Tester update()
        savedC1.setNumPermis("12/999999");
        Client updatedC1 = clientService.update(savedC1.getIdClient(), savedC1);
        assertEquals("12/999999", updatedC1.getNumPermis());
        assertNotEquals("12/123456", updatedC1.getNumPermis());
        System.out.println("Client 1 après update: " + updatedC1);

        // 3. Afficher la liste avec findAll()
        List<Client> clients = clientService.findAll();
        assertFalse(clients.isEmpty());
        System.out.println("Liste des clients en base:");
        clients.forEach(c -> System.out.println(" - " + c));

        // 4. Rechercher un client avec findById()
        Client foundClient = clientService.findById(savedC1.getIdClient());
        assertNotNull(foundClient);
        assertEquals("Ben Ali", foundClient.getNom());
        System.out.println("Client trouvé via findById: " + foundClient);

        // 5. Afficher le nombre de clients avec count()
        long totalClients = clientService.count();
        assertTrue(totalClients >= 2);
        System.out.println("Total des clients (count): " + totalClients);

        // 6. Supprimer le client avec deleteById(), puis vérifier qu'elle n'existe plus
        Long idToDelete = savedC1.getIdClient();
        clientService.deleteById(idToDelete);
        boolean existsAfterDelete = clientService.existsById(idToDelete);
        assertFalse(existsAfterDelete);
        System.out.println("Le client existe-t-il après suppression ? " + existsAfterDelete);

        // Nettoyage du 2ème client
        clientService.deleteById(savedC2.getIdClient());
        assertFalse(clientService.existsById(savedC2.getIdClient()));
    }

    @Test
    void testVehiculeService() {
        System.out.println("==================================================");
        System.out.println("              TEST VEHICULE SERVICE               ");
        System.out.println("==================================================");

        // 1. Créer un véhicule avec les attributs de votre entité Vehicule
        Vehicule v = new Vehicule();
        assertNull(v.getIdVehicule());
        v.setImmatriculation("123-TN-4567");
        v.setMarque("Renault");
        v.setModele("Clio");

        // 2. Enregistrer le vehicule
        Vehicule savedV = vehiculeService.create(v);
        assertNotNull(savedV.getIdVehicule());
        Long vehiculeId = savedV.getIdVehicule();
        System.out.println("Véhicule enregistré: " + savedV);

        // Tester update()
        savedV.setModele("Clio 5");
        Vehicule updatedV = vehiculeService.update(vehiculeId, savedV);
        assertEquals("Clio 5", updatedV.getModele());
        assertNotEquals("Clio", updatedV.getModele());
        System.out.println("Véhicule après modification: " + updatedV);

        // 3. Afficher tous les véhicules avec findAll()
        List<Vehicule> vehicules = vehiculeService.findAll();
        assertFalse(vehicules.isEmpty());
        System.out.println("Liste des véhicules:");
        vehicules.forEach(item -> System.out.println(" - " + item));

        // 4. Rechercher une voiture avec findById()
        Vehicule foundVehicule = vehiculeService.findById(vehiculeId);
        assertNotNull(foundVehicule);
        assertEquals("Clio 5", foundVehicule.getModele());
        System.out.println("Véhicule trouvé via findById: " + foundVehicule);

        // 5. Afficher le nombre de véhicules avec count()
        long totalVehicules = vehiculeService.count();
        assertTrue(totalVehicules > 0);
        System.out.println("Nombre total de véhicules (count): " + totalVehicules);

        // 6. Supprimer le véhicule avec deleteById()
        vehiculeService.deleteById(vehiculeId);
        boolean stillExists = vehiculeService.existsById(vehiculeId);
        assertFalse(stillExists);
        System.out.println("Le véhicule existe-t-il après deleteById ? " + stillExists);
    }

    @Test
    void testAutonomousServices() {
        System.out.println("==================================================");
        System.out.println("             TEST AUTONOME: 3 SERVICES            ");
        System.out.println("==================================================");

        // --- 1. EmployeService ---
        Employe emp = new Employe();
        emp.setNom("Gharbi");
        emp.setPrenom("Karim");
        Employe savedEmp = employeService.save(emp);
        assertNotNull(savedEmp.getIdEmploye());
        assertTrue(employeService.existsById(savedEmp.getIdEmploye()));
        System.out.println("Employé créé via service: " + savedEmp);
        Employe foundEmp = employeService.findById(savedEmp.getIdEmploye());
        assertNotNull(foundEmp);
        assertEquals("Gharbi", foundEmp.getNom());
        System.out.println("Employé trouvé: " + foundEmp);
        employeService.deleteById(savedEmp.getIdEmploye());
        assertFalse(employeService.existsById(savedEmp.getIdEmploye()));
        System.out.println("Employé supprimé avec succès.");

        // --- 2. EquipementService ---
        Equipement eq = new Equipement();
        eq.setLibelle("GPS Navigation");
        Equipement savedEq = equipementService.save(eq);
        assertNotNull(savedEq.getIdEquipement());
        assertTrue(equipementService.existsById(savedEq.getIdEquipement()));
        System.out.println("Equipement créé via service: " + savedEq);
        Equipement foundEq = equipementService.findById(savedEq.getIdEquipement());
        assertNotNull(foundEq);
        assertEquals("GPS Navigation", foundEq.getLibelle());
        System.out.println("Equipement trouvé: " + foundEq);
        equipementService.deleteById(savedEq.getIdEquipement());
        assertFalse(equipementService.existsById(savedEq.getIdEquipement()));
        System.out.println("Equipement supprimé avec succès.");

        // --- 3. MaintenanceService ---
        Maintenance m = new Maintenance();
        m.setDescription("Vidange et filtres");
        Maintenance savedM = maintenanceService.save(m);
        assertNotNull(savedM.getIdMaintenance());
        assertTrue(maintenanceService.existsById(savedM.getIdMaintenance()));
        System.out.println("Maintenance créée via service: " + savedM);
        Maintenance foundM = maintenanceService.findById(savedM.getIdMaintenance());
        assertNotNull(foundM);
        assertEquals("Vidange et filtres", foundM.getDescription());
        System.out.println("Maintenance trouvée: " + foundM);
        maintenanceService.deleteById(savedM.getIdMaintenance());
        assertFalse(maintenanceService.existsById(savedM.getIdMaintenance()));
        System.out.println("Maintenance supprimée avec succès.");
    }

    @Test
    void contextLoads() {
        
    }

}
