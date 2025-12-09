// src/main/java/com/uacm/mapeo/presupuestos/repository/ProyectoRepository.java
package com.uacm.mapeo.presupuestos.repository;

import com.uacm.mapeo.presupuestos.model.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    // Métodos básicos ya vienen con JpaRepository
}