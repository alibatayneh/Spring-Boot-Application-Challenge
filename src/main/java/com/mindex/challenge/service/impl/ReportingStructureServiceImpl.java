package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Stack;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Stack<Employee> stack = new Stack<>();
        Integer numberOfReports = 0;

        // Using the EmployeeService instead of repository to offload the null check
        //  and any other potential complex business logic that may surround fetching an employee.
        Employee employee = employeeService.read(id);

        // Using ofNullable to have cleaner code for adding reportees to the stack and null checking.
        Optional.ofNullable(employee.getDirectReports()).ifPresent(stack::addAll);

        // Pop direct reports from stack, increment the numberOfReports, 
        //  and add the next direct reports to the stack, repeat until stack is empty.
        while (!stack.isEmpty()) {
            // Pop the direct reports from the stack.
            Employee reportee = stack.pop();
            reportee = employeeService.read(reportee.getEmployeeId());

            // Increment the number of reportees to be returned.
            numberOfReports++;

            // Add the list of next direct reports to the stack.
            Optional.ofNullable(reportee.getDirectReports()).ifPresent(stack::addAll);
        }

        // Potential Improvement(minor): Add employee, numberOfReports in constructor.
        ReportingStructure reports = new ReportingStructure();
        reports.setEmployee(employee);
        reports.setNumberOfReports(numberOfReports);

        return reports;
    }
}
