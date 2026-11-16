package tn.esprit.tpautoloc.legacy;

/**
 * Classe représentant la version initiale problématique (AutoLoc v0).
 * Illustre la violation des principes SOLID (SRP, OCP) et l'anti-patron "God Class".
 */
public class AutoLocManager {

    public void louerVehicule(String typeReservation, int nombreJours, String clientEmail) {
        double prix = 0;
        if ("LongDuree".equals(typeReservation)) {
            prix = nombreJours * 120;
        } else if ("Weekend".equals(typeReservation)) {
            prix = nombreJours * 180;
        } else if ("Standard".equals(typeReservation)) {
            prix = nombreJours * 150;
        }
        System.out.println("Contrat créé");
        System.out.println("Prix total : " + prix);
        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }

    public void louerVehicule(String typeVehicule, String typeReservation, int nombreJours, String clientEmail) {
        louerVehicule(typeReservation, nombreJours, clientEmail);
    }

    private void envoyerEmail(String email) {
        System.out.println("Email envoyé à " + email);
    }

    private void envoyerSMS(String sms) {
        System.out.println("SMS envoyé à " + sms);
    }

    private void sauvegarderContrat() {
        System.out.println("Contrat sauvegardé");
    }

    private void enregistrerPaiement() {
        System.out.println("Paiement enregistré");
    }
}
