package tn.esprit.tpautoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.service.IClientService;
import tn.esprit.tpautoloc.web.dto.ClientDTO;
import tn.esprit.tpautoloc.web.mapper.ClientMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {
    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;
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

    @Override
    @Transactional
    public ClientDTO creer(ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);
        client.setIdClient(null);
        Client saved = clientRepository.save(client);
        return clientMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO obtenirParId(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable, id=" + id,id));
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> listerToutes() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        Client existant = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable, id=" + id,id));
        if (dto.getNom() != null) existant.setNom(dto.getNom());
        if (dto.getPrenom() != null) existant.setPrenom(dto.getPrenom());
        if (dto.getEmail() != null) existant.setEmail(dto.getEmail());
        if (dto.getTelephone() != null) existant.setTelephone(dto.getTelephone());
        if (dto.getNumPermis() != null) existant.setNumPermis(dto.getNumPermis());
        Client saved = clientRepository.save(existant);
        return clientMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec email : " + email, (Long) null));
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findByNomContaining(String nom) {
        return clientRepository.findByNomContaining(nom).stream()
                .map(clientMapper::toDto)
                .toList();
    }
}
