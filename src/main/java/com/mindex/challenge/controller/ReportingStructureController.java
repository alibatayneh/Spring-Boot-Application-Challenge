package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/employee/{employeeId}/reporting-structure")
    public ReportingStructure read(@PathVariable String employeeId) {
        LOG.debug("Received reportingStructure get request for id [{}]", employeeId);

        return reportingStructureService.read(employeeId);
    }
}
