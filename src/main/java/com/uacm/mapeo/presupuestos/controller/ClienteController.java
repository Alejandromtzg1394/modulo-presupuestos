package com.uacm.mapeo.presupuestos.controller;

import com.uacm.mapeo.presupuestos.dto.request.ClienteRequest;
import com.uacm.mapeo.presupuestos.dto.response.ClienteResponse;
import com.uacm.mapeo.presupuestos.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // GET: OK
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> obtenerTodosClientes() {
        List<ClienteResponse> clientes = clienteService.obtenerTodosClientes();
        return ResponseEntity.ok(clientes);
    }

    // GET: OK
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: OK
    @PostMapping
    public ResponseEntity<ClienteResponse> crearCliente(
            @Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteService.crearCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT: OK
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteService.actualizarCliente(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE: OK
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}