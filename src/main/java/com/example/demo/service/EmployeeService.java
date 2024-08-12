package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee){
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, Employee updateEmployee){
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if(existingEmployee.isPresent()){
            Employee updateExistingEmployee = existingEmployee.get();
            updateExistingEmployee.setName(updateEmployee.getName());
            updateExistingEmployee.setAge(updateEmployee.getAge());
            updateExistingEmployee.setEmail(updateEmployee.getEmail());
            if(updateEmployee.getPassword() != null && updateEmployee.getPassword().isEmpty()){
                updateExistingEmployee.setPassword(passwordEncoder.encode(updateEmployee.getPassword()));
            }
            return employeeRepository.save(updateExistingEmployee);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
}
