package com.example.demo.Services;

import com.example.demo.Exceptions.JDBCExceptions;
import com.example.demo.Models.Gasto;
import com.example.demo.Repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GastoService implements GastosCrud {

    @Autowired
    GastoRepository gastoRepository;

    @Autowired
    JDBCExceptions jdbcExceptions;

    //---------------Create-----------------------------------------------------------------------------------------------------//
    @Override
    public Gasto addExpense(Gasto gasto) {
        return gastoRepository.save(gasto);
    }

    //---------------Read-----------------------------------------------------------------------------------------------------//
    @Override
    public List<Gasto> getAllExpenses() {
        return gastoRepository.findAll();
    }

    @Override
    public List<String> getAllCategories() {
        List<Gasto> allExpenses = gastoRepository.findAll();

        return allExpenses.stream()
                .map(Gasto::getCategoria)
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    public Map<String, Number> categoryCounter() {
        return null;
    }

    @Override
    public List<Gasto> categoryFilter(String categoria) {
        List<Gasto> filter = gastoRepository.findByCategoriaIgnoreCase(categoria);
        if (filter.isEmpty()) {
            throw jdbcExceptions.expenseCategoryFilterNotFound();
        } else {
            return filter;
        }
    }

    @Override
    public List<Gasto> mountFilter(double montoMinimo, double montoMaximo) {
        List<Gasto> filter = gastoRepository.findByMontoBetween(montoMinimo, montoMaximo);
        if (filter.isEmpty()) {
            throw jdbcExceptions.expenseMountFilterNotFound();
        } else {
            return filter;
        }
    }

    @Override
    public List<Gasto> dayFilter(int dia, int mes, int anio) {

        List<Gasto> filter = gastoRepository.findByFechaYearAndFechaMonthAndFechaDay(anio, mes, dia);
        if (filter.isEmpty()) {
            throw jdbcExceptions.expenseDayFilterNotFound();
        } else {
            return filter;
        }
    }

    @Override
    public List<Gasto> weekFilter(int semana, int mes, int anio) {

        List<Gasto> gastosDelMes = gastoRepository.findByFechaYearAndFechaMonth(anio, mes);


        List<Gasto> filter = gastosDelMes.stream()
                .filter(g -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(g.getFecha());
                    int dia = calendar.get(Calendar.DAY_OF_MONTH);
                    int semanaCalculada = (int) Math.ceil((double) dia / 7);
                    return semanaCalculada == semana;
                })
                .collect(Collectors.toList());
        if (filter.isEmpty()) {
            throw jdbcExceptions.expenseWeekFilterNotFound();
        } else {
            return filter;
        }

    }

    @Override
    public List<Gasto> monthFilter(int mes, int anio) {

        List<Gasto> filter = gastoRepository.findByFechaYearAndFechaMonth(anio, mes);

        if (filter.isEmpty()) {
            throw jdbcExceptions.expenseMonthFilterNotFound();
        } else {
            return filter;
        }
    }

    @Override
    public double totalExpense() {
        List<Gasto> allExpenses = gastoRepository.findAll();

        return allExpenses.stream()
                .mapToDouble(Gasto::getMonto)
                .sum();
    }

    @Override
    public double categoryTotalExpense(String categoria) {
        List<Gasto> allCategoryExpenses = gastoRepository.findByCategoriaIgnoreCase(categoria);

        if (allCategoryExpenses.isEmpty()) {
            throw jdbcExceptions.expenseCategoryFilterNotFound();
        } else {
            return allCategoryExpenses.stream()
                    .mapToDouble(Gasto::getMonto)
                    .sum();
        }


    }


    @Override
    public double categoryPercentage(String categoria) {
        double totalExpense = totalExpense();
        double categoryExpense = categoryTotalExpense(categoria);
        return (categoryExpense * 100) / totalExpense;
    }


    @Override
    public double dayTotalExpense(int dia, int mes, int anio) {
        List<Gasto> gastosDelDia = gastoRepository.findByFechaYearAndFechaMonthAndFechaDay(anio, mes, dia);

        if (gastosDelDia.isEmpty()) {
            throw jdbcExceptions.expenseDayFilterNotFound();
        } else {
            return gastosDelDia.stream()
                    .mapToDouble(Gasto::getMonto)
                    .sum();
        }


    }

    @Override
    public double weekTotalExpense(int semana, int mes, int anio) {
        List<Gasto> gastosWeek = weekFilter(semana, mes, anio);

        if (gastosWeek.isEmpty()) {
            throw jdbcExceptions.expenseWeekFilterNotFound();
        } else {
            return gastosWeek.stream()
                    .mapToDouble(Gasto::getMonto)
                    .sum();
        }

    }

    @Override
    public double monthTotalExpense(int mes, int anio) {
        List<Gasto> gastosDelMes = gastoRepository.findByFechaYearAndFechaMonth(anio, mes);

        if (gastosDelMes.isEmpty()) {
            throw jdbcExceptions.expenseMonthFilterNotFound();
        } else {
            return gastosDelMes.stream()
                    .mapToDouble(Gasto::getMonto)
                    .sum();
        }


    }

    @Override
    public double maxExpenseMonth(int mes, int anio) {
        List<Gasto> gastosDelMes = gastoRepository.findByFechaYearAndFechaMonth(anio, mes);

        if (gastosDelMes.isEmpty()) {
            throw jdbcExceptions.expenseMonthFilterNotFound();
        } else {
            Optional<Gasto> minExpense = gastosDelMes.stream()
                    .max(Comparator.comparingDouble(Gasto::getMonto));

            return minExpense.map(Gasto::getMonto).orElse(0.0);
        }
    }

    @Override
    public double minExpenseMonth(int mes, int anio) {
        List<Gasto> gastosDelMes = gastoRepository.findByFechaYearAndFechaMonth(anio, mes);

        if (gastosDelMes.isEmpty()) {
            throw jdbcExceptions.expenseMonthFilterNotFound();
        } else {
            Optional<Gasto> minExpense = gastosDelMes.stream()
                    .min(Comparator.comparingDouble(Gasto::getMonto));

            return minExpense.map(Gasto::getMonto).orElse(0.0);
        }


    }

    //---------------Update-----------------------------------------------------------------------------------------------------//
    @Override
    public Gasto editExpense(double monto, String categoria, String descripcion, Number id) {
        Optional<Gasto> gastoOptional = gastoRepository.findById(id);

        if (gastoOptional.isPresent()) {
            Gasto gasto = gastoOptional.get();

            if (monto > 0) {
                System.out.println("categoria =>" + monto);
                gasto.setMonto(monto);
            }


            if (!categoria.isEmpty()) {
                gasto.setCategoria(categoria);
            }


            if (!descripcion.isEmpty()) {
                gasto.setDescripcion(descripcion);
            }

            return gastoRepository.save(gasto);
        } else {
            throw jdbcExceptions.expenseIDNotFound();
        }
    }

    //---------------Delete-----------------------------------------------------------------------------------------------------//
    @Override
    public Gasto deleteExpense(Long id) {
        Optional<Gasto> gastoOptional = gastoRepository.findById(id);

        // Verifica si el gasto existe
        if (gastoOptional.isPresent()) {
            Gasto gastoEliminado = gastoOptional.get();
            gastoRepository.deleteById(id);
            return gastoEliminado;
        } else {
            throw jdbcExceptions.expenseIDNotFound();
        }

    }


}
