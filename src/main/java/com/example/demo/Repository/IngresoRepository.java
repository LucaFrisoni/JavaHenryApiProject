package com.example.demo.Repository;

import com.example.demo.Models.Gasto;
import com.example.demo.Models.Ingresos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngresoRepository extends JpaRepository<Ingresos, Number> {

    List<Ingresos> findByCategoriaIgnoreCase(String categoria);

    @Query("SELECT i FROM Ingresos i WHERE YEAR(i.fecha) = :anio AND MONTH(i.fecha) = :mes")
    List<Ingresos> findByFechaYearAndFechaMonth(@Param("anio") int anio, @Param("mes") int mes);

}
