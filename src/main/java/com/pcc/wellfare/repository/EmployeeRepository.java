package com.pcc.wellfare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pcc.wellfare.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDeptid(String deptid);

    Optional<Employee> findById(Long empid);

    void deleteById(Long empid);
    // public List<Map<String, String>> findAllByOrderByBoardTypeIdAsc(RestManager
    // exManager){
    // List<Map<String, String>> boardTypesList new ArrayList<>(List<Employee>
    // findByCode(String code);

    // try {
    // String sql = "SeELECT BOARD_TYPE_ID, BOARD_TYPE_NAME FROM
    // EGPaPPEAL.MEETING_BOARD_TYPE "
    // +"ORDER BY BOARD_TYPE_ID_ASC";
    // List<MeetingBoardTypeEntity> allBoardType = simpleDataTableRepository
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

}
