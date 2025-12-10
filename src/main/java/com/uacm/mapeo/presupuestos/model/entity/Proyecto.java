// src/main/java/com/uacm/mapeo/presupuestos/model/entity/Proyecto.java
package com.uacm.mapeo.presupuestos.model.entity;

import com.uacm.mapeo.presupuestos.model.enums.EstadoProyecto;
import com.uacm.mapeo.presupuestos.model.enums.PrioridadProyecto;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "proyecto")
@Data
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Integer idProyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "retorno")
    private Double retorno; // Quitar precision y scale

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 30)
    private EstadoProyecto estado;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    private LocalDate fechaFinReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", length = 20)
    private PrioridadProyecto prioridad;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<Presupuesto> presupuestos;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<Contrato> contratos;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<Cronograma> cronogramas;
}