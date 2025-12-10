package com.uacm.mapeo.presupuestos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenPresupuestoDTO {

    private Integer idProyecto;

    private String nombreProyecto;
    private Double presupuestoAprobado;
    private Double totalCostoEstimado;
    private Double totalCostoReal;
    private Double desviacionEstimada;
    private Double desviacionReal;

    private boolean sobrepasaPresupuesto;
    private String mensajeAlerta;
}