package com.example.employee.service;

import com.example.employee.dto.EmployeeUpdateDTO;
import com.example.employee.model.Employee;

public interface EmployeeService {
    Employee updateEmployee(Long id, EmployeeUpdateDTO updateDTO);
}
