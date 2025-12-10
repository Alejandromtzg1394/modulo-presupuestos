// src/main/java/com/uacm/mapeo/presupuestos/dto/response/CronogramaResponse.java
package com.uacm.mapeo.presupuestos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CronogramaResponse {
    private Integer idCronograma;
    private Integer idProyecto;
    private String nombreCronograma;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double presupuestoTotal;
    private String estado;
}