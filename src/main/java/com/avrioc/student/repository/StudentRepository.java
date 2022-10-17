package com.avrioc.student.repository;

import com.avrioc.student.domain.StudentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Spring Data MongoDB repository for student entity.
 */
public interface StudentRepository extends MongoRepository<StudentEntity, String> {
    /**
     * Method to get grade and status with input as roll number.
     *
     * @param rollNumber
     * @return StudentEntity
     */
    @Query(value = "{rollNumber: ?0}", fields = "{grade:1, status:1}")
    StudentEntity getGradeAndStatus(Integer rollNumber);

    /**
     * Inline query method to find a student with inputs - roll number and grade.
     *
     * @param rollNumber
     * @param grade
     * @return StudentEntity
     */
    StudentEntity findByRollNumberAndGrade(Integer rollNumber, Integer grade);
}
