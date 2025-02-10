package org.example.apiclientes.controller;

import org.example.apiclientes.models.Cliente;
import org.example.apiclientes.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClientesRepository repositorio;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registro (Manteniendo la función original en /api/login)
    @PostMapping("/login")
    public String registrarUsuario(@RequestBody Cliente cliente) {
        if (repositorio.findById(cliente.getNombre()).isPresent()) {
            return "400"; // Usuario ya existe
        }
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        repositorio.save(cliente);
        return "200"; // Registro exitoso
    }

    // Login (Adaptado para manejar respuestas de Retrofit correctamente)
    @GetMapping("/user")
    public String verificarUsuario(@RequestParam String nombre, @RequestParam String contrasena) {
        Optional<Cliente> clienteOpt = repositorio.findById(nombre);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (passwordEncoder.matches(contrasena, cliente.getContrasena())) {
                return "200";
            } else {
                return "401";
            }
        } else {
            return "404";
        }
    }
}