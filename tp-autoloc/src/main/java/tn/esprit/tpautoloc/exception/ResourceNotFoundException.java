package tn.esprit.tpautoloc.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String ressource , Long id){
        super(ressource + " introuvable avec lidentifiant " + id);
    }


}
