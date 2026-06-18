package com.lucasyohan.domain.Demo_Park.services;

import com.lucasyohan.domain.Demo_Park.entities.Usuario;
import com.lucasyohan.domain.Demo_Park.exceptions.EntityNotFoundException;
import com.lucasyohan.domain.Demo_Park.exceptions.PasswordNotValidException;
import com.lucasyohan.domain.Demo_Park.exceptions.UsernameUniqueViolationException;
import com.lucasyohan.domain.Demo_Park.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder  passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario){
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' já cadastrado", usuario.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Usuário de Id %s não encontrado", id)));
    }

    @Transactional()
    public Usuario updatePassword(String newPassword, String actualPassword, String confPassword, Long id){
        if (!newPassword.equals(confPassword)) {
            throw new PasswordNotValidException("A nova senha e a confirmação de senha não coincidem");
        }
        Usuario user = buscarPorId(id);

        if (!passwordEncoder.matches(actualPassword, user.getPassword())) {
            throw new PasswordNotValidException("A senha atual está incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return usuarioRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário de username %s não encontrado", username))
        );
    }

    @Transactional(readOnly = true)
    public Usuario.Role buscarRolePorUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}
