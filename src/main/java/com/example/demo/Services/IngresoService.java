package com.example.demo.Services;

import com.example.demo.Exceptions.JDBCExceptions;
import com.example.demo.Models.Ingresos;
import com.example.demo.Repository.IngresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngresoService implements IngresosCrud {


    @Autowired
    IngresoRepository ingresoRepository;
    @Autowired
    JDBCExceptions jdbcExceptions;

    //---------------Create-----------------------------------------------------------------------------------------------------//
    @Override
    public Ingresos addIncome(Ingresos ingreso) {
        return ingresoRepository.save(ingreso);
    }

    //---------------Read-----------------------------------------------------------------------------------------------------//
    @Override
    public List<Ingresos> getAllIncomes() {
        List<Ingresos> allIncomes = ingresoRepository.findAll();
        if (allIncomes.isEmpty()) {
            throw jdbcExceptions.emptyArrayIncome();
        }
        return allIncomes;
    }

    @Override
    public List<String> getAllCategories() {
        List<Ingresos> allIncomes = ingresoRepository.findAll();
        if (allIncomes.isEmpty()) {
            throw jdbcExceptions.emptyArrayIncome();
        }
        return allIncomes.stream()
                .map(Ingresos::getCategoria)
                .distinct()
                .collect(Collectors.toList());

    }


    @Override
    public List<Ingresos> categoryFilter(String categoria) {
        List<Ingresos> filter = ingresoRepository.findByCategoriaIgnoreCase(categoria);
        if (filter.isEmpty()) {
            throw jdbcExceptions.incomeCategoryFilterNotFound();
        } else {
            return filter;
        }
    }

    @Override
    public List<Ingresos> monthFilter(int mes, int anio) {
        List<Ingresos> filter = ingresoRepository.findByFechaYearAndFechaMonth(anio, mes);

        if (filter.isEmpty()) {
            throw jdbcExceptions.incomeMonthFilterNotFound();
        } else {
            return filter;
        }
    }

    @Override
    public double totalIncome() {
        List<Ingresos> allIncomes = ingresoRepository.findAll();

        return allIncomes.stream()
                .mapToDouble(Ingresos::getMonto)
                .sum();
    }

    @Override
    public double totalIncomeByCategory(String categoria) {
        List<Ingresos> allCategoryIncomes= ingresoRepository.findByCategoriaIgnoreCase(categoria);

        if (allCategoryIncomes.isEmpty()) {
            throw jdbcExceptions.incomeCategoryFilterNotFound();
        } else {
            return allCategoryIncomes.stream()
                    .mapToDouble(Ingresos::getMonto)
                    .sum();
        }
    }

    @Override
    public double monthIncome(int mes, int anio) {
        List<Ingresos> ingresosDelMes = ingresoRepository.findByFechaYearAndFechaMonth(anio, mes);

        if (ingresosDelMes.isEmpty()) {
            throw jdbcExceptions.expenseMonthFilterNotFound();
        } else {
            return ingresosDelMes.stream()
                    .mapToDouble(Ingresos::getMonto)
                    .sum();
        }
    }

    //---------------Delete-----------------------------------------------------------------------------------------------------//
    @Override
    public Ingresos deleteIncome(Long id) {
        Optional<Ingresos> ingresoOptional = ingresoRepository.findById(id);

        // Verifica si el gasto existe
        if (ingresoOptional.isPresent()) {
            Ingresos ingresoEliminado = ingresoOptional.get();
            ingresoRepository.deleteById(id);
            return ingresoEliminado;
        } else {
            throw jdbcExceptions.incomeIDNotFound();
        }
    }



    //---------------Edit-----------------------------------------------------------------------------------------------------//
    @Override
    public Ingresos editIncome(double monto, String categoria, String descripcion, Long id) {
        Optional<Ingresos> ingresosOptional = ingresoRepository.findById(id);

        if (ingresosOptional.isPresent()) {
            Ingresos ingreso = ingresosOptional.get();

            if (monto > 0) {
                ingreso.setMonto(monto);
            }


            if (!categoria.isEmpty()) {
                ingreso.setCategoria(categoria);
            }


            if (!descripcion.isEmpty()) {
                ingreso.setDescripcion(descripcion);
            }

            return ingresoRepository.save(ingreso);
        } else {
            throw jdbcExceptions.incomeIDNotFound();
        }
    }
}
