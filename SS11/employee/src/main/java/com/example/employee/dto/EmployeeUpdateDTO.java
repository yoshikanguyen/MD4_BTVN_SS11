package com.example.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EmployeeUpdateDTO {

    @NotBlank(message = "Tên nhân viên không được để trống")
    @Size(min = 5, message = "Tên nhân viên phải có ít nhất 5 ký tự")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String department;

    private MultipartFile avatarFile; // Không bắt buộc
}
