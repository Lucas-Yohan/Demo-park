package com.lucasyohan.domain.Demo_Park.services;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import com.lucasyohan.domain.Demo_Park.repositories.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuariosRepository usuarioRepository;

    @Transactional
    public Usuarios salvar(Usuarios usuario){
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuarios buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
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

}
