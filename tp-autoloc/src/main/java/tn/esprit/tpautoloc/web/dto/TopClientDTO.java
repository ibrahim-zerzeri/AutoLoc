package tn.esprit.tpautoloc.web.dto;

/**
 * Projection DTO (Record) pour le classement des clients par nombre de réservations.
 * Atelier 8 - Exercice 8.1
 */
public record TopClientDTO(Long clientId, String nom, String prenom, Long nombreReservations) {
}
