package org.example.apiclientes.controller;

import java.util.Optional;

import org.example.apiclientes.models.Cliente;
import org.example.apiclientes.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClientesRepository repositorio;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public String registrarUsuario(@RequestBody Cliente cliente) {
        if (repositorio.findById(cliente.getNombre()).isPresent()) {
            return "400";
        }
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        repositorio.save(cliente);
        return "200";
    }

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