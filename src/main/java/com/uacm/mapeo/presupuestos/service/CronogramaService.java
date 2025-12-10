package com.uacm.mapeo.presupuestos.service;

import com.uacm.mapeo.presupuestos.dto.request.EtapaRequest;
import com.uacm.mapeo.presupuestos.dto.response.CronogramaResponse;
import com.uacm.mapeo.presupuestos.dto.response.EtapaResponse;
import com.uacm.mapeo.presupuestos.dto.response.ResumenPresupuestoDTO;

import java.util.List;

public interface CronogramaService {

    List<CronogramaResponse> obtenerTodosLosCronogramas();

    ResumenPresupuestoDTO obtenerResumenPresupuesto(Integer idProyecto);


    EtapaResponse crearEtapa(EtapaRequest request);
    EtapaResponse actualizarEtapa(Integer id, EtapaRequest request);
    void eliminarEtapa(Integer id);
    List<EtapaResponse> obtenerEtapasPorCronograma(Integer idCronograma);

    // Actualización de costos reales
    EtapaResponse actualizarCostoReal(Integer idEtapa, Double costoReal);

    // Validaciones
    boolean validarSobrepasoPresupuesto(Integer idProyecto);
}