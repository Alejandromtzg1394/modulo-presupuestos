// src/main/java/com/uacm/mapeo/presupuestos/repository/ProyectoRepository.java
package com.uacm.mapeo.presupuestos.repository;

import com.uacm.mapeo.presupuestos.model.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    // ELIMINA cualquier método findById personalizado
    // Spring Data JPA ya proporciona:
    // Optional<Proyecto> findById(Long id) automáticamente

    // Solo mantén métodos personalizados si son necesarios:
    List<Proyecto> findByNombre(String nombre);
    List<Proyecto> findByEstado(String estado);
}