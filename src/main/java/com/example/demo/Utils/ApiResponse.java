package com.example.demo.Utils;

import com.example.demo.Models.Gasto;
import org.springframework.stereotype.Component;

@Component
public class ApiResponse {

    private String message;
    private Gasto gasto;

    public ApiResponse(Gasto gasto) {
        this.message = "Gasto nuevo creado";
        this.gasto = gasto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Gasto getGasto() {
        return gasto;
    }

    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    public  String obtenerNombreMes(int mes) {
        return switch (mes) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "Invalid month";
        };
    }
}
