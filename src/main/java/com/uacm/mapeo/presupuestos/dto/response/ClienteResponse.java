package com.uacm.mapeo.presupuestos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Integer idCliente;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String rfc;
    private String contactoPrincipal;
    private String puestoContacto;
    private String observaciones;
}