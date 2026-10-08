package tn.esprit.tpautoloc.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Set;

import tn.esprit.tpautoloc.domain.enums.*;
@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private CategorieVehicule categorie;
    private BigDecimal tarifJournalier;
    private StatutVehicule statut;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "vehicule")
    private Set<Reservation> reservations;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehicule")
    private Set<Maintenance> maintenances;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Equipement> equipements;
    @ManyToOne
    Agence agence;

}