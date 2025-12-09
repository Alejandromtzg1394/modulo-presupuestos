package com.uacm.mapeo.presupuestos.service;

import com.uacm.mapeo.presupuestos.dto.request.ClienteRequest;
import com.uacm.mapeo.presupuestos.dto.response.ClienteResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    ClienteResponse crearCliente(@Valid ClienteRequest request);
    List<ClienteResponse> obtenerTodosClientes();
    Optional<ClienteResponse> obtenerClientePorId(Long id);
    ClienteResponse actualizarCliente(Long id, ClienteRequest request);
    void eliminarCliente(Long id);
}