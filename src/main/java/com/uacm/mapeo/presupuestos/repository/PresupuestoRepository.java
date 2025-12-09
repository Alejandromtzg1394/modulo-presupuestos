// src/main/java/com/uacm/mapeo/presupuestos/repository/PresupuestoRepository.java
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

    List<Presupuesto> findByProyectoIdProyecto(Long idProyecto);

    List<Presupuesto> findByEstado(EstadoPresupuesto estado);

    @Query("SELECT p FROM Presupuesto p WHERE p.proyecto.cliente.idCliente = :idCliente")
    List<Presupuesto> findByClienteId(@Param("idCliente") Long idCliente);

    Optional<Presupuesto> findByProyectoIdProyectoAndEstado(Long idProyecto, EstadoPresupuesto estado);

    @Query("SELECT MAX(p.version) FROM Presupuesto p WHERE p.proyecto.idProyecto = :proyectoId")
    Integer findMaxVersionByProyectoId(@Param("proyectoId") Long proyectoId);
}
