package com.pcc.wellfare.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pcc.wellfare.CSVRepresentation.EmployeeCsvRepresentation;
import com.pcc.wellfare.model.Budget;
import com.pcc.wellfare.model.Dept;
import com.pcc.wellfare.repository.BudgetRepository;
import com.pcc.wellfare.repository.DeptRepository;
import com.pcc.wellfare.requests.CreateEmployeeRequest;
import com.pcc.wellfare.requests.EditEmployeeRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pcc.wellfare.repository.EmployeeRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.pcc.wellfare.model.Employee;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BudgetRepository budgetRepository;
    private final DeptRepository deptRepository;
    private EntityManager entityManager;

    public EmployeeService(EmployeeRepository employeeRepository, BudgetRepository budgetRepository,
            DeptRepository deptRepository, EntityManager entityManager) {
        this.employeeRepository = employeeRepository;
        this.budgetRepository = budgetRepository;
        this.deptRepository = deptRepository;
        this.entityManager = entityManager;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> searchById(Long userId) {
        return employeeRepository.findById(userId);
    }

    public Employee createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        String email = createEmployeeRequest.getEmail();

        if (email == null || email.isEmpty() || email.equals("")) {
        } else {
            if (employeeRepository.existsByEmail(email)) {
                throw new RuntimeException("Email is already in use.");
            }
        }
        System.out.println(createEmployeeRequest.getLevel());
        Optional<Budget> budgetOptional = budgetRepository.findByLevel(createEmployeeRequest.getLevel());
        Budget budget = budgetOptional.orElseThrow(() -> new RuntimeException("Budget not found"));
        Optional<Dept> deptOptional = deptRepository.findByCode(createEmployeeRequest.getCode());
        Dept dept = deptOptional.orElseThrow(() -> new RuntimeException("Dept not found"));
        Employee employee = Employee
                .builder()
                .empid(createEmployeeRequest.getEmpId())
                .dept(dept)
                .tprefix(createEmployeeRequest.getTPrefix())
                .email(createEmployeeRequest.getEmail())
                .tname(createEmployeeRequest.getTName())
                .tsurname(createEmployeeRequest.getTSurname())
                .tposition(createEmployeeRequest.getTPosition())
                .budget(budget)
                .remark(createEmployeeRequest.getRemark())
                .status(createEmployeeRequest.getStatus())
                .build();

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long userid, EditEmployeeRequest editEmployeeRequest) {
//        String email = editEmployeeRequest.getEmail();

//        if (email == null || email.isEmpty() || email.equals("")) {
//        } else {
//            if (employeeRepository.existsByEmail(email)) {
//                throw new RuntimeException("Email is already in use.");
//            }
//        }
        Optional<Budget> budgetOptional = budgetRepository.findByLevel(editEmployeeRequest.getLevel());
        Budget budget = budgetOptional.orElseThrow(() -> new RuntimeException("Budget not found"));
        Optional<Dept> deptOptional = deptRepository.findById(editEmployeeRequest.getDeptCode());
        Dept dept = deptOptional.orElseThrow(() -> new RuntimeException("Dept not found"));
        Employee employee = employeeRepository.findById(userid)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: "
                        + userid));

        employee.setDept(dept);
        employee.setEmail(editEmployeeRequest.getEmail());
        employee.setTprefix(editEmployeeRequest.getTPrefix());
        employee.setTname(editEmployeeRequest.getTName());
        employee.setTsurname(editEmployeeRequest.getTSurname());
        employee.setTposition(editEmployeeRequest.getTPosition());
        employee.setBudget(budget);
        employee.setRemark(editEmployeeRequest.getRemark());
        employee.setStatus(this.getStatusCode(editEmployeeRequest.getRemark()));
        return employeeRepository.save(employee);
    }
    
    private String getStatusCode(String type) {
    	if(type == "พนักงานประจำ") {
    		return "1";
    	}
    	else if(type == "สัญญาจ้าง") {
    		return "2";
    	}
    	else {
    		return "3";
    	}
    }

    public String deleteEmployee(Long userid) {
        Optional<Employee> optionalemployee = employeeRepository.findById(userid);
        if (optionalemployee.isPresent()) {
            employeeRepository.deleteById(userid);
            return "ลบข้อมูลของ ID : " + userid + " เรียบร้อย";
        } else {
            return null;
        }
    }

    public Object searchUser(
            String empid,
            String tprefix,
            String tname,
            String tsurname,
            String tposition,
            String budget,
            String dept,
            String remark,
            String email,
            String status) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        List<Predicate> predicates = new ArrayList<>();

        if (empid != null) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("empid")),
                            "%" + empid.toLowerCase() + "%"));
        }

        if (tname != null) {
            Expression<String> fullName = builder.concat(
                    builder.concat(builder.lower(root.get("firstname")), " "),
                    builder.lower(root.get("lastname")));
            predicates.add(
                    builder.like(builder.lower(fullName), "%" + tname.toLowerCase() + "%"));
        }
        if (tsurname != null) {
            Expression<String> fullName = builder.concat(
                    builder.concat(builder.lower(root.get("firstname")), " "),
                    builder.lower(root.get("lastname")));
            predicates.add(
                    builder.like(builder.lower(fullName), "%" + tsurname.toLowerCase() + "%"));
        }

        if (email != null) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("email")),
                            "%" + email.toLowerCase() + "%"));
        }

        if (tposition != null && !tposition.isEmpty()) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("tposition")),
                            "%" + tposition.toLowerCase() + "%"));
        }

        if (budget != null && !budget.isEmpty()) {
            predicates.add(
                    builder.equal(
                            root.get("budget"),
                            budget));
        }

        if (dept != null && !dept.isEmpty()) {
            predicates.add(
                    builder.equal(
                            root.get("department"),
                            dept));
        }

        if (remark != null && !remark.isEmpty()) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("remark")),
                            "%" + remark.toLowerCase() + "%"));
        }

        if (email != null && !email.isEmpty()) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("email")),
                            "%" + email.toLowerCase() + "%"));
        }

        if (status != null && !status.isEmpty()) {
            predicates.add(
                    builder.equal(
                            root.get("status"),
                            status));
        }

        if (predicates.isEmpty()) {
            return "ไม่พบรายการที่ต้องการค้นหา";
        }

        query.where(predicates.toArray(new Predicate[0]));

        List<Employee> employees = entityManager.createQuery(query).getResultList();

        if (employees.isEmpty()) {
            return "ไม่พบรายการที่ต้องการค้นหา";
        }

        return employees;
    }

    public Integer uploadEmps(MultipartFile file) throws IOException {
        Set<Employee> Emps = parseCsv(file);
        employeeRepository.saveAll(Emps);
        return Emps.size();
    }

    private Set<Employee> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<EmployeeCsvRepresentation> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(EmployeeCsvRepresentation.class);
            CsvToBean<EmployeeCsvRepresentation> csvToBean = new CsvToBeanBuilder<EmployeeCsvRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse().stream()
                    .map(csvLine -> {
                        Budget budget = budgetRepository.findByLevel(csvLine.getLevel()).orElse(null);
                        Dept dept = deptRepository.findById(csvLine.getCode()).orElse(null);

                        return Employee.builder()
                                .empid(csvLine.getEmpid())
                                .tprefix(csvLine.getTprefix())
                                .tname(csvLine.getTname())
                                .tsurname(csvLine.getTsurname())
                                .tposition(csvLine.getTposition())
                                .budget(budget)
                                .dept(dept)
                                .status(csvLine.getStatus())
                                .remark(csvLine.getRemark())
                                .email(csvLine.getEmail())
                                .build();
                    })
                    .collect(Collectors.toSet());
        }
    }

}
