package com.example.demo.Services;

import com.example.demo.Models.Ingresos;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngresosCrud {

    Ingresos addIncome(Ingresos ingreso);

    List<Ingresos> getAllIncomes();

    List<String> getAllCategories();

    List<Ingresos> categoryFilter(String categoria);

    List<Ingresos> monthFilter(int mes, int anio);

    double totalIncome();

    double totalIncomeByCategory(String categoria);

    double monthIncome(int mes, int anio);

    Ingresos deleteIncome(Long id);



    //---------------Edit-----------------------------------------------------------------------------------------------------//
    Ingresos editIncome(double monto, String categoria, String descripcion, Long id);
}
