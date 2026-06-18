package com.lucasyohan.domain.Demo_Park.repositories;

import com.lucasyohan.domain.Demo_Park.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByUsername(String username);

    @Query("SELECT u.role FROM Usuario u WHERE u.username like :username")
    Usuario.Role findRoleByUsername(String username);
}
