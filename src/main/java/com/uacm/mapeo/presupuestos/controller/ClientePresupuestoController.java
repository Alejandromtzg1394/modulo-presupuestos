// src/main/java/com/uacm/mapeo/presupuestos/controller/ClientePresupuestoController.java
package com.uacm.mapeo.presupuestos.controller;

import com.uacm.mapeo.presupuestos.dto.response.PresupuestoResponse;
import com.uacm.mapeo.presupuestos.service.PresupuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClientePresupuestoController {

    private final PresupuestoService presupuestoService;

    // GET /clientes/{idCliente}/presupuestos
    @GetMapping("/{idCliente}/presupuestos")
    public ResponseEntity<List<PresupuestoResponse>> obtenerPresupuestosPorCliente(
            @PathVariable Long idCliente) {
        List<PresupuestoResponse> presupuestos = presupuestoService.obtenerPresupuestosPorCliente(idCliente);
        return ResponseEntity.ok(presupuestos);
    }
}