package com.avrioc.student.client;

import com.avrioc.student.model.StudentResult;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client class that has web client configured to accept an incoming result of student
 */
@Component
public class StudentClient {

    private final WebClient client;

    /**
     * @param builder
     */
    public StudentClient(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:8080").build();
    }

    /**
     * Reactive method to receive incoming result of a student.
     * will be redirected to StudentController with the payload received.
     *
     * @param studentResult
     * @return Mono publisher of StudentResult Type.
     */
    public Mono<StudentResult> addResult(StudentResult studentResult) {
        return this.client.post().uri("/result").accept(MediaType.APPLICATION_JSON)
                .bodyValue(studentResult)
                .retrieve()
                .bodyToMono(StudentResult.class);

    }

}
