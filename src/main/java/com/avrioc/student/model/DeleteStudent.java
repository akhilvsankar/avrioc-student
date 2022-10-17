package com.avrioc.student.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * POJO used in request body for deleting a student.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DeleteStudent {

    @Min(value = 1, message = "Required min roll number is 1")
    @Max(value = 100, message = "Required max roll number is 100")
    private Integer rollNumber;

    @Min(value = 1, message = "Required min grade is 1")
    @Max(value = 10, message = "Required max grade is 10")
    private Integer grade;


}
