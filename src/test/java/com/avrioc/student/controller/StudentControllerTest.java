package com.avrioc.student.controller;

import com.avrioc.student.config.WebFluxSecurityConfig;
import com.avrioc.student.model.DeleteStudent;
import com.avrioc.student.model.Student;
import com.avrioc.student.model.StudentResult;
import com.avrioc.student.repository.StudentRepository;
import com.avrioc.student.repository.StudentResultRepository;
import com.avrioc.student.service.StudentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

@WebFluxTest(StudentController.class)
@ExtendWith(SpringExtension.class)
@Import(WebFluxSecurityConfig.class)
@ActiveProfiles("test")
public class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StudentService studentService;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private StudentResultRepository studentResultRepository;

    private Student student;
    private StudentResult studentResult;


    @BeforeEach
    void setUp() {
        student = new Student();
        student.setRollNumber(1);
        student.setName("John");
        student.setGrade(1);
        student.setFatherName("James");

        studentResult = new StudentResult();
        studentResult.setTotalMarks(100f);
        studentResult.setObtainedMarks(70f);
        studentResult.setGrade(1);
        studentResult.setRollNumber(1);
    }

    @AfterEach
    void tearDown() {
        student = null;
        studentResult = null;

    }

    @Test
    @DisplayName(value = "test create student")
    void testCreateStudent() {

        Mockito.doNothing().when(studentService).createStudent(Mockito.any(Student.class));

        webTestClient.post()
                .uri("/students").bodyValue(student)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .value(Student::getRollNumber, equalTo(student.getRollNumber()));

    }

    @Test
    @DisplayName(value = "test for adding student result")
    void testAddStudentResult() {

        Mockito.when(studentService.validateRollNumber(Mockito.any(Integer.class), Mockito.any(Integer.class)))
                .thenReturn(true);

        Mockito.when(studentService.addResult(Mockito.any(StudentResult.class)))
                .thenReturn(studentResult);

        webTestClient.post()
                .uri("/result").bodyValue(studentResult)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StudentResult.class)
                .value(StudentResult::getRollNumber, equalTo(studentResult.getRollNumber()));

    }

    @Test
    @DisplayName(value = "test for retrieving students results")
    void testGetStudentsResults() {

        List<StudentResult> response = new ArrayList<>();
        response.add(studentResult);

        Mockito.when(studentService.getStudentsResult())
                .thenReturn(response);

        webTestClient.get()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentResult.class);

        Mockito.verify(studentService, Mockito.times(1)).getStudentsResult();

    }

    @Test
    @DisplayName(value = "test for retrieving student result by roll number")
    void testGetStudentResultByRollNumber() {

        Mockito.when(studentService.getStudentResultByRollNumber(studentResult.getRollNumber()))
                .thenReturn(studentResult);

        webTestClient.get()
                .uri("/students/result/{roll-number}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StudentResult.class)
                .value(StudentResult::getRollNumber, equalTo(studentResult.getRollNumber()));

    }

    @Test
    @DisplayName(value = "test for delete student")
    void testDeleteStudent() {

        Mockito.when(studentService.deleteStudent(Mockito.any(DeleteStudent.class)))
                .thenReturn(true);

        DeleteStudent deleteStudent = new DeleteStudent();
        deleteStudent.setRollNumber(1);
        deleteStudent.setGrade(1);

        EntityExchangeResult<Boolean> result = webTestClient.put()
                .uri("/delete").bodyValue(deleteStudent)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class).returnResult();

        Assertions.assertEquals(Boolean.TRUE, result.getResponseBody());

    }
}
