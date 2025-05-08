package com.company.employee.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.service.EmployeeService;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@RequestParam(value = "name", required = false) String name) {
        logger.info("Fetching all employees with name filter: {}", name);
        List<EmployeeDTO> employeeDTO = employeeService.getAllEmployees(name);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO);
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Integer id) {
        logger.info("Fetching employee details for ID: {}", id);
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO.get());
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        logger.info("Creating a new employee: {}", employeeDTO);
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Integer id, @RequestBody EmployeeDTO employeeDTO) {
        logger.info("Updating employee with ID: {}", id);
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedEmployee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOneEmployee(@PathVariable("id") Integer id) {
        logger.info("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).body("Employee " + id + " has been deleted.");
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllEmployees() {
        logger.info("Deleting all employees.");
        employeeService.deleteAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).body("All employees have been deleted.");
    }

    @GetMapping("/workingstatus/{workingStatus}")
    public ResponseEntity<List<EmployeeDTO>> findByWorkingStatus(@PathVariable("workingStatus") Boolean workingStatus) {
        logger.info("Fetching employees with working status: {}", workingStatus);
        List<EmployeeDTO> employeeDTO = employeeService.findByWorkingStatus(workingStatus);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<EmployeeDTO>> findByRoleStartingWith(@PathVariable("role") String rolePrefix) {
        logger.info("Fetching employees with roles starting with: {}", rolePrefix);
        List<EmployeeDTO> employeeDTO = employeeService.findByRoleStartingWith(rolePrefix);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO);
    }

    @GetMapping("/allEmployees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployee() {
        logger.info("Fetching all employees.");
        List<EmployeeDTO> employeeDTO = employeeService.getAllEmployee();
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO);
    }

    @GetMapping("/byName")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeByName(@RequestParam(value = "name", required = true) String name) {
        logger.info("Fetching employees by name: {}", name);
        List<EmployeeDTO> employeeDTO = employeeService.getEmployeeByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO);
    }

    @GetMapping("/allPagination")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBasedOnPage(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        logger.info("Fetching employees based on pagination - Page: {}, Size: {}, SortBy: {}, SortDir: {}",
                pageNumber, pageSize, sortBy, sortDir);
        List<EmployeeDTO> employeeDTO = employeeService.getEmployeesBasedOnPage(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDTO);
    }
}