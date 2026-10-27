package com.example.employee.controller;

import com.example.employee.dto.EmployeeUpdateDTO;
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getEmployees() {
        List<String> employees = List.of("Employee 1", "Employee 2", "Employee 3");
        return ResponseEntity.ok(employees);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute EmployeeUpdateDTO updateDTO) {
        Employee updatedEmployee = employeeService.updateEmployee(id, updateDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
}