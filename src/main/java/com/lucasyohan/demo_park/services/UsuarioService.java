package com.lucasyohan.demo_park.services;


import com.lucasyohan.demo_park.entity.Usuario;
import com.lucasyohan.demo_park.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario create) {
        return usuarioRepository.save(create);
    }

    @Transactional(readOnly = true)
    public Usuario getById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado"));
    }

    @Transactional
    public Usuario editarSenha(Long id, String password) {
        Usuario usuario = getById(id);
        usuario.setPassword(password);
        return usuario;
    }
}
