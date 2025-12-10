// src/main/java/com/uacm/mapeo/presupuestos/dto/response/EtapaResponse.java
package com.uacm.mapeo.presupuestos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtapaResponse {
    private Integer idEtapa;
    private Integer idCronograma;
    private Integer idProyecto;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    private LocalDate fechaFinReal;
    private Double costoEstimado;  // Cambiado a Double
    private Double costoReal;      // Cambiado a Double
    private Integer porcentajeAvance;

}