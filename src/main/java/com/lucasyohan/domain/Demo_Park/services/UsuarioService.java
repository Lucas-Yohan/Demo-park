package com.lucasyohan.domain.Demo_Park.services;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import com.lucasyohan.domain.Demo_Park.exceptions.EntityNotFoundException;
import com.lucasyohan.domain.Demo_Park.exceptions.UsernameUniqueViolationException;
import com.lucasyohan.domain.Demo_Park.repositories.UsuariosRepository;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuariosRepository usuarioRepository;

    @Transactional
    public Usuarios salvar(Usuarios usuario){
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Usuário de Id %s já cadastrado", usuario.getId()));
        }
    }

    @Transactional(readOnly = true)
    public Usuarios buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Usuário de Id %s não encontrado", id)));
    }

    @Transactional()
    public Usuarios updatePassword(String newPassword, String actualPassword, String confPassword, Long id){
        Usuarios user = buscarPorId(id);

        if (!user.getPassword().equals(actualPassword)) {
            throw new RuntimeException("Senha atual incorreta");
        }

        if (!newPassword.equals(confPassword)) {
            throw new RuntimeException("As senhas não coincidem");
        }

        user.setPassword(newPassword);
        return usuarioRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Usuarios> buscarTodos() {
        return usuarioRepository.findAll();
    }
}
