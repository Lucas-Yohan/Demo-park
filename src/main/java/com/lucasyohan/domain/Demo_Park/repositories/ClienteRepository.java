package com.lucasyohan.domain.Demo_Park.repositories;

import com.lucasyohan.domain.Demo_Park.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
