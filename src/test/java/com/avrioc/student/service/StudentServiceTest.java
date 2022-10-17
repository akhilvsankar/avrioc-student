package com.avrioc.student.service;

import com.avrioc.student.domain.StudentEntity;
import com.avrioc.student.domain.StudentResultEntity;
import com.avrioc.student.model.DeleteStudent;
import com.avrioc.student.model.Student;
import com.avrioc.student.model.StudentResult;
import com.avrioc.student.repository.StudentRepository;
import com.avrioc.student.repository.StudentResultRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentResultRepository studentResultRepository;
    @Mock
    private MongoTemplate mongoTemplate;
    private StudentResultEntity studentResultEntity;
    private StudentEntity studentEntity;

    private static final String ACTIVE_STATUS = "Active";

    @BeforeEach
    void setUp() {
        Integer rollNumber = 1;
        Integer grade = 1;
        studentResultEntity = new StudentResultEntity();
        studentResultEntity.setRollNumber(rollNumber);
        studentResultEntity.setGrade(grade);
        studentResultEntity.setObtainedMarks(100f);
        studentResultEntity.setStudentStatus(ACTIVE_STATUS);

        studentEntity = new StudentEntity();
        studentEntity.setRollNumber(rollNumber);
        studentEntity.setGrade(grade);
        studentEntity.setStatus(ACTIVE_STATUS);
    }

    @AfterEach
    void tearDown() {
        studentResultEntity = null;
        studentEntity = null;
    }

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName(value = "test for new student creation")
    void testCreateStudent() {
        studentService.createStudent(new Student());
        Mockito.verify(studentRepository, Mockito.atLeast(1)).save(Mockito.any(StudentEntity.class));
    }

    @Test
    @DisplayName(value = "test for adding student passed result")
    void testAddStudentPassResult() {
        StudentResult studentResult = new StudentResult();
        studentResult.setTotalMarks(100f);
        studentResult.setObtainedMarks(80f);
        studentResult.setRollNumber(1);
        studentResult.setGrade(1);
        StudentResult response = studentService.addResult(studentResult);
        Assertions.assertEquals("passed", response.getRemarks());
    }

    @Test
    @DisplayName(value = "test for adding student failed result")
    void testAddStudentFailResult() {
        StudentResult studentResult = new StudentResult();
        studentResult.setTotalMarks(100f);
        studentResult.setObtainedMarks(40f);
        studentResult.setRollNumber(2);
        studentResult.setGrade(2);
        StudentResult response = studentService.addResult(studentResult);
        Assertions.assertEquals("failed", response.getRemarks());
    }

    @Test
    @DisplayName(value = "test for validating roll number as truthy")
    void testValidateRollNumberTruthy() {
        Mockito.when(studentRepository.getGradeAndStatus(studentEntity.getRollNumber()))
                .thenReturn(studentEntity);
        boolean response = studentService.validateRollNumber(studentEntity.getGrade(), studentEntity.getRollNumber());
        Assertions.assertTrue(response);
    }

    @Test
    @DisplayName(value = "test for validating roll number as falsy")
    void testValidateRollNumberFalsy() {
        Mockito.when(studentRepository.getGradeAndStatus(studentEntity.getRollNumber()))
                .thenReturn(studentEntity);
        boolean response = studentService.validateRollNumber(studentEntity.getGrade() + 1, studentEntity.getRollNumber());
        Assertions.assertFalse(response);
    }

    @Test
    @DisplayName(value = "test for getting students results")
    void testGetStudentResults() {
        var list = new ArrayList<StudentResultEntity>();
        list.add(studentResultEntity);
        Mockito.when(studentResultRepository.findAllByStudentStatus(studentResultEntity.getStudentStatus()))
                .thenReturn(list);
        List<StudentResult> response = studentService.getStudentsResult();
        Assertions.assertEquals(studentResultEntity.getRollNumber(), response.get(0).getRollNumber());
    }

    @Test
    @DisplayName(value = "test for getting student result by roll number")
    void testGetStudentResultByRollNumber() {
        Mockito.when(studentResultRepository.findByRollNumberAndStudentStatus(studentResultEntity.getRollNumber(), studentResultEntity.getStudentStatus()))
                .thenReturn(studentResultEntity);
        StudentResult response = studentService.getStudentResultByRollNumber(studentEntity.getRollNumber());
        Assertions.assertEquals(studentResultEntity.getRollNumber(), response.getRollNumber());
    }

    @Test
    @DisplayName(value = "test for deleting student")
    void testDeleteStudent() {
        DeleteStudent deleteStudent = new DeleteStudent();
        deleteStudent.setGrade(studentEntity.getGrade());
        deleteStudent.setRollNumber(studentEntity.getRollNumber());
        Mockito.when(studentResultRepository.findByRollNumberAndGrade(deleteStudent.getRollNumber(),deleteStudent.getGrade()))
                .thenReturn(studentResultEntity);
        Mockito.when(studentRepository.findByRollNumberAndGrade(deleteStudent.getRollNumber(),deleteStudent.getGrade()))
                .thenReturn(studentEntity);
        boolean response = studentService.deleteStudent(deleteStudent);
        Assertions.assertTrue(response);
    }

}
