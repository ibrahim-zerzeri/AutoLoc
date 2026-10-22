package tn.esprit.tpautoloc.domain;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "contrat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonAlias("id")
    private Long idContrat;
    private LocalDate dateSignature;
    private  BigDecimal montantTotal;
    private boolean valide;
    @OneToOne (mappedBy = "contrat")
    @JsonIgnore
    private Reservation reservation;
    @OneToMany (cascade = CascadeType.ALL, mappedBy="contrat")
    @JsonManagedReference("contrat-paiements")
    private Set<Paiement> paiements;
}