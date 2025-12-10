package com.uacm.mapeo.presupuestos.repository;

import com.uacm.mapeo.presupuestos.model.entity.Presupuesto;
import com.uacm.mapeo.presupuestos.model.enums.EstadoPresupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    // Métodos básicos
    List<Presupuesto> findByProyectoIdProyecto(Long idProyecto);

    @Query("SELECT p FROM Presupuesto p JOIN p.proyecto pro WHERE pro.cliente.idCliente = :idCliente")
    List<Presupuesto> findByClienteId(@Param("idCliente") Long idCliente);

    // MÉTODO FALTANTE - Agrégalo:
    @Query("SELECT p FROM Presupuesto p WHERE " +
            "(:idProyecto IS NULL OR p.proyecto.idProyecto = :idProyecto) AND " +
            "(:idCliente IS NULL OR p.proyecto.cliente.idCliente = :idCliente) AND " +
            "(:estado IS NULL OR p.estado = :estado)")
    List<Presupuesto> findByFiltros(
            @Param("idProyecto") Long idProyecto,
            @Param("idCliente") Long idCliente,
            @Param("estado") EstadoPresupuesto estado);

    @Query("SELECT COALESCE(MAX(p.version), 0) FROM Presupuesto p WHERE p.proyecto.idProyecto = :idProyecto")
    Integer findMaxVersionByProyectoId(@Param("idProyecto") Long idProyecto);
}