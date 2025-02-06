package org.example.apiproductos.repository;

import org.example.apiproductos.models.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
}

