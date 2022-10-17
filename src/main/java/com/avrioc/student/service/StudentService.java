package com.avrioc.student.service;

import com.avrioc.student.domain.StudentEntity;
import com.avrioc.student.domain.StudentResultEntity;
import com.avrioc.student.model.DeleteStudent;
import com.avrioc.student.model.Student;
import com.avrioc.student.model.StudentResult;
import com.avrioc.student.repository.StudentRepository;
import com.avrioc.student.repository.StudentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for business logic management of student.
 */
@Service
@Transactional
public class StudentService {

    private static final String ACTIVE_STATUS = "Active";

    private static final String INACTIVE_STATUS = "Deleted";
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentResultRepository studentResultRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * creates a  new student by saving to MongoDB table.
     *
     * @param student
     */
    public void createStudent(Student student) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setName(student.getName());
        studentEntity.setFatherName(student.getFatherName());
        studentEntity.setStatus(ACTIVE_STATUS);
        studentEntity.setRollNumber(student.getRollNumber());
        studentEntity.setGrade(student.getGrade());
        studentRepository.save(studentEntity);
    }

    /**
     * adds a  new student result by saving to MongoDB table.
     *
     * @param studentResult
     * @return StudentResult
     */
    public StudentResult addResult(StudentResult studentResult) {
        StudentResultEntity studentResultEntity = studentResultRepository.findByRollNumberAndStudentStatus(studentResult.getRollNumber(), ACTIVE_STATUS);
        /* if student result is not available, create a new entity,
            else update the existing
         */
        if (studentResultEntity == null) {
            studentResultEntity = new StudentResultEntity();
        }
        studentResultEntity.setTotalMarks(studentResult.getTotalMarks());
        studentResultEntity.setObtainedMarks(studentResult.getObtainedMarks());
        studentResultEntity.setGrade(studentResult.getGrade());
        studentResultEntity.setRollNumber(studentResult.getRollNumber());
        studentResultEntity.setStudentStatus(ACTIVE_STATUS);
        float ratio = studentResult.getObtainedMarks() / studentResult.getTotalMarks();
        int percentage = (int) Math.floor((ratio) * 100);
        // if percentage is >=50 , set remarks as passed else failed.
        studentResultEntity.setRemarks(percentage >= 50 ? "passed" : "failed");
        studentResultRepository.save(studentResultEntity);
        studentResult.setRemarks(studentResultEntity.getRemarks());
        setStudentPosition(studentResult);
        return studentResult;
    }

    /**
     * check if grade stored in DB table and the grade received in the request are equal
     * Also check if status is active
     * if either of above checks fail, return false.
     * else, return true
     *
     * @param grade
     * @param rollNumber
     * @return true or false
     */
    public boolean validateRollNumber(Integer grade, Integer rollNumber) {
        StudentEntity studentEntity = studentRepository.getGradeAndStatus(rollNumber);
        return studentEntity != null && studentEntity.getGrade().equals(grade) && studentEntity.getStatus().equals(ACTIVE_STATUS);
    }

    /**
     * find number of students whose obtained marks are greater than current student's obtained marks.
     * position of current student will be 1+above result.
     * query using MongoDB template.
     *
     * @param studentResult
     */
    private void setStudentPosition(StudentResult studentResult) {
        long pos = mongoTemplate.count(new Query().addCriteria(Criteria.where("obtainedMarks").gt(studentResult.getObtainedMarks())), StudentResultEntity.class);
        Integer position = (int) pos + 1;
        studentResult.setPositionInClass(position);
    }

    /**
     * get all active students result by quering MongoDB table.
     * set position of each student by sorting in descending order of obtained marks
     *
     * @return Collection of StudentResult
     */
    public List<StudentResult> getStudentsResult() {
        List<StudentResultEntity> studentResults = studentResultRepository.findAllByStudentStatus(ACTIVE_STATUS);
        if (!CollectionUtils.isEmpty(studentResults)) {
            int[] counter = {0};
            float[] mark = {Float.MIN_VALUE};
            List<StudentResult> studentResultList = new ArrayList<>();
            studentResults.stream()
                    .sorted(Comparator.comparing(StudentResultEntity::getObtainedMarks).reversed()).forEach(sr -> {
                        StudentResult studentResult = new StudentResult();
                        // if obtained marks is same as previous, maintain same position
                        if (mark[0] == sr.getObtainedMarks()) {
                            studentResult.setPositionInClass(counter[0]);
                        } else {
                            studentResult.setPositionInClass(++counter[0]);
                        }
                        mark[0] = sr.getObtainedMarks();
                        studentResult.setRollNumber(sr.getRollNumber());
                        studentResult.setTotalMarks(sr.getTotalMarks());
                        studentResult.setObtainedMarks(sr.getObtainedMarks());
                        studentResult.setGrade(sr.getGrade());
                        studentResult.setRemarks(sr.getRemarks());
                        studentResultList.add(studentResult);
                    });
            return studentResultList;
        }
        return new ArrayList<>();
    }

    /**
     * get result of an active student by querying MongoDB table with input parameter as roll number
     *
     * @param rollNumber
     * @return StudentResult
     */
    public StudentResult getStudentResultByRollNumber(Integer rollNumber) {
        StudentResultEntity studentResultEntity = studentResultRepository.findByRollNumberAndStudentStatus(rollNumber, ACTIVE_STATUS);
        // if no student result found, return an empty object as response.
        if (studentResultEntity == null) {
            return new StudentResult();
        }
        StudentResult response = new StudentResult();
        response.setRollNumber(studentResultEntity.getRollNumber());
        response.setRemarks(studentResultEntity.getRemarks());
        response.setGrade(studentResultEntity.getGrade());
        response.setObtainedMarks(studentResultEntity.getObtainedMarks());
        response.setTotalMarks(studentResultEntity.getTotalMarks());
        setStudentPosition(response);
        return response;
    }

    /**
     * soft delete student result and student table entry by making status as deleted.
     * soft deleted entries will not be fetched
     *
     * @param deleteStudent
     * @return true
     */
    public boolean deleteStudent(DeleteStudent deleteStudent) {
        StudentEntity studentEntity = studentRepository.findByRollNumberAndGrade(deleteStudent.getRollNumber(), deleteStudent.getGrade());
        if (studentEntity != null) {
            studentEntity.setStatus(INACTIVE_STATUS);
            studentRepository.save(studentEntity);
        }
        StudentResultEntity studentResultEntity = studentResultRepository.findByRollNumberAndGrade(deleteStudent.getRollNumber(), deleteStudent.getGrade());
        if (studentResultEntity != null) {
            studentResultEntity.setStudentStatus(INACTIVE_STATUS);
            studentResultRepository.save(studentResultEntity);
        }
        return true;
    }
}
