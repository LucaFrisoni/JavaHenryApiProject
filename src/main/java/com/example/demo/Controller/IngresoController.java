package com.example.demo.Controller;

import com.example.demo.Exceptions.JDBCExceptions;
import com.example.demo.Models.Gasto;
import com.example.demo.Models.Ingresos;
import com.example.demo.Services.IngresoService;
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
@RequestMapping("/income")
public class IngresoController {

    @Autowired
    IngresoService ingresoService;
    @Autowired
    ApiResponse apiResponse;

    @Autowired
    ObjectMapper objectMapper;

    //Create
    @PostMapping("/create")
    public ResponseEntity<String> addIncome(@RequestBody Ingresos ingresos) {

        if (ingresos == null || ingresos.getDescripcion().isEmpty() || ingresos.getCategoria().isEmpty() || ingresos.getMonto() <= 0) {
            return ResponseEntity.badRequest().body("You should provided an income Request Body with a description, category and amount.");
        }

        try {
            ingresos.setCategoria(StringUtils.capitalize(ingresos.getCategoria().toLowerCase()));

            Ingresos newIncome = ingresoService.addIncome(ingresos);

            String expensesJson = objectMapper.writeValueAsString(newIncome);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("New income created:\n" + expensesJson);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Read
    @GetMapping("/allIncomes")
    public ResponseEntity<String> allIncomes() {
        try {
            List<Ingresos> allExpenses = ingresoService.getAllIncomes();

            String expensesJson = objectMapper.writeValueAsString(allExpenses);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("All incomes:\n" + expensesJson);
        }catch (JDBCExceptions e) {
            return ResponseEntity.ok("No income charged");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/allCategoriesIncome")
    public ResponseEntity<String> allCategoriesIncome() {
        try {
            List<String> allCategories = ingresoService.getAllCategories();

            return ResponseEntity.ok("All categories:\n" + allCategories);
        }catch (JDBCExceptions e) {
            return ResponseEntity.ok("No income charged");
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
            List<Ingresos> filter = ingresoService.categoryFilter(category);

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


    @GetMapping("/monthFilter")
    public ResponseEntity<String> monthFilter(@RequestParam String mes, @RequestParam String anio) {

        try {

            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for month, without it being decimal");
            }
            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            List<Ingresos> filter = ingresoService.monthFilter(parsedMes, parsedAnio);

            String expensesJson = objectMapper.writeValueAsString(filter);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Filter from " + writtenMonth + " " + parsedAnio + ":\n" + expensesJson);

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
            double totalExpense = ingresoService.totalIncome();
            return ResponseEntity.ok("Total revenue : $" + totalExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/totalIncomeByCategory")
    public ResponseEntity<String> totalIncomeByCategory(@RequestParam String category) {

        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body("You should provided a category Param.");
        }

        try {
            double totalIncome = ingresoService.totalIncomeByCategory(category);
            return ResponseEntity.ok("Total revenue of " + category + ": $" + totalIncome);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @GetMapping("/monthTotalIncome")
    public ResponseEntity<String> monthTotalIncome(@RequestParam String mes, @RequestParam String anio) {

        try {
            int parsedMes = Integer.parseInt(mes);
            int parsedAnio = Integer.parseInt(anio);

            if (parsedMes <= 0 || parsedMes > 12) {
                return ResponseEntity.badRequest().body("You must enter a correct numerical value for month, without it being decimal");
            }
            String writtenMonth = apiResponse.obtenerNombreMes(parsedMes);

            double totalIncome = ingresoService.monthIncome(parsedMes, parsedAnio);

            return ResponseEntity.ok("Total expenditure for " + writtenMonth + ", " + parsedAnio + " : $" + totalIncome);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical values for month and year");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    //Update
    @PutMapping("/edit")
    public ResponseEntity<String> editIncome(@RequestParam(required = false) double monto, @RequestParam(required = false) String categoria, @RequestParam(required = false) String descripcion, @RequestParam Long id) {

        if (monto <= 0) {
           return ResponseEntity.badRequest().body("Expense mount value can`t be 0 or negative");
        }


        try {
            Ingresos editedIncome = ingresoService.editIncome(monto, categoria, descripcion, id);

            String expensesJson = objectMapper.writeValueAsString(editedIncome);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Income edit successfully: \n" + expensesJson);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical value for ID");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Delete
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteExpense(@RequestParam Long id) {
        try {
            Ingresos deleteExpense = ingresoService.deleteIncome(id);

            String expensesJson = objectMapper.writeValueAsString(deleteExpense);
            expensesJson = expensesJson.replace("{", "\n{");
            expensesJson = expensesJson.replace(",", ",\n");

            return ResponseEntity.ok("Expense deleted successfully: \n" + expensesJson);
        } catch (JDBCExceptions e) {
            return ResponseEntity.badRequest().body("Error deleting income: " + e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Please enter valid numerical value for ID");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
