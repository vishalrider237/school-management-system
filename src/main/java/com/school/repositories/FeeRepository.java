package com.school.repositories;

import com.school.entities.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    @Query("SELECT hd.feeStatus from Fee hd where hd.user.id=:id")
    Boolean getFeeStatus(@Param("id") long id);
    @Query(value = "SELECT u.email,u.name, f.fee_status FROM users u LEFT JOIN fee f ON u.id = f.user_id;",nativeQuery = true)
    List<Object[]>getallStudentWithFee();
}
