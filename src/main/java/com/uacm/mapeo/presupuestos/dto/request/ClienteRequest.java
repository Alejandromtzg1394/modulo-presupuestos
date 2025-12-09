package com.uacm.mapeo.presupuestos.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteRequest {

    @NotBlank(message = "El nombre es requerido") //-- Entra
    @Size(max = 20, message = "El nombre no puede exceder 20 caracteres")
    private String nombre;

    @Size(max = 100, message = "La dirección no puede exceder 100 caracteres")
    private String direccion;

    @Pattern(regexp = "[0-9]{10}$", message = "Teléfono inválido")  //Solo numeros 10 fijo
    private String telefono;

    @Email(message = "Email inválido")
    @Size(max = 20, message = "Email no puede exceder 20 caracteres")
    private String email;

    @Pattern(regexp = "^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$", message = "RFC inválido")
    private String rfc;

    @Size(max = 100, message = "Contacto principal no puede exceder 100 caracteres")
    private String contactoPrincipal;

    @Size(max = 100, message = "Puesto no puede exceder 100 caracteres")
    private String puestoContacto;

    private String observaciones;
}