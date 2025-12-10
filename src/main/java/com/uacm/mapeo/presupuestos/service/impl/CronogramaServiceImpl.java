package com.uacm.mapeo.presupuestos.service.impl;

import com.uacm.mapeo.presupuestos.dto.request.EtapaRequest;
import com.uacm.mapeo.presupuestos.dto.response.CronogramaResponse;
import com.uacm.mapeo.presupuestos.dto.response.EtapaResponse;
import com.uacm.mapeo.presupuestos.dto.response.ResumenPresupuestoDTO;
import com.uacm.mapeo.presupuestos.model.entity.*;
import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import com.uacm.mapeo.presupuestos.repository.*;
import com.uacm.mapeo.presupuestos.service.CronogramaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CronogramaServiceImpl implements CronogramaService {

    private final CronogramaRepository cronogramaRepository;
    private final EtapaRepository etapaRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    public List<CronogramaResponse> obtenerTodosLosCronogramas() {
        List<Cronograma> cronogramas = cronogramaRepository.findAll();

        return cronogramas.stream()
                .map(this::convertirCronogramaAResponse)
                .collect(Collectors.toList());
    }

    private CronogramaResponse convertirCronogramaAResponse(Cronograma cronograma) {
        if (cronograma == null) {
            return null;
        }

        return new CronogramaResponse(
                cronograma.getId(),
                cronograma.getProyecto() != null ? cronograma.getProyecto().getIdProyecto() : null,
                cronograma.getDescripcion(),
                cronograma.getFechaInicio(),
                cronograma.getFechaFinEstimada(),
                0.0, // presupuestoTotal - ajusta según necesites
                "ACTIVO" // estado - ajusta según necesites
        );
    }

    //-->
    @Override
    public ResumenPresupuestoDTO obtenerResumenPresupuesto(Integer idProyecto) {
        // Obtener proyecto
        Proyecto proyecto = proyectoRepository.findById(Long.valueOf(idProyecto))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Proyecto no encontrado con ID: " + idProyecto
                ));

        // Obtener totales de costos
        Double totalCostoEstimado = cronogramaRepository.sumCostoEstimadoByProyecto(idProyecto);
        Double totalCostoReal = cronogramaRepository.sumCostoRealByProyecto(idProyecto);

        // Si no hay presupuesto aprobado, usar 0 o un valor por defecto
        double presupuestoAprobado = 0.0; // O obtenerlo de otro lugar
        double estimado = totalCostoEstimado != null ? totalCostoEstimado : 0.0;
        double real = totalCostoReal != null ? totalCostoReal : 0.0;

        // Calcular desviaciones (si presupuesto es 0, las desviaciones serán negativas)
        double desviacionEstimada = presupuestoAprobado - estimado;
        double desviacionReal = presupuestoAprobado - real;

        // Determinar si sobrepasa (si no hay presupuesto, siempre es false)
        boolean sobrepasaPresupuesto = presupuestoAprobado > 0 && real > presupuestoAprobado;

        return ResumenPresupuestoDTO.builder()
                .idProyecto(idProyecto)
                .nombreProyecto(proyecto.getNombre())
                .presupuestoAprobado(presupuestoAprobado)
                .totalCostoEstimado(estimado)
                .totalCostoReal(real)
                .desviacionEstimada(desviacionEstimada)
                .desviacionReal(desviacionReal)
                .sobrepasaPresupuesto(sobrepasaPresupuesto)
                .mensajeAlerta(presupuestoAprobado == 0 ?
                        "Sin presupuesto asignado" :
                        (sobrepasaPresupuesto ? "Excede presupuesto" : "Dentro del presupuesto"))
                .build();
    }

    @Override
    @Transactional
    public EtapaResponse crearEtapa(EtapaRequest request) {
        // Validar cronograma existe
        Cronograma cronograma = cronogramaRepository.findById(request.getIdCronograma().intValue())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cronograma no encontrado con ID: " + request.getIdCronograma()
                ));

        // Crear y guardar etapa
        Etapa etapa = new Etapa();
        etapa.setCronograma(cronograma);
        etapa.setDescripcion(request.getDescripcion());
        etapa.setFechaInicio(request.getFechaInicio());
        etapa.setFechaFinEstimada(request.getFechaFinEstimada());
        etapa.setFechaFinReal(null);
        etapa.setCostoEstimado(request.getCostoEstimado() != null ? request.getCostoEstimado() : 0.0);
        etapa.setCostoReal(0.0);
        etapa.setPorcentajeAvance(request.getPorcentajeAvance() != null ? request.getPorcentajeAvance() : 0);

        Etapa etapaGuardada = etapaRepository.save(etapa);

        // Retornar respuesta
        return EtapaResponse.builder()
                .idEtapa(etapaGuardada.getIdEtapa())
                .idCronograma(etapaGuardada.getCronograma().getId())
                .idProyecto(etapaGuardada.getCronograma().getProyecto() != null ?
                        etapaGuardada.getCronograma().getProyecto().getIdProyecto() : null)
                .descripcion(etapaGuardada.getDescripcion())
                .fechaInicio(etapaGuardada.getFechaInicio())
                .fechaFinEstimada(etapaGuardada.getFechaFinEstimada())
                .fechaFinReal(etapaGuardada.getFechaFinReal())
                .costoEstimado(etapaGuardada.getCostoEstimado())
                .costoReal(etapaGuardada.getCostoReal())
                .porcentajeAvance(etapaGuardada.getPorcentajeAvance())
                .build();
    }

    private EtapaResponse construirResponse(Etapa etapa) {
        return EtapaResponse.builder()
                .idEtapa(etapa.getIdEtapa())
                .idCronograma(etapa.getCronograma().getId())
                .idProyecto(extractIdProyecto(etapa))
                .descripcion(etapa.getDescripcion())
                .fechaInicio(etapa.getFechaInicio())
                .fechaFinEstimada(etapa.getFechaFinEstimada())
                .fechaFinReal(etapa.getFechaFinReal())
                .costoEstimado(etapa.getCostoEstimado())
                .costoReal(etapa.getCostoReal())
                .porcentajeAvance(etapa.getPorcentajeAvance())
                .build();
    }

    private Integer extractIdProyecto(Etapa etapa) {
        return etapa.getCronograma().getProyecto() != null ?
                etapa.getCronograma().getProyecto().getIdProyecto() : null;
    }

    @Override
    public EtapaResponse actualizarEtapa(Integer id, EtapaRequest request) {
        return null;
    }

    @Override
    public void eliminarEtapa(Integer id) {

    }

    @Override
    public List<EtapaResponse> obtenerEtapasPorCronograma(Integer idCronograma) {
        return List.of();
    }

    @Override
    public EtapaResponse actualizarCostoReal(Integer idEtapa, Double costoReal) {
        return null;
    }

    @Override
    public boolean validarSobrepasoPresupuesto(Integer idProyecto) {
        return false;
    }
}
