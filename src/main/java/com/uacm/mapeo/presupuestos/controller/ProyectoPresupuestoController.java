// src/main/java/com/uacm/mapeo/presupuestos/controller/ProyectoPresupuestoController.java
package com.uacm.mapeo.presupuestos.controller;

import com.uacm.mapeo.presupuestos.dto.response.PresupuestoResponse;
import com.uacm.mapeo.presupuestos.service.PresupuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/proyectos")
@RequiredArgsConstructor
public class ProyectoPresupuestoController {

    private final PresupuestoService presupuestoService;

    // GET /proyectos/{idProyecto}/presupuestos
    @GetMapping("/{idProyecto}/presupuestos")
    public ResponseEntity<List<PresupuestoResponse>> obtenerPresupuestosPorProyecto(
            @PathVariable Long idProyecto) {
        List<PresupuestoResponse> presupuestos = presupuestoService.obtenerPresupuestosPorProyecto(idProyecto);
        return ResponseEntity.ok(presupuestos);
    }
}

