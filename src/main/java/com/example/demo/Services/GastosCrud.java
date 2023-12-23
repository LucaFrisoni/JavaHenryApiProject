package com.example.demo.Services;

import com.example.demo.Models.Gasto;


import java.util.List;
import java.util.Map;

public interface GastosCrud {
    //---------------Create-----------------------------------------------------------------------------------------------------//
    Gasto addExpense(Gasto gasto);

    //---------------Update-----------------------------------------------------------------------------------------------------//
    Gasto editExpense(double monto, String categoria, String descripcion, Number id);

    //---------------Delete-----------------------------------------------------------------------------------------------------//
    Object deleteExpense(Long id);

    //---------------Read-----------------------------------------------------------------------------------------------------//
    List<Gasto> getAllExpenses();

    List<String> getAllCategories();

    Map<String, Number> categoryCounter();

    //Filters Register
    List<Gasto> categoryFilter(String categoria);

    List<Gasto> mountFilter(double montoMinimo, double montoMaximo);


    List<Gasto> dayFilter(int dia, int mes, int anio);

    List<Gasto> weekFilter(int semana, int mes, int anio);

    List<Gasto> monthFilter(int mes, int anio);

    //Filter Reports
    double totalExpense();

    double categoryTotalExpense(String categoria);

    double categoryPercentage(String categoria);


    double dayTotalExpense(int dia, int mes, int anio);

    double weekTotalExpense(int semana, int mes, int anio);

    double monthTotalExpense(int mes, int anio);

    double maxExpenseMonth(int mes, int anio);

    double minExpenseMonth(int mes, int anio);
}
