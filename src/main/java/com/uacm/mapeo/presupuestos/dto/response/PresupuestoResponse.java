// src/main/java/com/uacm/mapeo/presupuestos/dto/response/PresupuestoResponse.java
package com.uacm.mapeo.presupuestos.dto.response;

import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import com.uacm.mapeo.presupuestos.model.enums.Moneda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresupuestoResponse {
    private Long idPresupuesto;

    // Datos del Proyecto
    private Long idProyecto;
    private String nombreProyecto;
    private String estadoProyecto;

    // Datos del Cliente
    private Long idCliente;
    private String nombreCliente;
    private String rfcCliente;

    // Datos del Presupuesto
    private Double montoAprobado;
    private Moneda moneda;
    private LocalDateTime fechaAprobacion;
    private Integer version;
    private EstadoPresupuesto estado;
    private String comentarios;

    // Auditoría
    private LocalDateTime fechaCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
}