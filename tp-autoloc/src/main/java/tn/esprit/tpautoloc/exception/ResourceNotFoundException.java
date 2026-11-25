package tn.esprit.tpautoloc.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String ressource, Long id) {
        super(id != null ? ressource + " introuvable avec lidentifiant " + id : ressource);
    }
}
