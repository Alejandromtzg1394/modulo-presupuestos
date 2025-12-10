// src/main/java/com/uacm/mapeo/presupuestos/repository/EtapaRepository.java
package com.uacm.mapeo.presupuestos.repository;

import com.uacm.mapeo.presupuestos.model.entity.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EtapaRepository extends JpaRepository<Etapa, Integer> {

    List<Etapa> findByCronogramaId(Integer cronogramaId);
}