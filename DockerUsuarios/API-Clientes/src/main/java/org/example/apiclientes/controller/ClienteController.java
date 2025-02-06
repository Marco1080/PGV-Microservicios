package org.example.apiclientes.controller;

import org.example.apiclientes.models.Cliente;
import org.example.apiclientes.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClienteController {
    @Autowired
    private ClientesRepository repositorio;

    @GetMapping("/clientes")
    public List<Cliente> obtenerClientes() {
        return repositorio.findAll();
    }

    @PostMapping("/cliente")
    public Cliente agregarCliente(@RequestBody Cliente cliente) {
        return repositorio.save(cliente);
    }

    @DeleteMapping("/cliente/{id}")
    public String eliminarCliente(@PathVariable String id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return "Cliente eliminado con éxito.";
        } else {
            return "Cliente no encontrado.";
        }
    }

    @PutMapping("/cliente/{id}")
    public String actualizarCliente(@PathVariable String id, @RequestBody Cliente clienteActualizado) {
        Optional<Cliente> clienteOptional = repositorio.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setContrasena(clienteActualizado.getContrasena());
            repositorio.save(cliente);
            return "Cliente actualizado correctamente.";
        } else {
            return "Cliente no encontrado.";
        }
    }

    @PostMapping("/cliente/verificar/{id}")
    public String verificarContrasena(@PathVariable String cli, @RequestBody String contrasena) {
        Optional<Cliente> clienteOptional = repositorio.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            if (cliente.getContrasena().equals(contrasena)) {
                return "200";
            } else {
                return "400";
            }
        } else {
            return "400";
        }
    }
}
