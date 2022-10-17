package com.avrioc.student.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB's entity class for storing results of students.
 */
@Document("studentResult")
@Data
public class StudentResultEntity {

    @Id
    private String id;

    private Float totalMarks;

    private Float obtainedMarks;

    private Integer rollNumber;

    private Integer grade;

    private String remarks;

    private String studentStatus;

}
