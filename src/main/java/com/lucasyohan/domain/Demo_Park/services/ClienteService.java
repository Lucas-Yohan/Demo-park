package com.lucasyohan.domain.Demo_Park.services;

import com.lucasyohan.domain.Demo_Park.entities.Cliente;
import com.lucasyohan.domain.Demo_Park.exceptions.CpfUniqueViolationException;
import com.lucasyohan.domain.Demo_Park.exceptions.EntityNotFoundException;
import com.lucasyohan.domain.Demo_Park.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente save(Cliente cliente) {
        try{
            return clienteRepository.save(cliente);
        }catch (DataIntegrityViolationException e){
            throw new CpfUniqueViolationException(String.format("O CPF '%s' já está cadastrado", cliente.getCpf()));
        }
    }


    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(String.format("Cliente de id '%s' não encontrado", id)));
    }
}
