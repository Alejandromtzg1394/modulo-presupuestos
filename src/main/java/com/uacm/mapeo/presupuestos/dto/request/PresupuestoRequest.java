// src/main/java/com/uacm/mapeo/presupuestos/dto/request/PresupuestoRequest.java
package com.uacm.mapeo.presupuestos.dto.request;

import com.uacm.mapeo.presupuestos.model.enums.Moneda;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PresupuestoRequest {

    @NotNull(message = "El proyecto es requerido")
    private Long idProyecto;

    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double montoAprobado;

    @NotNull(message = "La moneda es requerida")
    private Moneda moneda;

    private String comentarios;

    private String usuarioCreacion;
}