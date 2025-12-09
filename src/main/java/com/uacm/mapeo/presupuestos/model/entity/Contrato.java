// src/main/java/com/uacm/mapeo/presupuestos/model/entity/Contrato.java
package com.uacm.mapeo.presupuestos.model.entity;

import com.uacm.mapeo.presupuestos.model.enums.Moneda;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "contrato")
@Data
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrato")
    private Long idContrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_presupuesto")
    private Presupuesto presupuesto;

    @Column(name = "numero_contrato", length = 50, nullable = false, unique = true)
    private String numeroContrato;

    @Column(name = "fecha_firma", nullable = false)
    private LocalDate fechaFirma;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;


    @Column(name = "monto_total", nullable = false)
    private Double montoTotal; // Quitar precision y scale

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", length = 10, nullable = false)
    private Moneda moneda;

    @Column(name = "condiciones_generales", columnDefinition = "TEXT")
    private String condicionesGenerales;

    @Column(name = "estatus", length = 20, nullable = false)
    private String estatus;
}