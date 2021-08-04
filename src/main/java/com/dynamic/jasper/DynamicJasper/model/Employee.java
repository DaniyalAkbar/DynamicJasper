package com.dynamic.jasper.DynamicJasper.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eid;

    private String name;
    private String designation;
    private String doj;
    private Long salary;



    public Long getEid() {
        return eid;
    }
    public void setEid(Long eid) {
        this.eid = eid;
    }


    public String getDoj() {
        return doj;
    }
    public void setDoj(String doj) {
        this.doj = doj;
    }

    public Long getSalary() {
        return salary;
    }
    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPropConcat(){
        return getEid().toString() + "@" + getName() + "@" + getDesignation() + "@" + getDoj() + "@" + getSalary().toString();
    }



    public Employee(){}

    public Employee(Long eid, String name, String designation, String doj, Long salary) {
        this.eid = eid;
        this.name = name;
        this.designation = designation;
        this.doj = doj;
        this.salary = salary;
    }

}
