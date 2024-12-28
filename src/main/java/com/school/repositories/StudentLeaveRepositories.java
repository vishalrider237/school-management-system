package com.school.repositories;

import com.school.entities.StudentLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudentLeaveRepositories extends JpaRepository<StudentLeave, Long> {

    @Query(value = "SELECT hd.name,hd.email,cd.body,cd.leavefrom,cd.leaveto,cd.student_leave_status,cd.subject,hd.id FROM USERS hd inner join STUDENT_LEAVE cd ON hd.id=cd.user_id",nativeQuery = true)
    List<Object> findAllLeaves();

    @Query(value = "SELECT hd.* from STUDENT_LEAVE hd where hd.user_id=?1",nativeQuery = true)
    List<StudentLeave> findByUserId(Long userId);

    @Query(value ="SELECT u.email,u.name,f.leaveto FROM users u LEFT JOIN student_leave  f ON u.id = f.user_id where f.leaveto between :start and :end",nativeQuery = true)
    List<Object[]> getAllUserLeave(LocalDateTime start, LocalDateTime end);
}
