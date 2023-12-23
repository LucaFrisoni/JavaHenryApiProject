package com.example.demo.Repository;

import com.example.demo.Models.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Number> {
    List<Gasto> findByCategoriaIgnoreCase(String categoria);

    List<Gasto> findByMontoBetween(double montoMinimo, double montoMaximo);

    @Query("SELECT g FROM Gasto g WHERE YEAR(g.fecha) = :year AND MONTH(g.fecha) = :month AND DAY(g.fecha) = :day")
    List<Gasto> findByFechaYearAndFechaMonthAndFechaDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);


    @Query("SELECT g FROM Gasto g WHERE YEAR(g.fecha) = :year AND MONTH(g.fecha) = :month")
    List<Gasto> findByFechaYearAndFechaMonth(@Param("year") int year, @Param("month") int month);

}
