package com.school.repositories;

import com.school.entities.User;
import com.school.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositories extends JpaRepository<User,Long> {

    List<User> findByUserRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.userRole = :role ORDER BY u.createdOn DESC")
    Page<User> findByUserRole(@Param("role")UserRole role, Pageable pageable);


    Optional<User> findByEmail(String email);


    @Query(value = "SELECT u.name,u.email,u.student_class ,u.address ,u.date_of_birth ,u.father_name ,u.gender ,u.mother_name ,f.body ,f.leavefrom ,f.leaveto ,f.subject ,g.amount ,g.description ,g.given_by ,g.month,g.fee_status  FROM users u LEFT JOIN student_leave  f ON u.id = f.user_id left join fee g on u.id =g.user_id ",nativeQuery = true)
    List<Object[]> getAllRecord();

    @Query(value = "SELECT count(hd.*) FROM users hd where hd.user_role ='1'",nativeQuery = true)
    Long countAllStudents();
}
