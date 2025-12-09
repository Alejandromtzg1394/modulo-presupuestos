// src/main/java/com/uacm/mapeo/presupuestos/controller/PresupuestoController.java
package com.uacm.mapeo.presupuestos.controller;

import com.uacm.mapeo.presupuestos.dto.request.PresupuestoRequest;
import com.uacm.mapeo.presupuestos.dto.response.PresupuestoResponse;
import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import com.uacm.mapeo.presupuestos.service.PresupuestoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/presupuestos")
@RequiredArgsConstructor
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    // GET /presupuestos → Lista con filtros
    @GetMapping
    public ResponseEntity<List<PresupuestoResponse>> obtenerPresupuestos(
            @RequestParam(required = false) Long proyecto,
            @RequestParam(required = false) Long cliente,
            @RequestParam(required = false) EstadoPresupuesto estado) {

        List<PresupuestoResponse> presupuestos = presupuestoService.filtrarPresupuestos(proyecto, cliente, estado);
        return ResponseEntity.ok(presupuestos);
    }

    // GET /presupuestos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoResponse> obtenerPresupuestoPorId(@PathVariable Long id) {
        return presupuestoService.obtenerPresupuestoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /presupuestos → Crear presupuesto
    @PostMapping
    public ResponseEntity<PresupuestoResponse> crearPresupuesto(
            @Valid @RequestBody PresupuestoRequest request) {
        PresupuestoResponse response = presupuestoService.crearPresupuesto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /presupuestos/{id} → Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoResponse> actualizarPresupuesto(
            @PathVariable Long id,
            @Valid @RequestBody PresupuestoRequest request) {
        PresupuestoResponse response = presupuestoService.actualizarPresupuesto(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /presupuestos/{id} → Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPresupuesto(@PathVariable Long id) {
        presupuestoService.eliminarPresupuesto(id);
        return ResponseEntity.noContent().build();
    }
}