package com.erp.studenterp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.studenterp.entity.Student;
import com.erp.studenterp.exception.StudentNotFoundException;
import com.erp.studenterp.repository.StudentRepository; // ✅ ADD THIS

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create Student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get All Students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get Student By ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> 
                    new StudentNotFoundException("Student not found with id: " + id));
    }

    // Delete Student
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }

    // Update Student
    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                    new StudentNotFoundException("Student not found with id: " + id));

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setDepartment(updatedStudent.getDepartment());

        return studentRepository.save(existingStudent);
    }
}