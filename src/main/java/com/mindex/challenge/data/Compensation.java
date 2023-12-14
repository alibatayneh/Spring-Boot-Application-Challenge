package com.mindex.challenge.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Compensation {
    @Id
    @JsonIgnore
    private String employeeId;
    private Employee employee;
    private Double salary;
    private LocalDate effectiveDate;

    public Compensation() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String id) {
        this.employeeId = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEffectiveDate() {
        return this.effectiveDate.format(DateTimeFormatter.ISO_DATE);
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = LocalDate.parse(effectiveDate);
        ;
    }
}
