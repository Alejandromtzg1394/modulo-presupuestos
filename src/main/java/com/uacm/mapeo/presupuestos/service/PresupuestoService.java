// src/main/java/com/uacm/mapeo/presupuestos/service/PresupuestoService.java
package com.uacm.mapeo.presupuestos.service;

import com.uacm.mapeo.presupuestos.dto.request.PresupuestoRequest;
import com.uacm.mapeo.presupuestos.dto.response.PresupuestoResponse;
import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import java.util.List;
import java.util.Optional;

public interface PresupuestoService {
    // CRUD básico
    PresupuestoResponse crearPresupuesto(PresupuestoRequest request);
    List<PresupuestoResponse> obtenerTodosPresupuestos();
    Optional<PresupuestoResponse> obtenerPresupuestoPorId(Long id);
    PresupuestoResponse actualizarPresupuesto(Long id, PresupuestoRequest request);
    void eliminarPresupuesto(Long id);

    // Consultas vinculadas (VINCULACIÓN CON PROYECTOS Y CLIENTES)
    List<PresupuestoResponse> obtenerPresupuestosPorProyecto(Long idProyecto);
    List<PresupuestoResponse> obtenerPresupuestosPorCliente(Long idCliente);
    List<PresupuestoResponse> filtrarPresupuestos(Long idProyecto, Long idCliente, EstadoPresupuesto estado);

    // Acciones de workflow (para después)
    PresupuestoResponse aprobarPresupuesto(Long id, String usuarioAprobador);
    PresupuestoResponse rechazarPresupuesto(Long id, String usuario, String motivo);
    PresupuestoResponse cerrarPresupuesto(Long id, String usuario);
}