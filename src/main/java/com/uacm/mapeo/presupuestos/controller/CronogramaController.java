// src/main/java/com/uacm/mapeo/presupuestos/controller/CronogramaController.java
package com.uacm.mapeo.presupuestos.controller;

import com.uacm.mapeo.presupuestos.dto.request.EtapaRequest;
import com.uacm.mapeo.presupuestos.dto.response.CronogramaResponse;
import com.uacm.mapeo.presupuestos.dto.response.EtapaResponse;
import com.uacm.mapeo.presupuestos.dto.response.ResumenPresupuestoDTO;
import com.uacm.mapeo.presupuestos.service.CronogramaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/proyectos")
@RequiredArgsConstructor
public class CronogramaController {

    private final CronogramaService cronogramaService;

    // GET /cronogramas - OBTENER TODOS LOS CRONOGRAMAS
    // http://localhost:8080/cronogramas
    @GetMapping("/cronogramas")
    public ResponseEntity<List<CronogramaResponse>> obtenerTodosLosCronogramas() {
        List<CronogramaResponse> cronogramas = cronogramaService.obtenerTodosLosCronogramas();
        return ResponseEntity.ok(cronogramas);
    }

    // GET /proyectos/{id}/resumen-presupuesto OK
    //http://localhost:8080/proyectos/3/resumen-presupuesto
    @GetMapping("/{idProyecto}/resumen-presupuesto")
    public ResponseEntity<ResumenPresupuestoDTO> obtenerResumenPresupuesto(
            @PathVariable Integer idProyecto) {
        ResumenPresupuestoDTO resumen = cronogramaService.obtenerResumenPresupuesto(idProyecto);
        return ResponseEntity.ok(resumen);
    }

    // POST /proyectos/etapas OK
    @PostMapping("/etapas")
    public ResponseEntity<EtapaResponse> crearEtapa(
            @Valid @RequestBody EtapaRequest request) {
        EtapaResponse response = cronogramaService.crearEtapa(request);
        return ResponseEntity.ok(response);
    }

    // PUT /etapas/{id}/costo-real
    @PutMapping("/etapas/{idEtapa}/costo-real")
    public ResponseEntity<EtapaResponse> actualizarCostoReal(
            @PathVariable Integer idEtapa,
            @RequestParam Double costoReal) {  // Cambiado a Double TEMA DE INTEGER Y LOG
        EtapaResponse response = cronogramaService.actualizarCostoReal(idEtapa, costoReal);
        return ResponseEntity.ok(response);
    }

    // GET /cronogramas/{id}/etapas
    @GetMapping("/cronogramas/{idCronograma}/etapas")
    public ResponseEntity<List<EtapaResponse>> obtenerEtapasPorCronograma(
            @PathVariable Integer idCronograma) {
        List<EtapaResponse> etapas = cronogramaService.obtenerEtapasPorCronograma(idCronograma);
        return ResponseEntity.ok(etapas);
    }
}