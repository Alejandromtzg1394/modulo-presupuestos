package com.uacm.mapeo.presupuestos.service.impl;

import com.uacm.mapeo.presupuestos.dto.request.ClienteRequest;
import com.uacm.mapeo.presupuestos.dto.response.ClienteResponse;
import com.uacm.mapeo.presupuestos.model.entity.Cliente;
import com.uacm.mapeo.presupuestos.repository.ClienteRepository;
import com.uacm.mapeo.presupuestos.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponse crearCliente(@Valid ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setRfc(request.getRfc());
        cliente.setContactoPrincipal(request.getContactoPrincipal());
        cliente.setPuestoContacto(request.getPuestoContacto());
        cliente.setObservaciones(request.getObservaciones());

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return convertirAResponse(clienteGuardado);
    }

    @Override
    public List<ClienteResponse> obtenerTodosClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClienteResponse> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public ClienteResponse actualizarCliente(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        cliente.setNombre(request.getNombre());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setRfc(request.getRfc());
        cliente.setContactoPrincipal(request.getContactoPrincipal());
        cliente.setPuestoContacto(request.getPuestoContacto());
        cliente.setObservaciones(request.getObservaciones());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertirAResponse(clienteActualizado);
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }


    private ClienteResponse convertirAResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .idCliente(cliente.getIdCliente())
                .nombre(cliente.getNombre())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .rfc(cliente.getRfc())
                .contactoPrincipal(cliente.getContactoPrincipal())
                .puestoContacto(cliente.getPuestoContacto())
                .observaciones(cliente.getObservaciones())
                .build();
    }

}