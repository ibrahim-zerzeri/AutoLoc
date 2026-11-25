package tn.esprit.tpautoloc.web.dto;

import java.math.BigDecimal;

/**
 * Projection DTO (Record) pour le calcul du chiffre d'affaires agrégé par agence.
 * Atelier 8 - Exercice 9.1
 */
public record ChiffreAffaireAgenceDTO(Long agenceId, String nomAgence, BigDecimal chiffreAffaires) {
}
