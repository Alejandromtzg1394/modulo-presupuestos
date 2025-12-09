// src/main/java/com/uacm/mapeo/presupuestos/model/entity/Cliente.java
package com.uacm.mapeo.presupuestos.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "rfc", length = 13, unique = true)
    private String rfc;

    @Column(name = "contacto_principal", length = 150)
    private String contactoPrincipal;

    @Column(name = "puesto_contacto", length = 100)
    private String puestoContacto;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Proyecto> proyectos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Contrato> contratos;
}