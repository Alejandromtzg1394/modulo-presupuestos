// src/main/java/com/uacm/mapeo/presupuestos/service/PresupuestoService.java
package com.uacm.mapeo.presupuestos.service;

import com.uacm.mapeo.presupuestos.dto.request.PresupuestoRequest;
import com.uacm.mapeo.presupuestos.dto.response.PresupuestoResponse;
import java.util.List;
import java.util.Map;

public interface PresupuestoService {
    PresupuestoResponse crearPresupuesto(PresupuestoRequest request);
    PresupuestoResponse obtenerPresupuesto(Long id);
    List<PresupuestoResponse> listarPresupuestos();
    List<PresupuestoResponse> listarPorProyecto(Long idProyecto);
    List<PresupuestoResponse> listarPorCliente(Long idCliente);
    PresupuestoResponse actualizarPresupuesto(Long id, PresupuestoRequest request);
    void eliminarPresupuesto(Long id);
    PresupuestoResponse aprobarPresupuesto(Long id, String usuarioAprobador);
    PresupuestoResponse rechazarPresupuesto(Long id, String motivo);
    PresupuestoResponse cerrarPresupuesto(Long id);

}