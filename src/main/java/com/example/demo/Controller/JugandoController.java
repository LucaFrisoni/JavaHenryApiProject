package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class JugandoController {

    @GetMapping("")
    public ResponseEntity<String> initialHi() {
        return ResponseEntity.ok("Welcome to my Expense and Income API y aguanteeeee Bocaaaaaaaaaa");
    }
}
