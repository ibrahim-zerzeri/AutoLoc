package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.web.dto.ClientDTO;

import java.util.List;

public interface IClientService {
    Client create(Client client);
    Client findById(Long id);
    List<Client> findAll();
    Client update(Long id, Client client);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
    default Client save(Client client) {
        return create(client);
    }

    // Méthodes DTO (Atelier 7)
    ClientDTO creer(ClientDTO dto);
    ClientDTO obtenirParId(Long id);
    List<ClientDTO> listerToutes();
    ClientDTO update(Long id, ClientDTO dto);
}
