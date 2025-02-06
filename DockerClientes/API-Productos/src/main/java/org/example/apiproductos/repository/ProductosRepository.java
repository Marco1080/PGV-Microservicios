package org.example.apiproductos.repository;

import org.example.apiproductos.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductosRepository extends JpaRepository<Producto, Integer> {

}