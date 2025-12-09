// src/main/java/com/uacm/mapeo/presupuestos/dto/response/PresupuestoResponse.java
package com.uacm.mapeo.presupuestos.dto.response;

import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import com.uacm.mapeo.presupuestos.model.enums.Moneda;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PresupuestoResponse {
    private Long idPresupuesto;
    private Long idProyecto;
    private String nombreProyecto;
    private Long idCliente;
    private String nombreCliente;
    private Double montoAprobado;
    private Moneda moneda;
    private LocalDate fechaAprobacion;
    private Integer version;
    private EstadoPresupuesto estado;
    private String comentarios;
    private LocalDate fechaCreacion;
    private String usuarioCreacion;
}