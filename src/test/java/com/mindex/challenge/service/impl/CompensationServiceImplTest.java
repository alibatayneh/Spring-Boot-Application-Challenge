package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

    @Test
    public void testCreateReadUpdate() {
        Compensation testCompensation = new Compensation();
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        testCompensation.setSalary(100000.0);
        testCompensation.setEffectiveDate("2023-06-06");

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation,
                Compensation.class, employeeId).getBody();

        assertNotNull(createdCompensation.getEmployee());
        assertEquals(employeeId, createdCompensation.getEmployee().getEmployeeId());
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationUrl, Compensation.class,
                employeeId).getBody();
        assertNotNull(readCompensation.getEmployee());
        assertEquals(employeeId, readCompensation.getEmployee().getEmployeeId());
        assertCompensationEquivalence(createdCompensation, readCompensation);


        // Update checks
        readCompensation.setSalary(200000.0);
        readCompensation.setEffectiveDate("2023-10-10");

        Compensation updatedCompensation = restTemplate.postForEntity(compensationUrl, readCompensation,
                Compensation.class, readCompensation.getEmployee().getEmployeeId()).getBody();

        assertNotNull(updatedCompensation.getEmployee());
        assertEquals(readCompensation.getEmployee().getEmployeeId(), updatedCompensation.getEmployee().getEmployeeId());
        assertCompensationEquivalence(readCompensation, updatedCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
