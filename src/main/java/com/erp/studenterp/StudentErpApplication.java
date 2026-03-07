package com.erp.studenterp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.erp.studenterp.entity.Department;
import com.erp.studenterp.entity.Student;
import com.erp.studenterp.repository.DepartmentRepository;
import com.erp.studenterp.repository.StudentRepository;

@SpringBootApplication
public class StudentErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentErpApplication.class, args);
    }

    @Bean
    CommandLineRunner run(StudentRepository studentRepo,
                          DepartmentRepository deptRepo) {

        return args -> {

            // ✅ Create and save Departments
            Department itDept = new Department();
            itDept.setName("IT");
            itDept = deptRepo.save(itDept);

            Department cseDept = new Department();
            cseDept.setName("CSE");
            cseDept = deptRepo.save(cseDept);

            // ✅ Create Students and assign departments
            Student student1 = new Student();
            student1.setName("Preethi");
            student1.setEmail("preethi@gmail.com");
            student1.setDepartment(itDept);

            Student student2 = new Student();
            student2.setName("Arun");
            student2.setEmail("arun@gmail.com");
            student2.setDepartment(cseDept);

            // ✅ Save students
            studentRepo.save(student1);
            studentRepo.save(student2);
        };
    }
}