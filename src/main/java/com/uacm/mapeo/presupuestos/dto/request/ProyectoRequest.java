package com.uacm.mapeo.presupuestos.dto.request;

import com.uacm.mapeo.presupuestos.model.enums.EstadoProyecto;
import com.uacm.mapeo.presupuestos.model.enums.PrioridadProyecto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProyectoRequest {

    @NotNull(message = "El cliente es requerido")
    private Integer idCliente;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String nombre;

    @DecimalMin(value = "0.00", message = "El retorno no puede ser negativo")
    private Double retorno;

    @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
    private String categoria;

    private EstadoProyecto estado;

    private String descripcion;

    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o en el futuro")
    private LocalDate fechaInicio;

    @Future(message = "La fecha fin estimada debe ser en el futuro")
    private LocalDate fechaFinEstimada;

    private LocalDate fechaFinReal;

    private PrioridadProyecto prioridad;
}