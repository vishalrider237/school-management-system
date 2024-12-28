package com.school.config;

import com.school.entities.StudentLeave;
import com.school.services.sudent.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerConfig {

    @Autowired
    private StudentService studentService;

    @Scheduled(cron = "0 0 0 * * ?")  //runs everyday at 12AM
    public void UnpaidFeeCheckStudents(){
        studentService.getFeeStatusPerStudent();
    }
   @Scheduled(cron = "0 0 10 * * *")//runs everyday at 10Am
    public void StudentLeaveStatus(){
        studentService.sendMailForStudentLeaveStatus();
    }
}
