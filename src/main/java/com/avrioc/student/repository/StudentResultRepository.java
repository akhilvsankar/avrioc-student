package com.avrioc.student.repository;

import com.avrioc.student.domain.StudentResultEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for student result entity.
 */
public interface StudentResultRepository extends MongoRepository<StudentResultEntity, String> {

    /**
     * Inline query method to find a student result with inputs - roll number and active status
     *
     * @param rollNumber
     * @param status
     * @return StudentResultEntity
     */
    StudentResultEntity findByRollNumberAndStudentStatus(Integer rollNumber, String status);

    /**
     * Inline query method to find a student result with inputs - roll number and grade
     *
     * @param rollNumber
     * @param grade
     * @return StudentResultEntity
     */
    StudentResultEntity findByRollNumberAndGrade(Integer rollNumber, Integer grade);

    /**
     * Inline query method to get all active students results
     *
     * @param activeStatus
     * @return List of StudentResultEntity
     */
    List<StudentResultEntity> findAllByStudentStatus(String activeStatus);
}
