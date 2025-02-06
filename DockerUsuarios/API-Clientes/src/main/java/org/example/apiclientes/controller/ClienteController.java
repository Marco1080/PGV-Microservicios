package org.example.apiclientes.controller;

import org.example.apiclientes.models.Cliente;
import org.example.apiclientes.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClientesRepository repositorio;

    @PostMapping("/login")
    public String crearUsuario(@RequestBody Cliente cliente) {
        // Verificar si el usuario ya existe
        if (repositorio.findById(cliente.getNombre()).isPresent()) {
            return "El usuario ya existe";
        }

        // Cifrar la contraseña antes de guardarla
        cliente.setContrasena(hashPassword(cliente.getContrasena()));
        repositorio.save(cliente);
        return "Usuario creado con éxito";
    }

    @GetMapping("/user")
    public String verificarUsuario(@RequestParam String nombre, @RequestParam String contrasena) {
        Optional<Cliente> clienteOpt = repositorio.findById(nombre);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            // Comparar la contraseña cifrada con la almacenada
            if (cliente.getContrasena().equals(hashPassword(contrasena))) {
                return "Usuario autenticado correctamente";
            } else {
                return "Credenciales incorrectas";
            }
        } else {
            return "Usuario no encontrado";
        }
    }

    // Método para cifrar la contraseña usando SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al cifrar la contraseña", e);
        }
    }
}
