package com.example.demo.Services;

import com.example.demo.Exceptions.JDBCExceptions;
import com.example.demo.Models.Gasto;
import com.example.demo.Models.Ingresos;
import com.example.demo.Repository.GastoRepository;
import com.example.demo.Repository.IngresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultadoNetoService implements ResultadosNetosCrud {


    @Autowired
    IngresoService ingresoService;

    @Autowired
    GastoService gastoService;


    @Override
    public String netResult(int mes, int year) {
        try {
            double allExpenses = gastoService.monthTotalExpense(mes, year);
            double allIncomes = ingresoService.monthIncome(mes, year);
            double result = allIncomes - allExpenses;
            return Double.toString(result);
        } catch (JDBCExceptions e) {
            return (e.getMessage());
        } catch (Exception e) {
            return "Server Internal Error";
        }


    }
}
