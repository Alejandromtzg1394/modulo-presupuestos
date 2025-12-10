// src/main/java/com/uacm/mapeo/presupuestos/dto/request/EtapaRequest.java
package com.uacm.mapeo.presupuestos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EtapaRequest {

    @NotNull(message = "El cronograma es requerido")
    private Long idCronograma;

    @NotBlank(message = "La descripción es requerida")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o en el futuro")
    private LocalDate fechaInicio;

    @Future(message = "La fecha fin estimada debe ser en el futuro")
    private LocalDate fechaFinEstimada;

    @DecimalMin(value = "0.00", message = "El costo estimado no puede ser negativo")
    private Double costoEstimado;  // Cambiado a Double

    @Min(value = 0, message = "El porcentaje de avance no puede ser menor a 0")
    @Max(value = 100, message = "El porcentaje de avance no puede ser mayor a 100")
    private Integer porcentajeAvance;
}