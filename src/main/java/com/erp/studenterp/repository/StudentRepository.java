package com.erp.studenterp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.studenterp.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}