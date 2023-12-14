package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting-structure";
    }

    @Test
    public void testRead() {
        ReportingStructure testReports = new ReportingStructure();
        Employee employee = new Employee();
        // Use John Lennon employee which number of reports should be 4.
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        employee.setEmployeeId(employeeId);
        testReports.setEmployee(employee);
        testReports.setNumberOfReports(4);

        // Read checks
        ReportingStructure readReports = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class,
                employeeId).getBody();
        assertNotNull(readReports);
        assertReportingStructureEquivalence(testReports, readReports);
    }

    private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}
