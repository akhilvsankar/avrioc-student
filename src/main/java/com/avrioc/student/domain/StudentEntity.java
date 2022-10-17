package com.avrioc.student.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB's entity class for storing primary information of a student.
 */
@Document("student")
@Data
public class StudentEntity {

    @Id
    private String id;
    private String name;
    private Integer rollNumber;
    private String fatherName;
    private Integer grade;
    private String status;


}
