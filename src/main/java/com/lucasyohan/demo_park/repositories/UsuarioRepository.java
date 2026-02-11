package com.lucasyohan.demo_park.repositories;

import com.lucasyohan.demo_park.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}

