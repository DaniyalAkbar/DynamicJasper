package com.dynamic.jasper.DynamicJasper.controller;

import com.dynamic.jasper.DynamicJasper.model.Employee;
import com.dynamic.jasper.DynamicJasper.service.EmployeeService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/listAll")
    private List<Employee> getAllEmployees(){
        return employeeService.getAllData();
    }

    @PostMapping("/insert")
    private void addEmployee(@RequestBody Employee empObj){
        employeeService.addEmployee(empObj);
    }

    @PostMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    private String printReport(@RequestParam int r, @RequestParam int c) throws JRException, ClassNotFoundException {
        return this.employeeService.fastReportbuilder(r,c);
    }

}
