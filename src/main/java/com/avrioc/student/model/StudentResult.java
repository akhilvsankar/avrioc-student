package com.avrioc.student.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * POJO used in web client for adding the result of a student.
 * Also used for returning student result in APIs.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StudentResult {
    @Min(value = 1, message = "total marks should be minimum 1")
    private Float totalMarks;

    @NotNull(message = "obtained marks is required")
    private Float obtainedMarks;

    @Min(value = 1, message = "Required min roll number is 1")
    @Max(value = 100, message = "Required max roll number is 100")
    private Integer rollNumber;

    @Min(value = 1, message = "Required min grade is 1")
    @Max(value = 10, message = "Required max grade is 10")
    private Integer grade;

    private String remarks;
    private Integer positionInClass;

}
