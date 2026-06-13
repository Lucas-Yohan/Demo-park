package com.lucasyohan.domain.Demo_Park.services;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import com.lucasyohan.domain.Demo_Park.exceptions.EntityNotFoundException;
import com.lucasyohan.domain.Demo_Park.exceptions.MethodArgumentNotValidException;
import com.lucasyohan.domain.Demo_Park.exceptions.UsernameUniqueViolationException;
import com.lucasyohan.domain.Demo_Park.repositories.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuariosRepository usuarioRepository;
    private final PasswordEncoder  passwordEncoder;

    @Transactional
    public Usuarios salvar(Usuarios usuario){
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' já cadastrado", usuario.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public Usuarios buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Usuário de Id %s não encontrado", id)));
    }

    @Transactional()
    public Usuarios updatePassword(String newPassword, String actualPassword, String confPassword, Long id){
        if (!newPassword.equals(confPassword)) {
            throw new MethodArgumentNotValidException("A nova senha e a confirmação de senha não coincidem");
        }
        Usuarios user = buscarPorId(id);

        if (!passwordEncoder.matches(actualPassword, user.getPassword())) {
            throw new MethodArgumentNotValidException("A senha atual está incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return usuarioRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Usuarios> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuarios buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário de username %s não encontrado", username))
        );
    }

    @Transactional(readOnly = true)
    public Usuarios.Role buscarRolePorUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}
