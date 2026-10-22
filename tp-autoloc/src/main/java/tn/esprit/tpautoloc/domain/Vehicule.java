package tn.esprit.tpautoloc.domain;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonAlias("id")
    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private CategorieVehicule categorie;
    private BigDecimal tarifJournalier;
    private StatutVehicule statut;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehicule")
    @JsonIgnore
    private Set<Reservation> reservations;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehicule")
    @JsonIgnore
    private Set<Maintenance> maintenances;
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Equipement> equipements;
    @ManyToOne
    private Agence agence;

}