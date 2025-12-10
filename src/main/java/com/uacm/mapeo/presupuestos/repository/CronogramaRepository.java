// src/main/java/com/uacm/mapeo/presupuestos/repository/CronogramaRepository.java
package com.uacm.mapeo.presupuestos.repository;

import com.uacm.mapeo.presupuestos.model.entity.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Integer> {



    @Query("SELECT COALESCE(SUM(e.costoEstimado), 0) FROM Etapa e WHERE e.cronograma.proyecto.idProyecto = :idProyecto")
    Double sumCostoEstimadoByProyecto(@Param("idProyecto") Integer idProyecto);


    @Query("SELECT COALESCE(SUM(e.costoReal), 0) FROM Etapa e WHERE e.cronograma.proyecto.idProyecto = :idProyecto")
    Double sumCostoRealByProyecto(@Param("idProyecto") Integer idProyecto);
}