
// src/main/java/com/uacm/mapeo/presupuestos/model/entity/Etapa.java
package com.uacm.mapeo.presupuestos.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "etapa")
@Data
public class Etapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etapa")
    private Integer idEtapa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cronograma", nullable = false)
    private Cronograma cronograma;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    private LocalDate fechaFinReal;

    @Column(name = "costo_estimado")
    private Double costoEstimado;  // Cambiado a Double

    @Column(name = "costo_real")
    private Double costoReal;      // Cambiado a Double

    @Column(name = "porcentaje_avance")
    private Integer porcentajeAvance; // 0-100

}