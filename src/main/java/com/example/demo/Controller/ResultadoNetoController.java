package com.example.demo.Controller;

import com.example.demo.Exceptions.JDBCExceptions;
import com.example.demo.Models.Ingresos;
import com.example.demo.Services.ResultadoNetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/netResult")
public class ResultadoNetoController {


    @Autowired
    ResultadoNetoService resultadoNetoService;

    @GetMapping("/result")
    public ResponseEntity<String> allIncomes(@RequestParam String mes, @RequestParam String anio) {
        try {

            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            String netResult = resultadoNetoService.netResult(parsedMes, parsedAnio);
            return ResponseEntity.ok("Net Result : $" + netResult);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
