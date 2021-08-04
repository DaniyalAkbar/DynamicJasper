package com.dynamic.jasper.DynamicJasper.repository;

import com.dynamic.jasper.DynamicJasper.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
