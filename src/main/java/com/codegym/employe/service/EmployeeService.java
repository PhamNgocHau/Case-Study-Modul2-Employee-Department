package com.codegym.employe.service;

import com.codegym.employe.model.Department;
import com.codegym.employe.model.Employee;
import com.codegym.employe.model.EmployeeForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public interface EmployeeService extends GeneralService<Employee> {

    Page<Employee> findAll(Pageable pageable);

    Page<Employee> findAllByFirstNameContaining(String name, Pageable pageable);

//    Page<Employee> findAllByNameDepartment(String name, Pageable pageable);

   Iterable<Employee> findAllByNameDepartment(Department department);

//    Iterable<Employee> findByDepartment(Department department);
//

   void editEmployee(EmployeeForm employeeForm , String avatar);
   ArrayList<Employee>sortService();
}
