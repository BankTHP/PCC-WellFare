package com.pcc.wellfare;

import com.pcc.wellfare.service.BudgetService;
import com.pcc.wellfare.service.DeptService;
import com.pcc.wellfare.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
@Component
public class DatabaseInitialzer implements CommandLineRunner {


    private final BudgetService budgetService;
    private final DeptService deptService;
    private  final EmployeeService employeeService;
    private final JdbcTemplate jdbcTemplate;
    private HttpHeaders headers = new HttpHeaders();

    public DatabaseInitialzer(BudgetService budgetService, DeptService deptService, EmployeeService employeeService, JdbcTemplate jdbcTemplate) {
        this.budgetService = budgetService;
        this.deptService = deptService;
        this.employeeService = employeeService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        insertDataIntoEmployeeTable();
        insertDataIntoBudgetTable();
        insertDataIntodeptTable();
    }

    private boolean isTableEmpty(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName;
            int count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count == 0;
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
    }

    public void insertDataIntoEmployeeTable() throws FileNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/employee/uploadEmps";
        if (isTableEmpty("public.employee")) {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            File file = new File("src/main/resources/fileExcel/Employee_1.csv");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
        }
    }
    private void insertDataIntoBudgetTable() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/budget/uploadBudgets";
        if (isTableEmpty("public.budget")) {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            File file = new File("src/main/resources/fileExcel/Budget_1.csv");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
        }
    }

    private void insertDataIntodeptTable() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/dept/uploadDepts";
        if (isTableEmpty("public.dept")) {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            File file = new File("src/main/resources/fileExcel/Dept_1.csv");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
        }
    }
}
