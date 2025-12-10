package com.uacm.mapeo.presupuestos.model.entity;

import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import com.uacm.mapeo.presupuestos.model.enums.Moneda;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "presupuesto")
@Data
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_presupuesto")
    private  Integer idPresupuesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    // CORRECCIÓN: QUITA precision y scale
    @Column(name = "monto_aprobado", nullable = false)
    private Double montoAprobado;

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", length = 10, nullable = false)
    private Moneda moneda;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 30, nullable = false)
    private EstadoPresupuesto estado = EstadoPresupuesto.BORRADOR;

    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}