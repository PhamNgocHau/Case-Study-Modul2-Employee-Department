package com.codegym.employe.service;

import com.codegym.employe.model.Department;
import com.codegym.employe.model.Employee;
import com.codegym.employe.model.EmployeeForm;
import com.codegym.employe.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Iterable<Employee> findAllService() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findByIdService(Long id) {
        return employeeRepository.findOne(id);
    }

    @Override
    public void saveService(Employee employee) {
        employeeRepository.save(employee);
    }


    @Override
    public void removeService(Long id) {
        employeeRepository.delete(id);
    }

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Iterable<Employee> findAllByNameDepartment(Department department) {
        return employeeRepository.findAllByDepartment(department);
    }

    @Override
    public Page<Employee> findAllByFirstNameContaining(String firstName, Pageable pageable) {
        return employeeRepository.findAllByNameContaining(firstName, pageable);
    }

    @Override
    public void editEmployee(EmployeeForm employeeForm, String avatar) {
        Employee employeeObject = employeeRepository.findOne(employeeForm.getId());
        employeeObject.setName(employeeForm.getName());
        employeeObject.setBirthDate(employeeForm.getBirthDate());
        employeeObject.setAddress(employeeForm.getAddress());
        employeeObject.setAvatar(avatar);
        employeeObject.setSalary(employeeForm.getSalary());
        employeeObject.setDepartment(employeeForm.getDepartment());
        employeeRepository.save(employeeObject);
    }

    @Override
    public ArrayList<Employee> sortService() {
        ArrayList<Employee> arrayList=(ArrayList<Employee>)employeeRepository.findAll();
        Collections.sort(arrayList,new ChairWeightComparator());
        return arrayList;
    }
}



class ChairWeightComparator implements Comparator<Employee> {
    public int compare(Employee employee, Employee employee1) {
        return (int) employee.getSalary() - (int) employee1.getSalary();
    }
}