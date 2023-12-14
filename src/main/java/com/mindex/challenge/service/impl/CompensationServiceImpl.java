package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(String id, Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        // Using the EmployeeService instead of repository to offload the null check
        //  and any other potential complex business logic that may surround fetching an employee.
        Employee employee = employeeService.read(id);

        // Check if the compensation exists, if it exists we update the existing compensation,
        //  otherwise we create a new compensation.
        Compensation existingCompensation = compensationRepository.findByEmployeeId(id);
        if (existingCompensation == null) {
            compensation.setEmployeeId(id);
            compensation.setEmployee(employee);
            compensation = compensationRepository.insert(compensation);
        } else {
            existingCompensation.setSalary(compensation.getSalary());
            existingCompensation.setEffectiveDate(compensation.getEffectiveDate());
            compensation = this.update(existingCompensation);
        }

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation with employee id [{}]", id);

        // Return a not found response if the compensation is not found, keep the response message simple,
        //  we don't want to reveal too much information for security purposes.
        return Optional.ofNullable(compensationRepository.findByEmployeeId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compensation not found."));
    }

    @Override
    public Compensation update(Compensation compensation) {
        LOG.debug("Updating compensation for employee id [{}]", compensation.getEmployee().getEmployeeId());

        return compensationRepository.save(compensation);
    }
}
