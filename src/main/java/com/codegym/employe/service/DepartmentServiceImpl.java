package com.codegym.employe.service;

import com.codegym.employe.model.Department;
import com.codegym.employe.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Iterable<Department> findAllService() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findByIdService(Long id) {
        return departmentRepository.findOne(id);
    }

    @Override
    public void saveService(Department department) {
        departmentRepository.save(department);
    }

    @Override
    public void removeService(Long id) {
        departmentRepository.delete(id);
    }
}
