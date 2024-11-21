package com.yb.controller;

import com.yb.domain.jpa.Student;
import com.yb.dto.response.StudentResponseDto;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class StudentControllerTest {

  RestTemplate restTemplate = new RestTemplate();
  Random random = new Random();
  private Logger log = LoggerFactory.getLogger(StudentControllerTest.class);

  @Test
  void create_many_student() {
    for (int i = 0; i < 1000; i++) {
      String name = "학생" + (random.nextInt(1000) + 1);
      int age = random.nextInt(100);
      var resp = restTemplate.postForEntity(
          "http://localhost:8089/student",
          new Student(name, age),
          StudentResponseDto.class
      );

      if (resp.getStatusCode().isError()) {
        log.info("error: {}", resp.getStatusCode());
      }
    }
  }

}
