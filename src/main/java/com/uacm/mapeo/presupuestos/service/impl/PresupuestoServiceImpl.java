// src/main/java/com/uacm/mapeo/presupuestos/service/impl/PresupuestoServiceImpl.java
package com.uacm.mapeo.presupuestos.service.impl;

import com.uacm.mapeo.presupuestos.dto.request.PresupuestoRequest;
import com.uacm.mapeo.presupuestos.dto.response.PresupuestoResponse;
import com.uacm.mapeo.presupuestos.model.entity.Presupuesto;
import com.uacm.mapeo.presupuestos.model.entity.Proyecto;
import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import com.uacm.mapeo.presupuestos.repository.PresupuestoRepository;
import com.uacm.mapeo.presupuestos.repository.ProyectoRepository;
import com.uacm.mapeo.presupuestos.service.PresupuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PresupuestoServiceImpl implements PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PresupuestoResponse> obtenerTodosPresupuestos() {
        return presupuestoRepository.findAll()
                .stream()
                .map(this::convertirAResponseConDetalles)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresupuestoResponse> obtenerPresupuestosPorProyecto(Long idProyecto) {
        // Validar que el proyecto exista
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + idProyecto));

        // Obtener presupuestos del proyecto
        List<Presupuesto> presupuestos = presupuestoRepository.findByProyectoIdProyecto(idProyecto);

        return presupuestos.stream()
                .map(this::convertirAResponseConDetalles)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresupuestoResponse> obtenerPresupuestosPorCliente(Long idCliente) {
        // Obtener presupuestos por cliente (usando el método del repositorio)
        List<Presupuesto> presupuestos = presupuestoRepository.findByClienteId(idCliente);

        return presupuestos.stream()
                .map(this::convertirAResponseConDetalles)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresupuestoResponse> filtrarPresupuestos(Long idProyecto, Long idCliente, EstadoPresupuesto estado) {
        List<Presupuesto> presupuestos = presupuestoRepository.findByFiltros(idProyecto, idCliente, estado);

        return presupuestos.stream()
                .map(this::convertirAResponseConDetalles)
                .collect(Collectors.toList());
    }

    @Override
    public PresupuestoResponse crearPresupuesto(PresupuestoRequest request) {
        // 1. Validar que el proyecto exista
        Proyecto proyecto = proyectoRepository.findById(request.getIdProyecto())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.getIdProyecto()));

        // 2. Calcular nueva versión
        Integer maxVersion = presupuestoRepository.findMaxVersionByProyectoId(request.getIdProyecto());
        Integer nuevaVersion = maxVersion + 1;

        // 3. Crear entidad Presupuesto
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setProyecto(proyecto);
        presupuesto.setMontoAprobado(request.getMontoAprobado());
        presupuesto.setMoneda(request.getMoneda());
        presupuesto.setVersion(nuevaVersion);
        presupuesto.setEstado(EstadoPresupuesto.BORRADOR);
        presupuesto.setComentarios(request.getComentarios());
        presupuesto.setUsuarioCreacion(request.getUsuarioCreacion());

        // 4. Guardar
        Presupuesto presupuestoGuardado = presupuestoRepository.save(presupuesto);

        // 5. Retornar response con detalles
        return convertirAResponseConDetalles(presupuestoGuardado);
    }

    private PresupuestoResponse convertirAResponseConDetalles(Presupuesto presupuesto) {
        return PresupuestoResponse.builder()
                .idPresupuesto(presupuesto.getIdPresupuesto())
                .idProyecto(presupuesto.getProyecto().getIdProyecto())
                .nombreProyecto(presupuesto.getProyecto().getNombre())
                .estadoProyecto(presupuesto.getProyecto().getEstado().name())
                .idCliente(presupuesto.getProyecto().getCliente().getIdCliente())
                .nombreCliente(presupuesto.getProyecto().getCliente().getNombre())
                .rfcCliente(presupuesto.getProyecto().getCliente().getRfc())
                .montoAprobado(presupuesto.getMontoAprobado())
                .moneda(presupuesto.getMoneda())
                .fechaAprobacion(presupuesto.getFechaAprobacion())
                .version(presupuesto.getVersion())
                .estado(presupuesto.getEstado())
                .comentarios(presupuesto.getComentarios())
                .fechaCreacion(presupuesto.getFechaCreacion())
                .usuarioCreacion(presupuesto.getUsuarioCreacion())
                .fechaModificacion(presupuesto.getFechaModificacion())
                .usuarioModificacion(presupuesto.getUsuarioModificacion())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PresupuestoResponse> obtenerPresupuestoPorId(Long id) {
        return presupuestoRepository.findById(id)
                .map(this::convertirAResponseConDetalles);
    }

    @Override
    public PresupuestoResponse actualizarPresupuesto(Long id, PresupuestoRequest request) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con ID: " + id));

        // Validar que solo se pueda editar si está en BORRADOR
        if (!presupuesto.getEstado().equals(EstadoPresupuesto.BORRADOR)) {
            throw new RuntimeException("Solo se puede editar presupuestos en estado BORRADOR");
        }

        // Actualizar campos permitidos
        presupuesto.setMontoAprobado(request.getMontoAprobado());
        presupuesto.setMoneda(request.getMoneda());
        presupuesto.setComentarios(request.getComentarios());
        presupuesto.setUsuarioModificacion(request.getUsuarioCreacion());

        Presupuesto actualizado = presupuestoRepository.save(presupuesto);
        return convertirAResponseConDetalles(actualizado);
    }

    @Override
    public void eliminarPresupuesto(Long id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con ID: " + id));

        // Validar que solo se pueda eliminar si está en BORRADOR o RECHAZADO
        if (!presupuesto.getEstado().equals(EstadoPresupuesto.BORRADOR) &&
                !presupuesto.getEstado().equals(EstadoPresupuesto.RECHAZADO)) {
            throw new RuntimeException("Solo se pueden eliminar presupuestos en estado BORRADOR o RECHAZADO");
        }

        presupuestoRepository.delete(presupuesto);
    }



    // Alejandro

    private boolean esUsuarioAdministrador(String usuario) {

        return usuario.equalsIgnoreCase("alejandro.mtz");
    }

    @Override
    public PresupuestoResponse aprobarPresupuesto(Long id, String usuarioAprobador) {

        // 1. Buscar presupuesto
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con ID: " + id));

        // 2. Validar que el usuario sea administrador
        if (!esUsuarioAdministrador(usuarioAprobador)) {
            throw new RuntimeException("El usuario no tiene permisos para aprobar presupuestos");
        }

        // 3. Validar que el estado sea BORRADOR
        if (presupuesto.getEstado() != EstadoPresupuesto.BORRADOR) {
            throw new RuntimeException("Solo los presupuestos en estado BORRADOR pueden aprobarse");
        }

        // 4. Actualizar estado y metadatos
        presupuesto.setEstado(EstadoPresupuesto.APROBADO);
        presupuesto.setFechaAprobacion(LocalDateTime.now());
        presupuesto.setUsuarioModificacion(usuarioAprobador);
        presupuesto.setFechaModificacion(LocalDateTime.now());

        // 5. Guardar
        presupuesto = presupuestoRepository.save(presupuesto);

        // 6. Convertir a response
        return convertirAResponseConDetalles(presupuesto);
    }


    @Override
    public PresupuestoResponse rechazarPresupuesto(Long id, String usuario, String motivo) {

        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con ID: " + id));

        if (motivo == null || motivo.trim().isEmpty()) {
            throw new RuntimeException("Debe proporcionar un motivo para rechazar el presupuesto");
        }

        if (presupuesto.getEstado() != EstadoPresupuesto.BORRADOR &&
                presupuesto.getEstado() != EstadoPresupuesto.APROBADO) {
            throw new RuntimeException("Solo los presupuestos en estado BORRADOR o APROBADO pueden rechazarse");
        }

        presupuesto.setEstado(EstadoPresupuesto.RECHAZADO);

        // Guardamos el motivo en 'comentarios' (sin necesidad de cambiar la entidad)
        String comentarioActual = presupuesto.getComentarios() == null ? "" : presupuesto.getComentarios() + "\n";
        comentarioActual += "Motivo de rechazo (por " + usuario + "): " + motivo;
        presupuesto.setComentarios(comentarioActual);

        // Actualizamos usuario y fecha de modificación
        presupuesto.setUsuarioModificacion(usuario);
        presupuesto.setFechaModificacion(LocalDateTime.now());

        presupuesto = presupuestoRepository.save(presupuesto);

        return convertirAResponseConDetalles(presupuesto);
    }

    @Override
    public PresupuestoResponse cerrarPresupuesto(Long id, String usuario) {

        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con ID: " + id));

        if (presupuesto.getEstado() != EstadoPresupuesto.APROBADO) {
            throw new RuntimeException("Solo los presupuestos en estado APROBADO pueden cerrarse");
        }

        presupuesto.setEstado(EstadoPresupuesto.CERRADO);

        // Opcional: añadir un comentario indicando quién cerró el presupuesto
        String comentarioActual = presupuesto.getComentarios() == null ? "" : presupuesto.getComentarios() + "\n";
        comentarioActual += "Cerrado por: " + usuario + " en " + LocalDateTime.now();
        presupuesto.setComentarios(comentarioActual);

        // Actualizamos usuario y fecha de modificación
        presupuesto.setUsuarioModificacion(usuario);
        presupuesto.setFechaModificacion(LocalDateTime.now());

        presupuesto = presupuestoRepository.save(presupuesto);

        return convertirAResponseConDetalles(presupuesto);
    }


}