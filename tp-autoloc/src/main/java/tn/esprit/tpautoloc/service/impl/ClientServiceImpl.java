package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.service.IClientService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {
    private final IClientRepository clientRepository;
    @Override
    public Client create(Client client) {
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau client ne doit pas avoir d'identifiant");
        }
        if (client.getNom() == null || client.getNom().trim().isEmpty() ||
                client.getPrenom() == null || client.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("L'attribut d'identification (nom et prénom) est obligatoire");
        }
        return clientRepository.save(client);
    }
    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
    @Override
    public Client update(Long id, Client client) {
        Client existant = findById(id);
        if (client.getNom() != null) {
            if (client.getNom().trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom du client ne peut pas être vide");
            }
            existant.setNom(client.getNom());
        }
        if (client.getPrenom() != null) {
            if (client.getPrenom().trim().isEmpty()) {
                throw new IllegalArgumentException("Le prénom du client ne peut pas être vide");
            }
            existant.setPrenom(client.getPrenom());
        }
        if (client.getEmail() != null) {
            existant.setEmail(client.getEmail());
        }
        if (client.getTelephone() != null) {
            existant.setTelephone(client.getTelephone());
        }
        if (client.getNumPermis() != null) {
            existant.setNumPermis(client.getNumPermis());
        }
        return clientRepository.save(existant);
    }
    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }
    @Override
    public long count() {
        return clientRepository.count();
    }
    @Override
    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }
}
