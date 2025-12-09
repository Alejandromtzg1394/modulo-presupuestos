// src/main/java/com/uacm/mapeo/presupuestos/controller/PresupuestoController.java
package com.uacm.mapeo.presupuestos.controller;

import com.uacm.mapeo.presupuestos.dto.request.PresupuestoRequest;
import com.uacm.mapeo.presupuestos.dto.response.PresupuestoResponse;
import com.uacm.mapeo.presupuestos.service.PresupuestoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/presupuestos")
@RequiredArgsConstructor
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    @PostMapping // Ok revisar
    public ResponseEntity<PresupuestoResponse> crearPresupuesto(
            @Valid @RequestBody PresupuestoRequest request) {
        PresupuestoResponse response = presupuestoService.crearPresupuesto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping //OK LISTAR
    public ResponseEntity<List<PresupuestoResponse>> listarPresupuestos() {
        List<PresupuestoResponse> presupuestos = presupuestoService.listarPresupuestos();
        return ResponseEntity.ok(presupuestos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoResponse> obtenerPresupuesto(@PathVariable Long id) {
        PresupuestoResponse response = presupuestoService.obtenerPresupuesto(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<PresupuestoResponse>> listarPorProyecto(
            @PathVariable Long proyectoId) {
        List<PresupuestoResponse> presupuestos = presupuestoService.listarPorProyecto(proyectoId);
        return ResponseEntity.ok(presupuestos);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PresupuestoResponse>> listarPorCliente(
            @PathVariable Long clienteId) {
        List<PresupuestoResponse> presupuestos = presupuestoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(presupuestos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PresupuestoResponse> actualizarPresupuesto(
            @PathVariable Long id,
            @Valid @RequestBody PresupuestoRequest request) {
        PresupuestoResponse response = presupuestoService.actualizarPresupuesto(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPresupuesto(@PathVariable Long id) {
        presupuestoService.eliminarPresupuesto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<PresupuestoResponse> aprobarPresupuesto(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String usuario = request.get("usuario");
        PresupuestoResponse response = presupuestoService.aprobarPresupuesto(id, usuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<PresupuestoResponse> rechazarPresupuesto(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String motivo = request.get("motivo");
        String usuario = request.get("usuario");
        PresupuestoResponse response = presupuestoService.rechazarPresupuesto(id, motivo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<PresupuestoResponse> cerrarPresupuesto(@PathVariable Long id) {
        PresupuestoResponse response = presupuestoService.cerrarPresupuesto(id);
        return ResponseEntity.ok(response);
    }
}