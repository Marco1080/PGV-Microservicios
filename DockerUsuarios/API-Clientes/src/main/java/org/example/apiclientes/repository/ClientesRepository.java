package org.example.apiclientes.repository;

import org.example.apiclientes.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientesRepository extends JpaRepository<Cliente, Integer>{
}
