package com.company.employee.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.exception.EmployeeNotFoundException;
import com.company.employee.exception.EmptyEmployeeException;
import com.company.employee.mapper.EmployeeMapper;
import com.company.employee.model.Employee;
import com.company.employee.repository.EmployeeRepository;
import com.company.employee.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeDTO> getAllEmployees(String name) {
        logger.info("Fetching all employees. Filter by name: {}", name);

        List<Employee> employee = (name == null) ? employeeRepository.findAll() : employeeRepository.findByName(name);
        List<EmployeeDTO> employeeDTO = employee.stream().map(EmployeeMapper::convertToDTO).toList();

        if (employeeDTO.isEmpty()) {
            logger.warn("Employee database is empty or no employees found for the given name.");
            throw new EmptyEmployeeException("Employee Database is empty");
        }

        logger.info("Successfully fetched {} employees.", employeeDTO.size());
        return employeeDTO;
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(Integer id) {
        logger.info("Fetching employee by ID: {}", id);
        return Optional.ofNullable(employeeRepository.findById(id)
                .map(EmployeeMapper::convertToDTO)
                .orElseThrow(() -> {
                    logger.error("Employee with ID {} not found.", id);
                    return new EmployeeNotFoundException("Employee with ID " + id + " not found.");
                }));
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        logger.info("Creating a new employee: {}", employeeDTO);
        Employee employee = EmployeeMapper.convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);

        logger.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return EmployeeMapper.convertToDTO(savedEmployee);
    }

    public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTOs) {
        logger.info("Updating employee with ID: {}", id);
        Optional<Employee> employeeData = employeeRepository.findById(id);

        if (employeeData.isPresent()) {
            Employee tempEmployeeData = employeeData.get();
            tempEmployeeData.setName(employeeDTOs.getName());
            tempEmployeeData.setRole(employeeDTOs.getRole());
            tempEmployeeData.setExperience(employeeDTOs.getExperience());
            tempEmployeeData.setWorkingStatus(employeeDTOs.getWorkingStatus());

            Employee updatedEmployee = employeeRepository.save(tempEmployeeData);

            logger.info("Employee with ID {} updated successfully.", id);
            return EmployeeMapper.convertToDTO(updatedEmployee);
        } else {
            logger.error("Employee with ID {} not found.", id);
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
    }

    @Override
    public void deleteEmployee(Integer id) {
        logger.info("Deleting employee with ID: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
            logger.info("Employee with ID {} deleted successfully.", id);
        } else {
            logger.error("Employee with ID {} not found.", id);
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
    }

    @Override
    public void deleteAllEmployees() {
        logger.info("Deleting all employees.");
        long employeeCount = employeeRepository.count();

        if (employeeCount == 0) {
            logger.warn("Employee database is empty.");
            throw new EmptyEmployeeException("Employee Database is empty");
        } else {
            employeeRepository.deleteAll();
            logger.info("All employees deleted successfully.");
        }
    }

    @Override
    public List<EmployeeDTO> findByWorkingStatus(Boolean workingStatus) {
        logger.info("Fetching employees by working status: {}", workingStatus);
        List<Employee> employee = employeeRepository.findByWorkingStatus(workingStatus);
        List<EmployeeDTO> employeeDTO = employee.stream().map(EmployeeMapper::convertToDTO).toList();

        if (employeeDTO.isEmpty()) {
            logger.warn("No employees found with working status: {}", workingStatus);
            throw new EmployeeNotFoundException("Employee with working status " + workingStatus + " are not found.");
        }

        logger.info("Successfully fetched {} employees with working status {}.", employeeDTO.size(), workingStatus);
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> findByRoleStartingWith(String rolePrefix) {
        logger.info("Fetching employees whose roles start with: {}", rolePrefix);
        List<Employee> employee = employeeRepository.findByRoleStartingWith(rolePrefix);
        List<EmployeeDTO> employeeDTO = employee.stream().map(EmployeeMapper::convertToDTO).toList();

        if (employeeDTO.isEmpty()) {
            logger.warn("No employees found with roles starting with: {}", rolePrefix);
            throw new EmployeeNotFoundException("Employee with role " + rolePrefix + " are not found.");
        }

        logger.info("Successfully fetched {} employees with roles starting with {}.", employeeDTO.size(), rolePrefix);
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        logger.info("Fetching all employees without filters.");
        List<Employee> employee = employeeRepository.getAllEmployee();
        List<EmployeeDTO> employeeDTO = employee.stream().map(EmployeeMapper::convertToDTO).toList();

        if (employeeDTO.isEmpty()) {
            logger.warn("Employee database is empty.");
            throw new EmptyEmployeeException("Employee Database is empty");
        }

        logger.info("Successfully fetched {} employees.", employeeDTO.size());
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeeByName(String name) {
        logger.info("Fetching employees by name: {}", name);
        List<Employee> employee = employeeRepository.getEmployeeByName(name);
        List<EmployeeDTO> employeeDTO = employee.stream().map(EmployeeMapper::convertToDTO).toList();

        if (employeeDTO.isEmpty()) {
            logger.warn("No employees found with name: {}", name);
            throw new EmployeeNotFoundException("Employee with name " + name + " are not found.");
        }

        logger.info("Successfully fetched {} employees with name {}.", employeeDTO.size(), name);
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesBasedOnPage(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        logger.info("Fetching employees with pagination - Page: {}, Size: {}, SortBy: {}, SortDir: {}", 
                pageNumber, pageSize, sortBy, sortDir);

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Employee> pageEmployee = employeeRepository.findAll(pageable);

        List<Employee> contentEmployee = pageEmployee.getContent();
        List<EmployeeDTO> employeeDTO = contentEmployee.stream().map(EmployeeMapper::convertToDTO).toList();

        if (employeeDTO.isEmpty()) {
            logger.warn("No employees found in the requested page.");
        }

        logger.info("Successfully fetched {} employees in the requested page.", employeeDTO.size());
        return employeeDTO;
    }
}