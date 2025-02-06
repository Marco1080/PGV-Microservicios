package org.example.apiclientes.models;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes", schema = "usuarios")
public class Cliente {
    @Id
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "contrasena", nullable = false, length = 50)
    private String contrasena;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

}