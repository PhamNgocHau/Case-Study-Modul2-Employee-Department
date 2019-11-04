package com.codegym.employe.controller;

import com.codegym.employe.model.Department;
import com.codegym.employe.model.Employee;
import com.codegym.employe.model.EmployeeForm;
import com.codegym.employe.service.DepartmentService;
import com.codegym.employe.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@PropertySource("classpath:global_config_app.properties")
public class EmployeeController {

    @Autowired
    Environment env;

    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public DepartmentService departmentService;

    @ModelAttribute("departments")
    public Iterable<Department> departments() {
        return departmentService.findAllService();
    }

    @GetMapping("/employees")
    public ModelAndView listEmployee(@RequestParam("s") Optional<String> s, @PageableDefault(value = 3) Pageable pageable) {

        Page<Employee> employees;
        if (s.isPresent()) {
            employees = employeeService.findAllByFirstNameContaining(s.get(), pageable);
        } else {
            employees = employeeService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }

    @GetMapping("/create-employee")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employeeForm", new EmployeeForm());
        return modelAndView;
    }

    @PostMapping("/save-employee")
    public ModelAndView saveEmployee(@ModelAttribute("employeeForm") EmployeeForm employeeForm, BindingResult result) {

        // thong bao neu xay ra loi
//        if (result.hasErrors()){
//            ModelAndView modelAndView = new ModelAndView("/employee/create");
//            modelAndView.addObject("employeeForm",new EmployeeForm());
//            return modelAndView;
//        }

        // lay ten file
        MultipartFile multipartFile = employeeForm.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        //Luu file vao o cung
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // tham khảo: https://github.com/codegym-vn/spring-static-resources

        // tao doi tuong de luu vao db
        Employee employeeObject = new Employee(employeeForm.getName(), employeeForm.getBirthDate(),
                employeeForm.getAddress(), fileName, employeeForm.getSalary(), employeeForm.getDepartment());

        // luu vao db
        employeeService.saveService(employeeObject);

        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employee", new EmployeeForm());
        modelAndView.addObject("message", "New Employee created successfully");
        return modelAndView;
    }

    @GetMapping("/edit-employee/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Employee employee = employeeService.findByIdService(id);
        if (employee != null) {
            ModelAndView modelAndView = new ModelAndView("/employee/edit");
            modelAndView.addObject("employeeForm", employee);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("edit-employee")
    public ModelAndView updateEmployee(@ModelAttribute("employeeForm") EmployeeForm employeeForm) {

        // lay ten file
        MultipartFile multipartFile = employeeForm.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        //Luu file len server
        try {
            FileCopyUtils.copy(employeeForm.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        employeeService.editEmployee(employeeForm, fileName);

//        employeeService.saveService(employee);
        ModelAndView modelAndView = new ModelAndView("/employee/edit");
        modelAndView.addObject("employeeForm", new EmployeeForm());
        modelAndView.addObject("messages", "employee updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-employee/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Employee employee = employeeService.findByIdService(id);
        if (employee != null) {
            ModelAndView modelAndView = new ModelAndView("/employee/delete");
            modelAndView.addObject("employee", employee);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-employee")
    public String deleteEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.removeService(employee.getId());
        return "redirect:employees";
    }

    @GetMapping("/view-employee/{id}")
    public ModelAndView viewEmployee(@PathVariable Long id) {
        Employee employee = employeeService.findByIdService(id);
        if (employee != null) {
            ModelAndView modelAndView = new ModelAndView("/employee/view");
            modelAndView.addObject("employee", employee);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }


    @GetMapping("/sort-employee")
    public ModelAndView sort1() {
        ArrayList<Employee>employees=employeeService.sortService();
        ModelAndView modelAndView = new ModelAndView("/employee/sort");
        modelAndView.addObject("employees", employees);
        //modelAndView.addObject("messages", "employee updated successfully");
        return modelAndView;
        }

}

