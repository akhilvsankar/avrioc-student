package com.avrioc.student.controller;

import com.avrioc.student.model.DeleteStudent;
import com.avrioc.student.model.Student;
import com.avrioc.student.model.StudentResult;
import com.avrioc.student.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

/**
 * Rest controller class for the Student management.
 */
@RestController
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Post Controller method for creating a new student.
     * method is accessible only to admins.
     *
     * @param student
     * @return Mono publisher of Student type
     */
    @PostMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Student> create(@Valid @RequestBody Student student) {

        LOGGER.info("Request to create a student received :{}", student);
        studentService.createStudent(student);
        return Mono.justOrEmpty(student);
    }

    /**
     * Post Controller method for adding the result of a student.
     * This method will be invoked by web client, when a new result of a student is added from a browser/subscribers
     * Roll number of the student is validated against the grade and active status before adding the result.
     *
     * @param studentResult
     * @return Mono publisher of StudentResult type
     */
    @PostMapping("/result")
    public Mono<StudentResult> addResult(@Valid @RequestBody StudentResult studentResult) {

        LOGGER.info("Request to add student result received :{}", studentResult);
        boolean isValid = studentService.validateRollNumber(studentResult.getGrade(), studentResult.getRollNumber());
        if (!isValid) {
            throw new ValidationException("Roll number validation failed against grade and active status");
        }
        StudentResult response = studentService.addResult(studentResult);
        return Mono.justOrEmpty(response);
    }

    /**
     * GET controller method for retrieving the result of all students with active status.
     * method is accessible to admins and normal users.
     *
     * @return Flux publisher of StudentResult Type
     */
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    public Flux<StudentResult> getStudentsResult() {

        LOGGER.info("Request to get students result received ");
        List<StudentResult> response = studentService.getStudentsResult();
        return Flux.fromIterable(response);
    }

    /**
     * GET controller method for retrieving the result of a student with active status.
     * method is accessible to admins and normal users.
     *
     * @param rollNumber
     * @return Mono publisher of StudentResult type
     */
    @GetMapping("/students/result/{roll-number}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    public Mono<StudentResult> getStudentResultByRollNumber(@PathVariable("roll-number") Integer rollNumber) {

        LOGGER.info("Request to get student result for the roll number : {} received ", rollNumber);
        StudentResult response = studentService.getStudentResultByRollNumber(rollNumber);
        return Mono.justOrEmpty(response);
    }

    /**
     * Put Controller method to soft delete a student by making status as deleted.
     * method is accessible only to admins.
     *
     * @param deleteStudent
     * @return Mono publisher of Boolean type
     */
    @PutMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Boolean> delete(@Valid @RequestBody DeleteStudent deleteStudent) {

        LOGGER.info("Request to delete student having the roll number : {} received ", deleteStudent.getRollNumber());
        boolean response = studentService.deleteStudent(deleteStudent);
        return Mono.justOrEmpty(response);
    }

}
