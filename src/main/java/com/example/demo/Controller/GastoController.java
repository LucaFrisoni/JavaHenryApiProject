package com.example.demo.Controller;

import com.example.demo.Exceptions.JDBCExceptions;
import com.example.demo.Models.Gasto;
import com.example.demo.Services.GastoService;
import com.example.demo.Utils.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/expense")
public class GastoController {

    @Autowired
    GastoService gastoService;
    @Autowired
     ApiResponse apiResponse;

    @Autowired
     ObjectMapper objectMapper;



    //---------------Create---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    @PostMapping("/create")
    public ResponseEntity<String> addExpense(@RequestBody Gasto gasto) {
        if (gasto == null || gasto.getDescripcion().isEmpty() || gasto.getCategoria().isEmpty() || gasto.getMonto() <= 0) {
            return ResponseEntity.badRequest().body("You should provided an expense Request Body with a description, category and amount.");
        }

        try {
            gasto.setCategoria(StringUtils.capitalize(gasto.getCategoria().toLowerCase()));

            Gasto newGasto = gastoService.addExpense(gasto);

            String expensesJson = objectMapper.writeValueAsString(newGasto);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("New expense created:\n" + expensesJson);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    //---------------Read-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    @GetMapping("/allExpenses")
    public ResponseEntity<String> allExpenses() {
        try {
            List<Gasto> allExpenses = gastoService.getAllExpenses();

            String expensesJson = objectMapper.writeValueAsString(allExpenses);

            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("All expenses:\n" + expensesJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/allCategories")
    public ResponseEntity<String> allCategories() {
        try {
            List<String> allCategories = gastoService.getAllCategories();
            return ResponseEntity.ok("All categories:\n" + allCategories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/categoryFilter")
    public ResponseEntity<String> categoryFilter(@RequestParam String category) {
        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body("You should provided a category Param.");
        }

        try {
            List<Gasto> filter = gastoService.categoryFilter(category);

            String expensesJson = objectMapper.writeValueAsString(filter);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Filter by " + category + ":\n" + expensesJson);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/mountFilter")
    public ResponseEntity<String> mountFilter(@RequestParam double min, @RequestParam double max) {

        if (min <= 0 || max <= 0 || min > max) {
            return ResponseEntity.badRequest().body("Provide correct values for min and max");
        }
        try {
            List<Gasto> filter = gastoService.mountFilter(min, max);

            String expensesJson = objectMapper.writeValueAsString(filter);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Filter between $" + min + " and $" + max + ":\n" + expensesJson);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/dayFilter")
    public ResponseEntity<String> dayFilter(@RequestParam String dia, @RequestParam String mes, @RequestParam String anio) {

        try {
            int parsedDia = Integer.parseInt(dia);
            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedDia <= 0 || parsedDia > 31 || parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for day and month, without it being decimal");
            }

            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            List<Gasto> filter = gastoService.dayFilter(parsedDia, parsedMes, parsedAnio);

            String expensesJson = objectMapper.writeValueAsString(filter);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Filter for " + writtenMonth + " " + parsedDia + " " + parsedAnio + ":\n" + expensesJson);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for day, month, and year");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    @GetMapping("/weekFilter")
    public ResponseEntity<String> weekFilter(@RequestParam String semana, @RequestParam String mes, @RequestParam String anio) {

        try {

            int parsedSemana = Integer.parseInt(semana);
            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedSemana <= 0 || parsedSemana > 5 || parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for week and month, without it being decimal.Weeks count as numerical values from 1 to 5");
            }

            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            List<Gasto> filter = gastoService.weekFilter(parsedSemana, parsedMes, parsedAnio);

            String expensesJson = objectMapper.writeValueAsString(filter);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Filter of week " + parsedSemana + " of " + writtenMonth + " " + parsedAnio + ":\n" + expensesJson);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for week, month, and year");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/monthFilter")
    public ResponseEntity<String> monthFilter(@RequestParam String mes, @RequestParam String anio) {

        try {

            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for month, without it being decimal");
            }
            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            List<Gasto> filter = gastoService.monthFilter(parsedMes, parsedAnio);

            String expensesJson = objectMapper.writeValueAsString(filter);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Filter for " + writtenMonth + " " + parsedAnio + ":\n" + expensesJson);

        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for month and year");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/totalExpense")
    public ResponseEntity<String> totalExpense() {

        try {
            double totalExpense = gastoService.totalExpense();
            return ResponseEntity.ok("Total expenditure: $" + totalExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/totalExpenseByCategory")
    public ResponseEntity<String> totalExpenseByCategory(@RequestParam String category) {

        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body("You should provided a category Param.");
        }

        try {
            double totalExpense = gastoService.categoryTotalExpense(category);
            return ResponseEntity.ok("Total expenditure by " + category + ": $" + totalExpense);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/totalExpenseByPorcentualCategory")
    public ResponseEntity<String> totalExpenseByPorcentualCategory(@RequestParam String category) {

        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body("You should provided a category Param.");
        }

        try {
            double stadistic = gastoService.categoryPercentage(category);
            return ResponseEntity.ok("Porcentual stadistic of " + category + " : %" + stadistic);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/dayTotalExpense")
    public ResponseEntity<String> dayTotalExpense(@RequestParam String dia, @RequestParam String mes, @RequestParam String anio) {

        try {

            int parsedDia = Integer.parseInt(dia);
            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);
            if (parsedDia <= 0 || parsedDia > 31 || parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for day and month, without it being decimal");
            }
            double totalExpense = gastoService.dayTotalExpense(parsedDia, parsedMes, parsedAnio);
            return ResponseEntity.ok("Total expenditure for " + writtenMonth + " " + parsedDia + ", " + parsedAnio + ": $" + totalExpense);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for day, month, and year");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/weekTotalExpense")
    public ResponseEntity<String> weekTotalExpense(@RequestParam String semana, @RequestParam String mes, @RequestParam String anio) {

        try {
            int parsedSemana = Integer.parseInt(semana);
            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedSemana <= 0 || parsedSemana > 5 || parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for week and month, without it being decimal.Weeks count as numerical values from 1 to 5");
            }

            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            double totalExpense = gastoService.weekTotalExpense(parsedSemana, parsedMes, parsedAnio);
            return ResponseEntity.ok("Total expenditure for week" + parsedSemana + " of " + parsedMes + ", " + parsedAnio + " : $" + totalExpense);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for week, month and year");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/monthTotalExpense")
    public ResponseEntity<String> monthTotalExpense(@RequestParam String mes, @RequestParam String anio) {

        try {
            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for month, without it being decimal");
            }
            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            double totalExpense = gastoService.monthTotalExpense(parsedMes, parsedAnio);

            return ResponseEntity.ok("Total expenditure for " + writtenMonth + ", " + parsedAnio + " : $" + totalExpense);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for month and year");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/maxORmin/{value}")
    public ResponseEntity<String> monthTotalExpense(@PathVariable String value, @RequestParam String mes, @RequestParam String anio) {

        try {
            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for month, without it being decimal");
            }
            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            if (value.equalsIgnoreCase("max")) {
                double maxExpense = gastoService.maxExpenseMonth(parsedMes, parsedAnio);
                return ResponseEntity.ok("Maximum expense for " + writtenMonth + " of " + anio + ": " + maxExpense);
            }
            if (value.equalsIgnoreCase("min")) {
                double minExpense = gastoService.minExpenseMonth(parsedMes, parsedAnio);
                return ResponseEntity.ok("Minimum expense for " + writtenMonth + " of " + anio + ": " + minExpense);
            } else {

                return ResponseEntity.badRequest().body("Only min or max values are allowed for path variable");
            }
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for month and year");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    //---------------Update-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

    @PutMapping("/edit")
    public ResponseEntity<String> editExpense(@RequestParam(required = false) double monto, @RequestParam(required = false) String categoria, @RequestParam(required = false) String descripcion, @RequestParam String id) {

        if (monto <= 0) {
           return ResponseEntity.badRequest().body("Expense mount value can`t be 0 or negative");
        }
        Number parsedID = NumberUtils.createNumber(id); // Convertir String a Number

        try {
            Gasto editedExpense = gastoService.editExpense(monto, categoria, descripcion, parsedID);

            String expensesJson = objectMapper.writeValueAsString(editedExpense);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Expense edit successfully: \n" + expensesJson);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical value for ID");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    //---------------Delete-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteExpense(@RequestParam Long id) {
        try {
            Gasto deleteExpense = gastoService.deleteExpense(id);

            String expensesJson = objectMapper.writeValueAsString(deleteExpense);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Expense deleted successfully \n" + expensesJson);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body("Error deleting expense: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
