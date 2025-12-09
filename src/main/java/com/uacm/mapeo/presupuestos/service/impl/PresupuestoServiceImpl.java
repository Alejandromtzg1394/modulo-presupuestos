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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
//
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PresupuestoServiceImpl implements PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    @Transactional
    public PresupuestoResponse crearPresupuesto(PresupuestoRequest request) {
        // Validar que el proyecto exista
        Proyecto proyecto = proyectoRepository.findById(request.getIdProyecto())
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado"));

        // Validar que no haya otro presupuesto aprobado para el mismo proyecto
        presupuestoRepository.findByProyectoIdProyectoAndEstado(proyecto.getIdProyecto(), EstadoPresupuesto.APROBADO)
                .ifPresent(p -> {
                    throw new IllegalStateException("Ya existe un presupuesto aprobado para este proyecto");
                });

        // Calcular nueva versión
        Integer maxVersion = presupuestoRepository.findMaxVersionByProyectoId(proyecto.getIdProyecto());
        int nuevaVersion = (maxVersion != null) ? maxVersion + 1 : 1;

        // Crear presupuesto
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setProyecto(proyecto);
        presupuesto.setMontoAprobado(request.getMontoAprobado());
        presupuesto.setMoneda(request.getMoneda());
        presupuesto.setVersion(nuevaVersion);
        presupuesto.setEstado(EstadoPresupuesto.BORRADOR);
        presupuesto.setComentarios(request.getComentarios());
        presupuesto.setUsuarioCreacion(request.getUsuarioCreacion());
        presupuesto.setFechaCreacion(LocalDate.now());

        Presupuesto saved = presupuestoRepository.save(presupuesto);
        return mapToResponse(saved);
    }

    @Override
    public PresupuestoResponse obtenerPresupuesto(Long id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));
        return mapToResponse(presupuesto);
    }

    @Override
    public List<PresupuestoResponse> listarPresupuestos() {
        return presupuestoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PresupuestoResponse> listarPorProyecto(Long idProyecto) {
        return presupuestoRepository.findByProyectoIdProyecto(idProyecto)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PresupuestoResponse> listarPorCliente(Long idCliente) {
        return presupuestoRepository.findByClienteId(idCliente)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PresupuestoResponse actualizarPresupuesto(Long id, PresupuestoRequest request) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));

        // Validar que solo se pueda actualizar en estados BORRADOR o EN_REVISION
        if (presupuesto.getEstado() == EstadoPresupuesto.APROBADO ||
                presupuesto.getEstado() == EstadoPresupuesto.CERRADO) {
            throw new IllegalStateException("No se puede modificar un presupuesto " + presupuesto.getEstado());
        }

        // Validar que el proyecto existe si se cambia
        if (!presupuesto.getProyecto().getIdProyecto().equals(request.getIdProyecto())) {
            Proyecto nuevoProyecto = proyectoRepository.findById(request.getIdProyecto())
                    .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado"));
            presupuesto.setProyecto(nuevoProyecto);
        }

        // Actualizar campos
        presupuesto.setMontoAprobado(request.getMontoAprobado());
        presupuesto.setMoneda(request.getMoneda());
        presupuesto.setComentarios(request.getComentarios());
        presupuesto.setFechaModificacion(LocalDate.now());
        presupuesto.setUsuarioModificacion(request.getUsuarioCreacion());

        // Si el estado es BORRADOR, cambiar a EN_REVISION si se especifica
        if (presupuesto.getEstado() == EstadoPresupuesto.BORRADOR &&
                request instanceof PresupuestoRequest) {
            // Aquí podrías agregar lógica para cambiar estado
        }

        Presupuesto updated = presupuestoRepository.save(presupuesto);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void eliminarPresupuesto(Long id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));

        // Validar que solo se pueda eliminar en estados BORRADOR o RECHAZADO
        if (presupuesto.getEstado() != EstadoPresupuesto.BORRADOR &&
                presupuesto.getEstado() != EstadoPresupuesto.RECHAZADO) {
            throw new IllegalStateException("Solo se pueden eliminar presupuestos en estado BORRADOR o RECHAZADO");
        }

        // Verificar si es la última versión para no dejar huecos en la numeración
        List<Presupuesto> presupuestosProyecto = presupuestoRepository
                .findByProyectoIdProyecto(presupuesto.getProyecto().getIdProyecto());

        if (presupuestosProyecto.size() > 1) {
            // Reorganizar versiones si es necesario
            presupuestosProyecto.remove(presupuesto);
            for (int i = 0; i < presupuestosProyecto.size(); i++) {
                presupuestosProyecto.get(i).setVersion(i + 1);
            }
            presupuestoRepository.saveAll(presupuestosProyecto);
        }

        presupuestoRepository.delete(presupuesto);
    }

    @Override
    @Transactional
    public PresupuestoResponse aprobarPresupuesto(Long id, String usuarioAprobador) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));

        // Validar estado
        if (presupuesto.getEstado() != EstadoPresupuesto.EN_REVISION) {
            throw new IllegalStateException("Solo los presupuestos en revisión pueden ser aprobados");
        }

        // Cerrar presupuestos anteriores aprobados
        List<Presupuesto> presupuestosAnteriores = presupuestoRepository
                .findByProyectoIdProyecto(presupuesto.getProyecto().getIdProyecto())
                .stream()
                .filter(p -> p.getEstado() == EstadoPresupuesto.APROBADO &&
                        !p.getIdPresupuesto().equals(id))
                .collect(Collectors.toList());

        presupuestosAnteriores.forEach(p -> {
            p.setEstado(EstadoPresupuesto.CERRADO);
            p.setFechaModificacion(LocalDate.now());
            p.setUsuarioModificacion(usuarioAprobador);
        });

        if (!presupuestosAnteriores.isEmpty()) {
            presupuestoRepository.saveAll(presupuestosAnteriores);
        }

        // Actualizar presupuesto actual
        presupuesto.setEstado(EstadoPresupuesto.APROBADO);
        presupuesto.setFechaAprobacion(LocalDate.now());
        presupuesto.setFechaModificacion(LocalDate.now());
        presupuesto.setUsuarioModificacion(usuarioAprobador);

        Presupuesto updated = presupuestoRepository.save(presupuesto);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public PresupuestoResponse rechazarPresupuesto(Long id, String motivo) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));

        // Validar estado
        if (presupuesto.getEstado() != EstadoPresupuesto.EN_REVISION) {
            throw new IllegalStateException("Solo los presupuestos en revisión pueden ser rechazados");
        }

        // Actualizar presupuesto
        presupuesto.setEstado(EstadoPresupuesto.RECHAZADO);
        presupuesto.setComentarios((presupuesto.getComentarios() != null ?
                presupuesto.getComentarios() + "\n\n" : "") +
                "RECHAZADO - Motivo: " + motivo);
        presupuesto.setFechaModificacion(LocalDate.now());
        presupuesto.setUsuarioModificacion("Sistema");

        Presupuesto updated = presupuestoRepository.save(presupuesto);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public PresupuestoResponse cerrarPresupuesto(Long id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));

        // Validar estado - solo se pueden cerrar presupuestos APROBADOS
        if (presupuesto.getEstado() != EstadoPresupuesto.APROBADO) {
            throw new IllegalStateException("Solo los presupuestos aprobados pueden ser cerrados");
        }

        // Verificar si el proyecto está completado o cancelado
        Proyecto proyecto = presupuesto.getProyecto();
        if (proyecto.getEstado() != null &&
                !proyecto.getEstado().toString().equals("COMPLETADO") &&
                !proyecto.getEstado().toString().equals("CANCELADO")) {
            throw new IllegalStateException("El proyecto debe estar COMPLETADO o CANCELADO para cerrar el presupuesto");
        }

        // Cerrar presupuesto
        presupuesto.setEstado(EstadoPresupuesto.CERRADO);
        presupuesto.setFechaModificacion(LocalDate.now());
        presupuesto.setUsuarioModificacion("Sistema");

        Presupuesto updated = presupuestoRepository.save(presupuesto);
        return mapToResponse(updated);
    }

    // Método auxiliar para cambiar estado a EN_REVISION
    @Transactional
    public PresupuestoResponse enviarARevision(Long id, String usuario) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));

        // Validar estado - solo se puede enviar a revisión desde BORRADOR
        if (presupuesto.getEstado() != EstadoPresupuesto.BORRADOR) {
            throw new IllegalStateException("Solo los presupuestos en borrador pueden enviarse a revisión");
        }

        // Cambiar estado
        presupuesto.setEstado(EstadoPresupuesto.EN_REVISION);
        presupuesto.setFechaModificacion(LocalDate.now());
        presupuesto.setUsuarioModificacion(usuario);

        Presupuesto updated = presupuestoRepository.save(presupuesto);
        return mapToResponse(updated);
    }

    // Método auxiliar para validar sumas de costos
    public boolean validarSumaCostosEstimados(Long idPresupuesto) {
        Presupuesto presupuesto = presupuestoRepository.findById(idPresupuesto)
                .orElseThrow(() -> new EntityNotFoundException("Presupuesto no encontrado"));

        // Obtener suma de costos estimados de todas las etapas del proyecto
        Double sumaCostosEstimados = presupuesto.getProyecto().getCronogramas().stream()
                .flatMap(c -> c.getEtapas().stream())
                .mapToDouble(e -> e.getCostoEstimado() != null ? e.getCostoEstimado() : 0.0)
                .sum();

        return sumaCostosEstimados <= presupuesto.getMontoAprobado();
    }

    // Método auxiliar para obtener resumen presupuestal
    public Map<String, Object> obtenerResumenPresupuestal(Long idProyecto) {
        List<Presupuesto> presupuestos = presupuestoRepository.findByProyectoIdProyecto(idProyecto);

        Optional<Presupuesto> presupuestoAprobado = presupuestos.stream()
                .filter(p -> p.getEstado() == EstadoPresupuesto.APROBADO)
                .findFirst();

        // Calcular suma de costos estimados y reales
        Double sumaCostosEstimados = 0.0;
        Double sumaCostosReales = 0.0;

        // Aquí deberías implementar la lógica para obtener costos de etapas
        // Esto es un ejemplo básico

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalPresupuestos", presupuestos.size());
        presupuestoAprobado.ifPresent(p -> {
            resumen.put("presupuestoAprobado", p.getMontoAprobado());
            resumen.put("moneda", p.getMoneda());
        });
        resumen.put("sumaCostosEstimados", sumaCostosEstimados);
        resumen.put("sumaCostosReales", sumaCostosReales);

        if (presupuestoAprobado.isPresent()) {
            Double montoAprobado = presupuestoAprobado.get().getMontoAprobado();
            resumen.put("diferenciaEstimados", montoAprobado - sumaCostosEstimados);
            resumen.put("diferenciaReales", montoAprobado - sumaCostosReales);
            resumen.put("porcentajeEjecutado", sumaCostosReales > 0 ?
                    (sumaCostosReales / montoAprobado) * 100 : 0);
        }

        return resumen;
    }

    private PresupuestoResponse mapToResponse(Presupuesto presupuesto) {
        PresupuestoResponse response = new PresupuestoResponse();
        response.setIdPresupuesto(presupuesto.getIdPresupuesto());
        response.setIdProyecto(presupuesto.getProyecto().getIdProyecto());
        response.setNombreProyecto(presupuesto.getProyecto().getNombre());
        response.setIdCliente(presupuesto.getProyecto().getCliente().getIdCliente());
        response.setNombreCliente(presupuesto.getProyecto().getCliente().getNombre());
        response.setMontoAprobado(presupuesto.getMontoAprobado());
        response.setMoneda(presupuesto.getMoneda());
        response.setFechaAprobacion(presupuesto.getFechaAprobacion());
        response.setVersion(presupuesto.getVersion());
        response.setEstado(presupuesto.getEstado());
        response.setComentarios(presupuesto.getComentarios());
        response.setFechaCreacion(presupuesto.getFechaCreacion());
        response.setUsuarioCreacion(presupuesto.getUsuarioCreacion());
        return response;
    }
}