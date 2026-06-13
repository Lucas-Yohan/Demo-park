package com.lucasyohan.domain.Demo_Park.repositories;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {


    Optional<Usuarios> findByUsername(String username);

    @Query("SELECT u.role FROM Usuarios u WHERE u.username like :username")
    Usuarios.Role findRoleByUsername(String username);
}
