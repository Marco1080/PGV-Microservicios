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

    @PostMapping("/login")
    public String crearUsuario(@RequestBody Cliente cliente) {
        if (repositorio.findById(cliente.getNombre()).isPresent()) {
            return "400";
        }

        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        repositorio.save(cliente);
        return "200";
    }

    @GetMapping("/user")
    public String verificarUsuario(@RequestParam Cliente cliente) {
        String nombre = cliente.getNombre();
        String contrasena = cliente.getContrasena();
        Optional<Cliente> clienteOpt = repositorio.findById(nombre);
        if (clienteOpt.isPresent()) {
            cliente = clienteOpt.get();
            if (passwordEncoder.matches(contrasena, cliente.getContrasena())) {
                return "Usuario autenticado correctamente";
            } else {
                return "Credenciales incorrectas";
            }
        } else {
            return "Usuario no encontrado";
        }
    }
}
