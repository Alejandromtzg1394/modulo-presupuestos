// src/main/java/com/uacm/mapeo/presupuestos/repository/ContratoRepository.java
package com.uacm.mapeo.presupuestos.repository;

import com.uacm.mapeo.presupuestos.model.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findByClienteIdCliente(Long idCliente);
    List<Contrato> findByProyectoIdProyecto(Long idProyecto);
    boolean existsByNumeroContrato(String numeroContrato);
}