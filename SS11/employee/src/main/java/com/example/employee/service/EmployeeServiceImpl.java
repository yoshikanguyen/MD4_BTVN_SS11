package com.example.employee.service;

import com.example.employee.dto.EmployeeUpdateDTO;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CloudinaryService cloudinaryService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CloudinaryService cloudinaryService) {
        this.employeeRepository = employeeRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeUpdateDTO updateDTO) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + id));

        employee.setFullName(updateDTO.getFullName());
        employee.setEmail(updateDTO.getEmail());
        employee.setDepartment(updateDTO.getDepartment());

        // Dùng khi update file mới
        if (updateDTO.getAvatarFile() != null && !updateDTO.getAvatarFile().isEmpty()) {
            String newAvatarUrl = cloudinaryService.uploadImage(updateDTO.getAvatarFile());
            employee.setAvatarUrl(newAvatarUrl);
        }
        return employeeRepository.save(employee);
    }
}
